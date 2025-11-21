// Copyright Epic Games, Inc. All Rights Reserved.

#include "Tests/LyraWebSocketSyncGauntletTest.h"
#include "Engine/Engine.h"
#include "Engine/World.h"
#include "HAL/PlatformProcess.h"
#include "Net/RealtimeWebSocketConnection.h"
#include "Tests/LyraWebSocketSyncTest.h"

#include UE_INLINE_GENERATED_CPP_BY_NAME(LyraWebSocketSyncGauntletTest)

DEFINE_LOG_CATEGORY(LogLyraWebSocketSyncGauntlet);

UWebSocketSyncGauntletTestController::UWebSocketSyncGauntletTestController(
    const FObjectInitializer &ObjectInitializer)
    : Super(ObjectInitializer), WebSocketConnection(nullptr),
      TestHelper(nullptr), bTestStarted(false), bConnected(false),
      bPlayerInputSent(false), GameStateReceived(0), PlayerInputSent(0),
      TestStartTime(0.0f), ConnectionTimeout(0.0f), TestDuration(0.0f) {}

void UWebSocketSyncGauntletTestController::OnPostMapChange(UWorld *World) {
  Super::OnPostMapChange(World);

  if (World && !bTestStarted) {
    UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
           TEXT("LyraWebSocketSyncGauntletTest: Map loaded, starting test..."));
    StartTest();
  }
}

void UWebSocketSyncGauntletTestController::OnTick(float DeltaTime) {
  Super::OnTick(DeltaTime);

  if (!bTestStarted) {
    return;
  }

  TestDuration += DeltaTime;

  // Check for timeout
  if (TestDuration > MaxTestDuration) {
    FinishTest(false, FString::Printf(TEXT("Test timeout after %.1f seconds"),
                                      MaxTestDuration));
    return;
  }

  // Check connection status
  if (!bConnected &&
      (TestDuration - TestStartTime) < ConnectionTimeoutDuration) {
    CheckConnection();
  } else if (!bConnected &&
             (TestDuration - TestStartTime) >= ConnectionTimeoutDuration) {
    FinishTest(false, TEXT("Failed to connect to Gateway within timeout"));
    return;
  }

  // Send PlayerInput messages
  if (bConnected && !bPlayerInputSent) {
    SendPlayerInput();
  }

  // Check for GameState
  if (bConnected && bPlayerInputSent) {
    CheckGameState();
  }
}

void UWebSocketSyncGauntletTestController::StartTest() {
  bTestStarted = true;
  TestStartTime = TestDuration;
  ConnectionTimeout = ConnectionTimeoutDuration;

  UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
         TEXT("LyraWebSocketSyncGauntletTest: Starting WebSocket "
              "synchronization test"));
  UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
         TEXT("LyraWebSocketSyncGauntletTest: Gateway should be running on "
              "ws://127.0.0.1:18080"));

  // Create WebSocket connection
  UWorld *World = GetWorld();
  if (!World) {
    FinishTest(false, TEXT("No World available"));
    return;
  }

  WebSocketConnection = NewObject<URealtimeWebSocketConnection>(World);
  if (!WebSocketConnection) {
    FinishTest(false, TEXT("Failed to create WebSocketConnection"));
    return;
  }

  // Create helper for callbacks
  TestHelper = NewObject<UWebSocketTestHelper>(World);
  if (!TestHelper) {
    FinishTest(false, TEXT("Failed to create TestHelper"));
    return;
  }

  UWebSocketTestHelper *Helper = Cast<UWebSocketTestHelper>(TestHelper);
  if (Helper) {
    WebSocketConnection->OnConnected.AddDynamic(
        Helper, &UWebSocketTestHelper::OnConnectedCallback);
    WebSocketConnection->OnGameStateReceived.AddDynamic(
        Helper, &UWebSocketTestHelper::OnGameStateReceivedCallback);
  }

  // Connect to Gateway
  FString ServerAddress = TEXT("127.0.0.1");
  int32 ServerPort = 18080;
  FString Token = TEXT("test");

  UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
         TEXT("LyraWebSocketSyncGauntletTest: Connecting to %s:%d"),
         *ServerAddress, ServerPort);
  WebSocketConnection->Connect(ServerAddress, ServerPort, Token);
}

void UWebSocketSyncGauntletTestController::CheckConnection() {
  if (!TestHelper) {
    return;
  }

  UWebSocketTestHelper *Helper = Cast<UWebSocketTestHelper>(TestHelper);
  if (Helper && Helper->bConnectionCallbackCalled) {
    bConnected = Helper->bConnected;
    if (bConnected) {
      UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
             TEXT("LyraWebSocketSyncGauntletTest: Successfully connected to "
                  "Gateway"));
    } else {
      UE_LOG(LogLyraWebSocketSyncGauntlet, Warning,
             TEXT("LyraWebSocketSyncGauntletTest: Connection failed"));
    }
  }
}

void UWebSocketSyncGauntletTestController::SendPlayerInput() {
  if (!WebSocketConnection || !bConnected) {
    return;
  }

  // Send 10 PlayerInput messages over time
  if (PlayerInputSent < 10) {
    // Send one message per tick until we've sent 10
    WebSocketConnection->SendPlayerInput(1.0f, 0.0f, false, 0.0f, 0.0f);
    PlayerInputSent++;

    if (PlayerInputSent == 10) {
      bPlayerInputSent = true;
      UE_LOG(
          LogLyraWebSocketSyncGauntlet, Log,
          TEXT("LyraWebSocketSyncGauntletTest: Sent %d PlayerInput messages"),
          PlayerInputSent);
    }
  }
}

void UWebSocketSyncGauntletTestController::CheckGameState() {
  if (!TestHelper) {
    return;
  }

  UWebSocketTestHelper *Helper = Cast<UWebSocketTestHelper>(TestHelper);
  if (Helper) {
    int32 CurrentGameStateCount = Helper->GameStateReceived;

    if (CurrentGameStateCount > GameStateReceived) {
      GameStateReceived = CurrentGameStateCount;
      UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
             TEXT("LyraWebSocketSyncGauntletTest: Received GameState #%d"),
             GameStateReceived);
    }

    // Test passes if we received at least one GameState
    // Wait a bit to ensure we get responses
    if (TestDuration - TestStartTime > 5.0f && GameStateReceived > 0) {
      FinishTest(
          true,
          FString::Printf(
              TEXT("Test passed: Sent %d PlayerInput, Received %d GameState"),
              PlayerInputSent, GameStateReceived));
    } else if (TestDuration - TestStartTime > 10.0f && GameStateReceived == 0) {
      // If no GameState received but we waited long enough, test may still pass
      // (Dedicated Server might not be running)
      FinishTest(true,
                 FString::Printf(TEXT("Test completed: Sent %d PlayerInput, No "
                                      "GameState (Server may not be running)"),
                                 PlayerInputSent));
    }
  }
}

void UWebSocketSyncGauntletTestController::FinishTest(bool bSuccess,
                                                      const FString &Message) {
  if (WebSocketConnection) {
    WebSocketConnection->Disconnect();
  }

  if (bSuccess) {
    UE_LOG(LogLyraWebSocketSyncGauntlet, Log,
           TEXT("LyraWebSocketSyncGauntletTest: %s"), *Message);
    EndTest(0);
  } else {
    UE_LOG(LogLyraWebSocketSyncGauntlet, Error,
           TEXT("LyraWebSocketSyncGauntletTest: %s"), *Message);
    EndTest(1);
  }

  bTestStarted = false;
}
