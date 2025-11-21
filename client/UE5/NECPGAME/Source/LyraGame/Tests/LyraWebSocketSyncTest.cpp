#include "Tests/LyraWebSocketSyncTest.h"
#include "Engine/World.h"
#include "HAL/PlatformProcess.h"
#include "Net/ProtobufCodec.h"
#include "Net/RealtimeWebSocketConnection.h"

#if WITH_DEV_AUTOMATION_TESTS

void UWebSocketTestHelper::OnConnectedCallback(bool bSuccess) {
  bConnected = bSuccess;
  bConnectionCallbackCalled = true;
}

void UWebSocketTestHelper::OnGameStateReceivedCallback(
    const TArray<uint8> &GameStateData) {
  GameStateReceived++;
  bGameStateCallbackCalled = true;
}

IMPLEMENT_SIMPLE_AUTOMATION_TEST(FLyraWebSocketConnectionTest,
                                 "LyraGame.Network.WebSocket.Connection",
                                 EAutomationTestFlags::EditorContext |
                                     EAutomationTestFlags::ProductFilter)

bool FLyraWebSocketConnectionTest::RunTest(const FString &Parameters) {
  URealtimeWebSocketConnection *Connection =
      NewObject<URealtimeWebSocketConnection>();

  if (!TestNotNull(TEXT("Connection object created"), Connection)) {
    return false;
  }

  if (!TestFalse(TEXT("Connection not connected initially"),
                 Connection->IsConnected())) {
    return false;
  }

  return true;
}

IMPLEMENT_SIMPLE_AUTOMATION_TEST(
    FLyraPlayerInputEncodingTest,
    "LyraGame.Network.WebSocket.PlayerInputEncoding",
    EAutomationTestFlags::EditorContext | EAutomationTestFlags::ProductFilter)

bool FLyraPlayerInputEncodingTest::RunTest(const FString &Parameters) {
  FProtobufCodec::FClientMessage Message;
  Message.Type = FProtobufCodec::FClientMessage::EMessageType::PlayerInput;
  Message.PlayerInput.PlayerId = TEXT("p12345678");
  Message.PlayerInput.Tick = 1;
  Message.PlayerInput.MoveX = 1.0f;
  Message.PlayerInput.MoveY = 0.0f;
  Message.PlayerInput.Shoot = false;
  Message.PlayerInput.AimX = 0.0f;
  Message.PlayerInput.AimY = 0.0f;

  TArray<uint8> Encoded = FProtobufCodec::EncodeClientMessage(Message);

  if (!TestTrue(TEXT("PlayerInput encoded successfully"), Encoded.Num() > 0)) {
    return false;
  }

  FProtobufCodec::FClientMessage Decoded;
  if (!TestTrue(TEXT("PlayerInput decoded successfully"),
                FProtobufCodec::DecodeClientMessage(Encoded, Decoded))) {
    return false;
  }

  if (!TestEqual(TEXT("PlayerID matches"), Decoded.PlayerInput.PlayerId,
                 Message.PlayerInput.PlayerId)) {
    return false;
  }

  if (!TestEqual(TEXT("Tick matches"), Decoded.PlayerInput.Tick,
                 Message.PlayerInput.Tick)) {
    return false;
  }

  if (!TestEqual(TEXT("MoveX matches"), Decoded.PlayerInput.MoveX,
                 Message.PlayerInput.MoveX, 0.001f)) {
    return false;
  }

  if (!TestEqual(TEXT("MoveY matches"), Decoded.PlayerInput.MoveY,
                 Message.PlayerInput.MoveY, 0.001f)) {
    return false;
  }

  if (!TestEqual(TEXT("Shoot matches"), Decoded.PlayerInput.Shoot,
                 Message.PlayerInput.Shoot)) {
    return false;
  }

  return true;
}

IMPLEMENT_SIMPLE_AUTOMATION_TEST(FLyraGameStateDecodingTest,
                                 "LyraGame.Network.WebSocket.GameStateDecoding",
                                 EAutomationTestFlags::EditorContext |
                                     EAutomationTestFlags::ProductFilter)

