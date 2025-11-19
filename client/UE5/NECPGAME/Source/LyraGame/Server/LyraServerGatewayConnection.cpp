#include "LyraServerGatewayConnection.h"
#include "WebSocketsModule.h"
#include "IWebSocket.h"
#include "Modules/ModuleManager.h"
#include "TimerManager.h"
#include "Engine/World.h"
#include "Engine/Engine.h"
#include "Templates/SharedPointer.h"
#include "UObject/WeakObjectPtr.h"

ULyraServerGatewayConnection::ULyraServerGatewayConnection()
	: GatewayPort(18080)
	, bIsConnected(false)
	, HeartbeatInterval(5.0f)
{
}

ULyraServerGatewayConnection::~ULyraServerGatewayConnection()
{
	Shutdown();
}

void ULyraServerGatewayConnection::Initialize(const FString& InGatewayAddress, int32 InGatewayPort)
{
	Shutdown();

	GatewayAddress = InGatewayAddress;
	GatewayPort = InGatewayPort;

	if (!FModuleManager::Get().IsModuleLoaded("WebSockets"))
	{
		FModuleManager::Get().LoadModule("WebSockets");
	}

	FString WebSocketURL = FString::Printf(TEXT("ws://%s:%d/server"), *GatewayAddress, GatewayPort);
	UE_LOG(LogTemp, Log, TEXT("LyraServerGatewayConnection: Connecting to %s"), *WebSocketURL);

	if (WebSocket.IsValid())
	{
		WebSocket->Close();
		WebSocket.Reset();
	}

	WebSocket = FWebSocketsModule::Get().CreateWebSocket(WebSocketURL, TEXT("ws"));
	if (!WebSocket.IsValid())
	{
		UE_LOG(LogTemp, Error, TEXT("LyraServerGatewayConnection: Failed to create WebSocket connection"));
		return;
	}

	TWeakObjectPtr<ULyraServerGatewayConnection> WeakThis(this);
	WebSocket->OnConnected().AddLambda([WeakThis]() {
		if (ULyraServerGatewayConnection* StrongThis = WeakThis.Get()) {
			StrongThis->OnWebSocketConnected();
		}
	});
	WebSocket->OnConnectionError().AddLambda([WeakThis](const FString& Error) {
		if (ULyraServerGatewayConnection* StrongThis = WeakThis.Get()) {
			StrongThis->OnWebSocketConnectionError();
		}
	});
	WebSocket->OnRawMessage().AddLambda([WeakThis](const void* Data, SIZE_T Size, SIZE_T BytesRemaining) {
		if (ULyraServerGatewayConnection* StrongThis = WeakThis.Get()) {
			if (BytesRemaining == 0)
			{
				StrongThis->OnWebSocketRawMessage(const_cast<void*>(Data), Size);
			}
		}
	});
	WebSocket->OnClosed().AddLambda([WeakThis](int32 StatusCode, const FString& Reason, bool bWasClean) {
		if (ULyraServerGatewayConnection* StrongThis = WeakThis.Get()) {
			StrongThis->OnWebSocketClosed();
		}
	});

	WebSocket->Connect();
}

void ULyraServerGatewayConnection::Shutdown()
{
	if (WebSocket.IsValid())
	{
		if (bIsConnected)
		{
			WebSocket->Close();
		}
		WebSocket.Reset();
	}

	bIsConnected = false;

	if (GEngine && !GIsGarbageCollecting)
	{
		UWorld* World = GEngine->GetWorldFromContextObject(this, EGetWorldErrorMode::ReturnNull);
		if (World && IsValid(World) && !World->bIsTearingDown)
		{
			FTimerManager* TimerManager = &World->GetTimerManager();
			if (TimerManager)
			{
				TimerManager->ClearTimer(HeartbeatTimerHandle);
			}
		}
	}
}

void ULyraServerGatewayConnection::OnWebSocketConnected()
{
	bIsConnected = true;

	if (GEngine && !GIsGarbageCollecting)
	{
		UWorld* World = GEngine->GetWorldFromContextObject(this, EGetWorldErrorMode::ReturnNull);
		if (World && IsValid(World) && !World->bIsTearingDown)
		{
			FTimerManager* TimerManager = &World->GetTimerManager();
			if (TimerManager)
			{
				TimerManager->SetTimer(HeartbeatTimerHandle, this, &ULyraServerGatewayConnection::SendHeartbeat, HeartbeatInterval, true);
			}
		}
	}

	UE_LOG(LogTemp, Log, TEXT("LyraServerGatewayConnection: Connected to gateway at %s:%d"), *GatewayAddress, GatewayPort);
	OnGatewayConnected.Broadcast(true);
}

void ULyraServerGatewayConnection::OnWebSocketConnectionError()
{
	bIsConnected = false;
	UE_LOG(LogTemp, Error, TEXT("LyraServerGatewayConnection: Connection error"));
	OnGatewayDisconnected.Broadcast(TEXT("Connection error"));
}

void ULyraServerGatewayConnection::OnWebSocketClosed()
{
	bIsConnected = false;

	if (GEngine && !GIsGarbageCollecting)
	{
		UWorld* World = GEngine->GetWorldFromContextObject(this, EGetWorldErrorMode::ReturnNull);
		if (World && IsValid(World) && !World->bIsTearingDown)
		{
			FTimerManager* TimerManager = &World->GetTimerManager();
			if (TimerManager)
			{
				TimerManager->ClearTimer(HeartbeatTimerHandle);
			}
		}
	}

	UE_LOG(LogTemp, Warning, TEXT("LyraServerGatewayConnection: Connection closed"));
	OnGatewayDisconnected.Broadcast(TEXT("Connection closed"));
}

void ULyraServerGatewayConnection::OnWebSocketRawMessage(void* Data, int32 DataSize)
{
	TArray<uint8> MessageData;
	MessageData.Append(static_cast<uint8*>(Data), DataSize);
	ProcessServerMessage(MessageData);
}

void ULyraServerGatewayConnection::ProcessServerMessage(const TArray<uint8>& Data)
{
	if (Data.Num() == 0)
	{
		return;
	}

	OnPlayerInputReceived.Broadcast(Data);
}

void ULyraServerGatewayConnection::SendGameStateUpdate(const TArray<uint8>& GameStateData)
{
	if (WebSocket.IsValid() && bIsConnected)
	{
		WebSocket->Send(GameStateData.GetData(), GameStateData.Num(), true);
	}
	else
	{
		UE_LOG(LogTemp, Warning, TEXT("LyraServerGatewayConnection::SendGameStateUpdate: ===== CANNOT SEND: WebSocket=%p, bIsConnected=%d ====="), 
			WebSocket.Get(), bIsConnected);
	}
}

void ULyraServerGatewayConnection::SendPlayerStateUpdate(const FString& PlayerID, const TArray<uint8>& PlayerStateData)
{
	if (WebSocket.IsValid() && bIsConnected)
	{
		WebSocket->Send(PlayerStateData.GetData(), PlayerStateData.Num(), true);
	}
}

void ULyraServerGatewayConnection::SendHeartbeat()
{
	if (WebSocket.IsValid() && bIsConnected)
	{
		TArray<uint8> HeartbeatData;
		HeartbeatData.Add(0);
		WebSocket->Send(HeartbeatData.GetData(), HeartbeatData.Num(), true);
	}
}


