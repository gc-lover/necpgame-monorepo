#pragma once

#include "CoreMinimal.h"
#include "HAL/CriticalSection.h"
#include "UObject/NoExportTypes.h"
#include "EntityStateHistoryManager.generated.h"

struct FEntityStateSnapshot {
  FVector Location;
  FRotator Rotation;
  FVector Velocity;
  float Timestamp;
  int64 Tick;
};

UCLASS()
class LYRAGAME_API UEntityStateHistoryManager : public UObject {
  GENERATED_BODY()

public:
  static constexpr int32 MaxHistorySize = 8;

  void AddSnapshot(const FString &EntityId,
                   const FEntityStateSnapshot &Snapshot);
  TArray<FEntityStateSnapshot> GetHistory(const FString &EntityId) const;
  TArray<FString> GetAllEntityIds() const;
  void CleanupOldSnapshots(const FString &EntityId, float InterpolationTime);
  void Clear();

private:
  TMap<FString, TArray<FEntityStateSnapshot>> EntityStateHistory;
  mutable FCriticalSection EntityStateHistoryMutex;
};
