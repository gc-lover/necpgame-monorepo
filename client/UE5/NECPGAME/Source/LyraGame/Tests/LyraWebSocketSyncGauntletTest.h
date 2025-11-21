// Copyright Epic Games, Inc. All Rights Reserved.

#pragma once

#include "GauntletTestController.h"
#include "Net/RealtimeWebSocketConnection.h"
#include "Logging/LogMacros.h"

#include "LyraWebSocketSyncGauntletTest.generated.h"

DECLARE_LOG_CATEGORY_EXTERN(LogLyraWebSocketSyncGauntlet, Log, All);

UCLASS()
class UWebSocketSyncGauntletTestController : public UGauntletTestController
{
	GENERATED_BODY()

public:
	UWebSocketSyncGauntletTestController(const FObjectInitializer& ObjectInitializer);

	//~UGauntletTestController interface
	virtual void OnTick(float DeltaTime) override;
	virtual void OnPostMapChange(UWorld* World) override;
	//~End of UGauntletTestController interface

protected:
	void StartTest();
	void CheckConnection();
	void SendPlayerInput();
	void CheckGameState();
	void FinishTest(bool bSuccess, const FString& Message);

private:
	UPROPERTY()
	TObjectPtr<URealtimeWebSocketConnection> WebSocketConnection;

	UPROPERTY()
	TObjectPtr<UObject> TestHelper;

	bool bTestStarted;
	bool bConnected;
	bool bPlayerInputSent;
	int32 GameStateReceived;
	int32 PlayerInputSent;
	
	float TestStartTime;
	float ConnectionTimeout;
	float TestDuration;
	
	static constexpr float MaxTestDuration = 30.0f;
	static constexpr float ConnectionTimeoutDuration = 10.0f;
};

