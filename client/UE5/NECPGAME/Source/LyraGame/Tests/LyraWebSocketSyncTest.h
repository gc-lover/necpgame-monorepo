#pragma once

#include "CoreMinimal.h"
#include "Misc/AutomationTest.h"
#include "UObject/NoExportTypes.h"

#include "LyraWebSocketSyncTest.generated.h"

UCLASS()
class UWebSocketTestHelper : public UObject
{
	GENERATED_BODY()

public:
	bool bConnected = false;
	bool bConnectionCallbackCalled = false;
	int32 GameStateReceived = 0;
	bool bGameStateCallbackCalled = false;

	UFUNCTION()
	void OnConnectedCallback(bool bSuccess);

	UFUNCTION()
	void OnGameStateReceivedCallback(const TArray<uint8>& GameStateData);
};
