#include "Net/RealtimeWebSocketConnection.h"
#include "Async/Async.h"
#include "Engine/Engine.h"
#include "Engine/World.h"
#include "HAL/PlatformProcess.h"
#include "HAL/CriticalSection.h"
#include "IWebSocket.h"
#include "Misc/ConfigCacheIni.h"
#include "Modules/ModuleManager.h"
#include "Net/ProtobufCodec.h"
#include "Templates/SharedPointer.h"
#include "UObject/WeakObjectPtr.h"
#include "WebSocketsModule.h"

URealtimeWebSocketConnection::URealtimeWebSocketConnection() {
  bIsConnected = false;
  LastRTT = 0;
  ServerPort = 0;
  ClientTick = 0;
  WebSocket = nullptr;
}

URealtimeWebSocketConnection::~URealtimeWebSocketConnection() {
  bIsConnected = false;

  if (WebSocket.IsValid()) {
    WebSocket->Close();
    WebSocket.Reset();
  }

  HeartbeatTimerHandle.Invalidate();
}

void URealtimeWebSocketConnection::BeginDestroy() {
  Disconnect();
  Super::BeginDestroy();
}

void URealtimeWebSocketConnection::Connect(const FString &InServerAddress,
                                           int32 InServerPort,
                                           const FString &InToken) {
  if (bIsConnected) {
    UE_LOG(LogTemp, Log,
           TEXT("WebSocketConnection: Already connected, skipping connection"));
    return;
  }

  if (WebSocket.IsValid()) {
    UE_LOG(LogTemp, Log,
           TEXT("WebSocketConnection: WebSocket exists but not connected, "
                "cleaning up..."));
    CleanupWebSocket();
  }

  ServerAddress = InServerAddress;
  ServerPort = InServerPort;
  AuthToken = InToken;

  UE_LOG(LogTemp, Log, TEXT("WebSocketConnection: Connecting to %s:%d"),
         *ServerAddress, ServerPort);

  if (!InitializeWebSocket()) {
    UE_LOG(LogTemp, Error,
           TEXT("WebSocketConnection: Failed to initialize WebSocket"));
    OnDisconnected.Broadcast(TEXT("Failed to initialize WebSocket"));
    return;
  }

  UE_LOG(LogTemp, Log,
         TEXT("WebSocketConnection: WebSocket initialized, waiting for "
              "connection..."));
}

void URealtimeWebSocketConnection::ConnectWithConfig(const FString &InToken) {
  FString ConfigServerAddress;
  int32 ConfigServerPort = 18080;
  float HeartbeatInterval = 1.0f;
  float ConnectionTimeout = 5.0f;

  if (GConfig) {
    GConfig->GetString(TEXT("WebSocketConnection"), TEXT("ServerAddress"),
                       ConfigServerAddress, GEngineIni);
    GConfig->GetInt(TEXT("WebSocketConnection"), TEXT("ServerPort"),
                    ConfigServerPort, GEngineIni);
    GConfig->GetFloat(TEXT("WebSocketConnection"), TEXT("HeartbeatInterval"),
                      HeartbeatInterval, GEngineIni);
    GConfig->GetFloat(TEXT("WebSocketConnection"), TEXT("ConnectionTimeout"),
                      ConnectionTimeout, GEngineIni);
  }

  if (ConfigServerAddress.IsEmpty()) {
    ConfigServerAddress = TEXT("127.0.0.1");
  }

  Connect(ConfigServerAddress, ConfigServerPort, InToken);
}

void URealtimeWebSocketConnection::Disconnect() {
  if (!bIsConnected && !WebSocket.IsValid()) {
    return;
  }

  UE_LOG(LogTemp, Log, TEXT("WebSocketConnection: Disconnecting"));

  if (GEngine && !GIsGarbageCollecting) {
    UWorld *World = GEngine->GetWorldFromContextObject(
        this, EGetWorldErrorMode::ReturnNull);
    if (World && IsValid(World) && !World->bIsTearingDown) {
      FTimerManager *TimerManager = &World->GetTimerManager();
      if (TimerManager) {
        TimerManager->ClearTimer(HeartbeatTimerHandle);
      }
    }
  }

  CleanupWebSocket();

  if (OnDisconnected.IsBound()) {
    OnDisconnected.Broadcast(TEXT("Disconnected"));
  }
}