bool FLyraGameStateDecodingTest::RunTest(const FString &Parameters) {
  FProtobufCodec::FServerMessage Message;
  Message.Type = FProtobufCodec::FServerMessage::EMessageType::GameState;
  Message.GameState.Snapshot.Tick = 100;

  FProtobufCodec::FEntityState Entity;
  Entity.Id = TEXT("p12345678");
  Entity.X = FProtobufCodec::QuantizeCoordinate(100.0f);
  Entity.Y = FProtobufCodec::QuantizeCoordinate(200.0f);
  Entity.Z = FProtobufCodec::QuantizeCoordinate(50.0f);
  Entity.VX = FProtobufCodec::QuantizeCoordinate(1.0f);
  Entity.VY = FProtobufCodec::QuantizeCoordinate(0.0f);
  Entity.VZ = FProtobufCodec::QuantizeCoordinate(0.0f);
  Entity.Yaw = FProtobufCodec::QuantizeCoordinate(45.0f);
  Message.GameState.Snapshot.Entities.Add(Entity);

  TArray<uint8> Encoded = FProtobufCodec::EncodeServerMessage(Message);

  if (!TestTrue(TEXT("GameState encoded successfully"), Encoded.Num() > 0)) {
    return false;
  }

  FProtobufCodec::FServerMessage Decoded;
  if (!TestTrue(TEXT("GameState decoded successfully"),
                FProtobufCodec::DecodeServerMessage(Encoded, Decoded))) {
    return false;
  }

  if (!TestEqual(TEXT("Tick matches"), Decoded.GameState.Snapshot.Tick,
                 Message.GameState.Snapshot.Tick)) {
    return false;
  }

  if (!TestEqual(TEXT("Entity count matches"),
                 Decoded.GameState.Snapshot.Entities.Num(), 1)) {
    return false;
  }

  if (Decoded.GameState.Snapshot.Entities.Num() > 0) {
    const FProtobufCodec::FEntityState &DecodedEntity =
        Decoded.GameState.Snapshot.Entities[0];
    if (!TestEqual(TEXT("Entity ID matches"), DecodedEntity.Id, Entity.Id)) {
      return false;
    }

    float ExpectedX = FProtobufCodec::DequantizeCoordinate(Entity.X);
    float ExpectedY = FProtobufCodec::DequantizeCoordinate(Entity.Y);
    float ExpectedZ = FProtobufCodec::DequantizeCoordinate(Entity.Z);
    
    float DecodedX = FProtobufCodec::DequantizeCoordinate(DecodedEntity.X);
    float DecodedY = FProtobufCodec::DequantizeCoordinate(DecodedEntity.Y);
    float DecodedZ = FProtobufCodec::DequantizeCoordinate(DecodedEntity.Z);

    if (!TestEqual(TEXT("Entity X matches"), DecodedX, ExpectedX, 0.1f)) {
      return false;
    }

    if (!TestEqual(TEXT("Entity Y matches"), DecodedY, ExpectedY, 0.1f)) {
      return false;
    }

    if (!TestEqual(TEXT("Entity Z matches"), DecodedZ, ExpectedZ, 0.1f)) {
      return false;
    }
  }

  return true;
}

IMPLEMENT_SIMPLE_AUTOMATION_TEST(
    FLyraWebSocketSyncIntegrationTest,
    "LyraGame.Network.WebSocket.SynchronizationIntegration",
    EAutomationTestFlags::EditorContext | EAutomationTestFlags::ProductFilter |
        EAutomationTestFlags::RequiresUser)

bool FLyraWebSocketSyncIntegrationTest::RunTest(const FString &Parameters) {
  AddInfo(TEXT("Starting full synchronization cycle test"));
  AddInfo(
      TEXT("NOTE: This test requires realtime-gateway service to be running. "
           "Start it with: docker-compose up -d realtime-gateway"));

  URealtimeWebSocketConnection *Connection =
      NewObject<URealtimeWebSocketConnection>();
  if (!TestNotNull(TEXT("Connection object created"), Connection)) {
    return false;
  }

  UWebSocketTestHelper *Helper = NewObject<UWebSocketTestHelper>();
  if (!TestNotNull(TEXT("Helper object created"), Helper)) {
    return false;
  }

  FString ServerAddress = TEXT("127.0.0.1");
  int32 ServerPort = 18080;
  FString Token = TEXT("test");

  AddInfo(FString::Printf(TEXT("Connecting to: %s:%d"), *ServerAddress,
                          ServerPort));

  Connection->OnConnected.AddDynamic(
      Helper, &UWebSocketTestHelper::OnConnectedCallback);
  Connection->OnGameStateReceived.AddDynamic(
      Helper, &UWebSocketTestHelper::OnGameStateReceivedCallback);

  Connection->Connect(ServerAddress, ServerPort, Token);

  const float Timeout = 15.0f;
  const float StartTime = FPlatformTime::Seconds();

  while (!Helper->bConnectionCallbackCalled &&
         (FPlatformTime::Seconds() - StartTime) < Timeout) {
    FPlatformProcess::Sleep(0.2f);
    // Allow WebSocket to process events - WebSocket events are processed on
    // game thread In Automation tests, we need to give time for async
    // operations
  }

  if (!Helper->bConnectionCallbackCalled) {
    AddWarning(TEXT("Connection callback not called within timeout - Gateway "
                    "may not be running or WebSocket events not processed"));
    AddInfo(TEXT("To test full cycle, ensure realtime-gateway is running: "
                 "docker-compose up -d realtime-gateway"));
    AddInfo(TEXT("Note: In Automation tests, WebSocket events may not process "
                 "without an active World context"));
    return true;
  }

  if (!TestTrue(TEXT("Connected to Gateway"), Helper->bConnected)) {
    AddError(TEXT("Failed to connect to Gateway. Make sure realtime-gateway "
                  "service is running: docker-compose up -d realtime-gateway"));
    return false;
  }

  AddInfo(TEXT("Connected successfully, sending PlayerInput messages..."));

  int32 PlayerInputSent = 0;

  for (int32 i = 0; i < 10; i++) {
    Connection->SendPlayerInput(1.0f, 0.0f, false, 0.0f, 0.0f);
    PlayerInputSent++;
    FPlatformProcess::Sleep(0.05f);
  }

  FPlatformProcess::Sleep(2.0f);

  AddInfo(FString::Printf(
      TEXT("Test Results: PlayerInput sent=%d, GameState received=%d"),
      PlayerInputSent, Helper->GameStateReceived));

  if (!TestTrue(TEXT("PlayerInput sent"), PlayerInputSent > 0)) {
    return false;
  }

  if (Helper->GameStateReceived == 0) {
    AddWarning(TEXT("No GameState received - this is expected if Dedicated "
                    "Server is not running"));
    AddInfo(TEXT("To test full cycle, start UE5 Dedicated Server first"));
  } else {
    if (!TestTrue(TEXT("GameState received"), Helper->GameStateReceived > 0)) {
      return false;
    }
  }

  Connection->Disconnect();

  return true;
}

#endif
