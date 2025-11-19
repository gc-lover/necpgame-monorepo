#pragma once

#include "CoreMinimal.h"
#include "UObject/NoExportTypes.h"

#include "QUICConnectionConfig.generated.h"

UCLASS(Config=Game)
class LYRAGAME_API UQUICConnectionConfig : public UObject
{
	GENERATED_BODY()

public:
	UQUICConnectionConfig();

	UPROPERTY(Config, EditAnywhere, Category = "QUIC Connection")
	FString ServerAddress;

	UPROPERTY(Config, EditAnywhere, Category = "QUIC Connection")
	int32 ServerPort;

	UPROPERTY(Config, EditAnywhere, Category = "QUIC Connection")
	float HeartbeatInterval;

	UPROPERTY(Config, EditAnywhere, Category = "QUIC Connection")
	float ConnectionTimeout;
};

