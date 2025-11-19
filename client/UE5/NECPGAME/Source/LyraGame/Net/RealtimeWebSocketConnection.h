#pragma once

#include "CoreMinimal.h"
#include "UObject/NoExportTypes.h"
#include "Net/ProtobufCodec.h"

#include "RealtimeWebSocketConnection.generated.h"

// Forward declaration
class IWebSocket;

DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnConnectedDelegate, bool, bSuccess);
DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnDisconnectedDelegate, const FString&, Reason);
DECLARE_DYNAMIC_MULTICAST_DELEGATE_TwoParams(FOnHeartbeatAckDelegate, int64, ServerTimeMs, int64, RTTMs);
DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnEchoAckDelegate, const TArray<uint8>&, Payload);
DECLARE_DYNAMIC_MULTICAST_DELEGATE_OneParam(FOnGameStateReceivedDelegate, const TArray<uint8>&, GameStateData);

UCLASS(BlueprintType, Blueprintable)
class LYRAGAME_API URealtimeWebSocketConnection : public UObject
{
	GENERATED_BODY()

public:
	URealtimeWebSocketConnection();
	virtual ~URealtimeWebSocketConnection();
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void Connect(const FString& ServerAddress, int32 ServerPort, const FString& Token);
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void ConnectWithConfig(const FString& Token);
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void Disconnect();
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void SendHeartbeat();
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void SendEcho(const TArray<uint8>& Payload);
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void SendPlayerInput(float MoveX, float MoveY, bool Shoot, float AimX, float AimY);
	
	UFUNCTION(BlueprintPure, Category = "WebSocket")
	bool IsConnected() const { return bIsConnected; }
	
	UFUNCTION(BlueprintPure, Category = "WebSocket")
	int32 GetLastRTT() const { return LastRTT; }
	
	UFUNCTION(BlueprintCallable, Category = "WebSocket")
	void SetPlayerId(const FString& InPlayerId);

	UPROPERTY(BlueprintAssignable, Category = "WebSocket")
	FOnConnectedDelegate OnConnected;
	
	UPROPERTY(BlueprintAssignable, Category = "WebSocket")
	FOnDisconnectedDelegate OnDisconnected;
	
	UPROPERTY(BlueprintAssignable, Category = "WebSocket")
	FOnHeartbeatAckDelegate OnHeartbeatAck;
	
	UPROPERTY(BlueprintAssignable, Category = "WebSocket")
	FOnEchoAckDelegate OnEchoAck;
	
	UPROPERTY(BlueprintAssignable, Category = "WebSocket")
	FOnGameStateReceivedDelegate OnGameStateReceived;

protected:
	virtual void BeginDestroy() override;

private:
	bool InitializeWebSocket();
	void CleanupWebSocket();
	
	void OnWebSocketConnected();
	void OnWebSocketDisconnected(const FString& Reason);
	void OnWebSocketDataReceived(const TArray<uint8>& Data);
	
	void ProcessServerMessage(const TArray<uint8>& Data);
	void SendProtobufMessage(const TArray<uint8>& Data);
	
	FString ServerAddress;
	int32 ServerPort;
	FString AuthToken;
	FString PlayerId;
	
	bool bIsConnected;
	int32 LastRTT;
	int64 ClientTick;
	
	TSharedPtr<class IWebSocket> WebSocket;
	FTimerHandle HeartbeatTimerHandle;
};

