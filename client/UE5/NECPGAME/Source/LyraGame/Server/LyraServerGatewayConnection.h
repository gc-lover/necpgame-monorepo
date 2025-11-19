#pragma once

#include "CoreMinimal.h"
#include "UObject/NoExportTypes.h"
#include "LyraServerGatewayConnection.generated.h"

// Forward declaration
class IWebSocket;

UCLASS(BlueprintType)
class LYRAGAME_API ULyraServerGatewayConnection : public UObject
{
	GENERATED_BODY()

public:
	ULyraServerGatewayConnection();
	virtual ~ULyraServerGatewayConnection();

	void Initialize(const FString& GatewayAddress, int32 GatewayPort);
	void Shutdown();

	void SendGameStateUpdate(const TArray<uint8>& GameStateData);
	void SendPlayerStateUpdate(const FString& PlayerID, const TArray<uint8>& PlayerStateData);

	bool IsConnected() const { return bIsConnected && WebSocket.IsValid(); }

	DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnPlayerInputReceived, const TArray<uint8>&, InputData);
	DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnPlayerConnected, const FString&, PlayerID);
	DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnPlayerDisconnected, const FString&, PlayerID);
	DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnGatewayConnected, bool, bConnected);
	DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnGatewayDisconnected, const FString&, Reason);

	UPROPERTY(BlueprintAssignable)
	FOnPlayerInputReceived OnPlayerInputReceived;

	UPROPERTY(BlueprintAssignable)
	FOnPlayerConnected OnPlayerConnected;

	UPROPERTY(BlueprintAssignable)
	FOnPlayerDisconnected OnPlayerDisconnected;

	UPROPERTY(BlueprintAssignable)
	FOnGatewayConnected OnGatewayConnected;

	UPROPERTY(BlueprintAssignable)
	FOnGatewayDisconnected OnGatewayDisconnected;

private:
	void OnWebSocketConnected();
	void OnWebSocketConnectionError();
	void OnWebSocketClosed();
	void OnWebSocketRawMessage(void* Data, int32 DataSize);

	void ProcessServerMessage(const TArray<uint8>& Data);
	void SendHeartbeat();

	FTimerHandle HeartbeatTimerHandle;
	TSharedPtr<class IWebSocket> WebSocket;
	FString GatewayAddress;
	int32 GatewayPort;
	bool bIsConnected;
	float HeartbeatInterval;
};

