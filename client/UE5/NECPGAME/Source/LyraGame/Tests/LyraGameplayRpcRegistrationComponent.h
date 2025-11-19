// Copyright Epic Games, Inc. All Rights Reserved.

#pragma once

#include "CoreMinimal.h"
#include "LyraGameplayRpcRegistrationComponent.generated.h"

#define UE_API LYRAGAME_API

UCLASS(MinimalAPI)
class ULyraGameplayRpcRegistrationComponent : public UObject
{
	GENERATED_BODY()
protected:
#if WITH_RPC_REGISTRY
	static UE_API ULyraGameplayRpcRegistrationComponent* ObjectInstance;
#endif

public:
	static UE_API ULyraGameplayRpcRegistrationComponent* GetInstance();

#if WITH_RPC_REGISTRY
	UE_API TSharedPtr<FJsonObject> GetJsonObjectFromRequestBody(TArray<uint8> InRequestBody);

	UE_API virtual void DeregisterHttpCallbacks();

	UE_API virtual void RegisterAlwaysOnHttpCallbacks();
	
	UE_API bool HttpExecuteCheatCommand(const FHttpServerRequest& Request, const FHttpResultCallback& OnComplete);

	UE_API virtual void RegisterFrontendHttpCallbacks();

	UE_API virtual void RegisterInMatchHttpCallbacks();
	
	UE_API bool HttpFireOnceCommand(const FHttpServerRequest& Request, const FHttpResultCallback& OnComplete);

	UE_API bool HttpGetPlayerVitalsCommand(const FHttpServerRequest& Request, const FHttpResultCallback& OnComplete);
#endif
};

#undef UE_API