void URealtimeWebSocketConnection::SendHeartbeat() {
  if (!bIsConnected || !WebSocket.IsValid()) {
    return;
  }

  FProtobufCodec::FClientMessage Message;
  Message.Token = AuthToken;
  Message.Type = FProtobufCodec::FClientMessage::EMessageType::Heartbeat;
  FDateTime Now = FDateTime::UtcNow();
  int64 UnixTimestamp = Now.ToUnixTimestamp();
  int32 Milliseconds = Now.GetMillisecond();
  Message.Heartbeat.ClientTimeMs = UnixTimestamp * 1000 + Milliseconds;

  TArray<uint8> MessageData = FProtobufCodec::EncodeClientMessage(Message);
  SendProtobufMessage(MessageData);
}

void URealtimeWebSocketConnection::SendEcho(const TArray<uint8> &Payload) {
  if (!bIsConnected || !WebSocket.IsValid()) {
    return;
  }

  SendProtobufMessage(Payload);
}

void URealtimeWebSocketConnection::SetPlayerId(const FString &InPlayerId) {
  PlayerId = InPlayerId;
}

void URealtimeWebSocketConnection::SendPlayerInput(float MoveX, float MoveY,
                                                   bool Shoot, float AimX,
                                                   float AimY) {
  if (!bIsConnected || !WebSocket.IsValid()) {
    return;
  }

  FProtobufCodec::FClientMessage Message;
  Message.Token = AuthToken;
  Message.Type = FProtobufCodec::FClientMessage::EMessageType::PlayerInput;
  Message.PlayerInput.PlayerId =
      PlayerId.IsEmpty() ? TEXT("player1") : PlayerId;

  if (Message.PlayerInput.PlayerId.Len() > 20) {
    UE_LOG(LogTemp, Error,
           TEXT("SendPlayerInput: LONG PlayerId detected! Length=%d, "
                "Value='%s' - TRACING SOURCE"),
           Message.PlayerInput.PlayerId.Len(), *Message.PlayerInput.PlayerId);
    UE_LOG(LogTemp, Error,
           TEXT("SendPlayerInput: Stored PlayerId='%s', IsEmpty=%d"), *PlayerId,
           PlayerId.IsEmpty());
  }

  Message.PlayerInput.Tick = ClientTick++;
  Message.PlayerInput.MoveX = FProtobufCodec::QuantizeCoordinate(MoveX);
  Message.PlayerInput.MoveY = FProtobufCodec::QuantizeCoordinate(MoveY);
  Message.PlayerInput.Shoot = Shoot;
  Message.PlayerInput.AimX = FProtobufCodec::QuantizeCoordinate(AimX);
  Message.PlayerInput.AimY = FProtobufCodec::QuantizeCoordinate(AimY);

  TArray<uint8> MessageData = FProtobufCodec::EncodeClientMessage(Message);
  SendProtobufMessage(MessageData);
}

