#pragma once

#include "CoreMinimal.h"
#include "UObject/NoExportTypes.h"
#include "PlayerIdResolver.generated.h"

UCLASS()
class LYRAGAME_API UPlayerIdResolver : public UObject {
  GENERATED_BODY()

public:
  FString GetPlayerIdFromController(APlayerController *Controller) const;
  APlayerController *FindControllerByPlayerId(const FString &PlayerId,
                                              UWorld *World) const;
  TMap<FString, APlayerController *> BuildControllerMap(UWorld *World) const;
};