bool URealtimeWebSocketConnection::InitializeWebSocket() {
  UE_LOG(LogTemp, Log,
         TEXT("WebSocketConnection: Initializing WebSockets module..."));

  if (!FModuleManager::Get().IsModuleLoaded("WebSockets")) {
    UE_LOG(LogTemp, Log,
           TEXT("WebSocketConnection: Loading WebSockets module..."));
    FModuleManager::Get().LoadModule("WebSockets");
  } else {
    UE_LOG(LogTemp, Log,
           TEXT("WebSocketConnection: WebSockets module already loaded"));
  }

  FString WebSocketURL = FString::Printf(
      TEXT("ws://%s:%d/ws?token=%s"), *ServerAddress, ServerPort, *AuthToken);
  UE_LOG(LogTemp, Log, TEXT("WebSocketConnection: Creating connection to %s"),
         *WebSocketURL);

  if (WebSocket.IsValid()) {
    UE_LOG(LogTemp, Warning,
           TEXT("WebSocketConnection: WebSocket already exists, closing it "
                "first..."));
    WebSocket->Close();
    WebSocket.Reset();
  }

  WebSocket =
      FWebSocketsModule::Get().CreateWebSocket(WebSocketURL, TEXT("ws"));
  if (!WebSocket.IsValid()) {
    UE_LOG(LogTemp, Error,
           TEXT("WebSocketConnection: Failed to create WebSocket connection"));
    return false;
  }

  UE_LOG(
      LogTemp, Log,
      TEXT("WebSocketConnection: WebSocket connection created successfully"));

  TWeakObjectPtr<URealtimeWebSocketConnection> WeakThis(this);
  WebSocket->OnConnected().AddLambda([WeakThis]() {
    if (URealtimeWebSocketConnection *StrongThis = WeakThis.Get()) {
      StrongThis->OnWebSocketConnected();
    }
  });
  WebSocket->OnConnectionError().AddLambda([WeakThis](const FString &Error) {
    if (URealtimeWebSocketConnection *StrongThis = WeakThis.Get()) {
      StrongThis->OnWebSocketDisconnected(
          FString::Printf(TEXT("Connection error: %s"), *Error));
    }
  });
  WebSocket->OnRawMessage().AddLambda(
      [WeakThis](const void *Data, SIZE_T Size, SIZE_T BytesRemaining) {
        UE_LOG(LogTemp, VeryVerbose,
               TEXT("WebSocketConnection: OnRawMessage called: Size=%d, "
                    "BytesRemaining=%d"),
               Size, BytesRemaining);
        if (URealtimeWebSocketConnection *StrongThis = WeakThis.Get()) {
          if (!IsValid(StrongThis)) {
            UE_LOG(LogTemp, VeryVerbose,
                   TEXT("WebSocketConnection: StrongThis is not valid, ignoring message"));
            return;
          }
          TArray<uint8> ReceivedData;
          ReceivedData.Append(static_cast<const uint8 *>(Data), Size);
          if (BytesRemaining == 0) {
            TWeakObjectPtr<URealtimeWebSocketConnection> WeakThisForAsync = WeakThis;
            AsyncTask(ENamedThreads::GameThread, [WeakThisForAsync, ReceivedData]() {
              if (URealtimeWebSocketConnection* StrongThis = WeakThisForAsync.Get()) {
                if (IsValid(StrongThis)) {
                  StrongThis->OnWebSocketDataReceived(ReceivedData);
                }
              }
            });
          } else {
            UE_LOG(LogTemp, VeryVerbose,
                   TEXT("WebSocketConnection: Message chunk received, waiting "
                        "for more data..."));
          }
        } else {
          UE_LOG(LogTemp, VeryVerbose,
                 TEXT("WebSocketConnection: WeakThis is invalid, ignoring "
                      "message"));
        }
      });
  WebSocket->OnClosed().AddLambda(
      [WeakThis](int32 StatusCode, const FString &Reason, bool bWasClean) {
        if (URealtimeWebSocketConnection *StrongThis = WeakThis.Get()) {
          StrongThis->OnWebSocketDisconnected(
              FString::Printf(TEXT("Connection closed: %s"), *Reason));
        }
      });

  WebSocket->Connect();

  return true;
}

void URealtimeWebSocketConnection::CleanupWebSocket() {
  bIsConnected = false;

  if (GEngine && !GIsGarbageCollecting) {
    UWorld *World = nullptr;
    if (IsValid(this)) {
      World = GEngine->GetWorldFromContextObject(
          this, EGetWorldErrorMode::ReturnNull);
    }

    if (World && IsValid(World) && !World->bIsTearingDown) {
      FTimerManager *TimerManager = &World->GetTimerManager();
      if (TimerManager) {
        TimerManager->ClearTimer(HeartbeatTimerHandle);
      }
    }
  }

  if (WebSocket.IsValid()) {
    bIsConnected = false;
    WebSocket->Close();
    WebSocket.Reset();
  }
}

void URealtimeWebSocketConnection::OnWebSocketConnected() {
  bIsConnected = true;
  ClientTick = 0;

  UE_LOG(LogTemp, Log,
         TEXT("WebSocketConnection: ===== CONNECTED to %s:%d ===== "
              "bIsConnected=%d"),
         *ServerAddress, ServerPort, bIsConnected);

  if (!AuthToken.IsEmpty()) {
    FProtobufCodec::FClientMessage AuthMessage;
    AuthMessage.Token = AuthToken;
    AuthMessage.Type = FProtobufCodec::FClientMessage::EMessageType::Heartbeat;
    TArray<uint8> AuthData = FProtobufCodec::EncodeClientMessage(AuthMessage);
    SendProtobufMessage(AuthData);
    UE_LOG(LogTemp, VeryVerbose,
           TEXT("WebSocketConnection: Auth token sent after connection"));
  }

  OnConnected.Broadcast(true);
  UE_LOG(LogTemp, VeryVerbose,
         TEXT("WebSocketConnection: OnConnected delegate broadcasted"));

  if (UWorld *World = GEngine->GetWorldFromContextObject(
          this, EGetWorldErrorMode::LogAndReturnNull)) {
    World->GetTimerManager().SetTimer(
        HeartbeatTimerHandle, this,
        &URealtimeWebSocketConnection::SendHeartbeat, 1.0f, true);
    UE_LOG(LogTemp, VeryVerbose,
           TEXT("WebSocketConnection: Heartbeat timer started"));
  }
}

void URealtimeWebSocketConnection::OnWebSocketDisconnected(
    const FString &Reason) {
  bIsConnected = false;
  const bool bNormalClose = Reason.Contains(TEXT("Successfully closed"));
  if (bNormalClose) {
    UE_LOG(LogTemp, Log,
           TEXT("WebSocketConnection: ===== DISCONNECTED: %s ===== bIsConnected=%d"),
           *Reason, bIsConnected);
  } else {
    UE_LOG(LogTemp, Warning,
           TEXT("WebSocketConnection: ===== DISCONNECTED: %s ===== bIsConnected=%d"),
           *Reason, bIsConnected);
  }

  if (GEngine && !GIsGarbageCollecting) {
    UWorld *World = GEngine->GetWorldFromContextObject(
        this, EGetWorldErrorMode::ReturnNull);
    if (World && IsValid(World) && !World->bIsTearingDown) {
      FTimerManager *TimerManager = &World->GetTimerManager();
      if (TimerManager) {
        TimerManager->ClearTimer(HeartbeatTimerHandle);
      }
    }
  }

  CleanupWebSocket();

  if (OnDisconnected.IsBound()) {
    OnDisconnected.Broadcast(Reason);
  }
}

void URealtimeWebSocketConnection::OnWebSocketDataReceived(
    const TArray<uint8> &Data) {
  UE_LOG(LogTemp, VeryVerbose,
         TEXT("WebSocketConnection: ===== DATA RECEIVED: %d bytes ===== "
              "bIsConnected=%d"),
         Data.Num(), bIsConnected);
  ProcessServerMessage(Data);
}

void URealtimeWebSocketConnection::ProcessServerMessage(
    const TArray<uint8> &Data) {
  if (Data.Num() == 0) {
    return;
  }

  TWeakObjectPtr<URealtimeWebSocketConnection> WeakThis = this;

  FProtobufCodec::FServerMessage ServerMsg;
  if (!FProtobufCodec::DecodeServerMessage(Data, ServerMsg)) {
    TArray<uint8> BroadcastData = Data;
    AsyncTask(ENamedThreads::GameThread, [WeakThis, BroadcastData]() {
      if (URealtimeWebSocketConnection* StrongThis = WeakThis.Get()) {
        if (IsValid(StrongThis) && StrongThis->OnGameStateReceived.IsBound()) {
          StrongThis->OnGameStateReceived.Broadcast(BroadcastData);
        }
      }
    });
    return;
  }

  if (ServerMsg.Type ==
      FProtobufCodec::FServerMessage::EMessageType::HeartbeatAck) {
    int64 ServerTime = ServerMsg.HeartbeatAck.ServerTimeMs;
    int64 RTT = ServerMsg.HeartbeatAck.RTTEstimateMs;
    AsyncTask(ENamedThreads::GameThread, [WeakThis, ServerTime, RTT]() {
      if (URealtimeWebSocketConnection* StrongThis = WeakThis.Get()) {
        if (IsValid(StrongThis) && StrongThis->OnHeartbeatAck.IsBound()) {
          StrongThis->OnHeartbeatAck.Broadcast(ServerTime, RTT);
        }
      }
    });
    LastRTT = static_cast<int32>(ServerMsg.HeartbeatAck.RTTEstimateMs);
  } else if (ServerMsg.Type ==
             FProtobufCodec::FServerMessage::EMessageType::GameState) {
    TArray<uint8> GameStateData =
        FProtobufCodec::EncodeServerMessage(ServerMsg);
    AsyncTask(ENamedThreads::GameThread, [WeakThis, GameStateData]() {
      if (URealtimeWebSocketConnection* StrongThis = WeakThis.Get()) {
        if (IsValid(StrongThis) && StrongThis->OnGameStateReceived.IsBound()) {
          StrongThis->OnGameStateReceived.Broadcast(GameStateData);
        }
      }
    });
  } else {
    TArray<uint8> BroadcastData = Data;
    AsyncTask(ENamedThreads::GameThread, [WeakThis, BroadcastData]() {
      if (URealtimeWebSocketConnection* StrongThis = WeakThis.Get()) {
        if (IsValid(StrongThis) && StrongThis->OnGameStateReceived.IsBound()) {
          StrongThis->OnGameStateReceived.Broadcast(BroadcastData);
        }
      }
    });
  }
}

void URealtimeWebSocketConnection::SendProtobufMessage(
    const TArray<uint8> &Data) {
  if (!WebSocket.IsValid() || !bIsConnected) {
    return;
  }

  if (Data.Num() == 0) {
    return;
  }

  WebSocket->Send(Data.GetData(), Data.Num(), true);
}
