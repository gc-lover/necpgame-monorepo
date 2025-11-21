#include "Net/EntityStateHistoryManager.h"

void UEntityStateHistoryManager::AddSnapshot(
    const FString &EntityId, const FEntityStateSnapshot &Snapshot) {
  if (EntityId.IsEmpty()) {
    return;
  }

  FScopeLock Lock(&EntityStateHistoryMutex);
  TArray<FEntityStateSnapshot> *HistoryPtr = EntityStateHistory.Find(EntityId);

  if (HistoryPtr) {
    HistoryPtr->Add(Snapshot);
    if (HistoryPtr->Num() > MaxHistorySize) {
      HistoryPtr->RemoveAt(0);
    }
  } else {
    int32 CurrentSize = EntityStateHistory.Num();
    EntityStateHistory.Reserve(CurrentSize + 1);

    TArray<FEntityStateSnapshot> NewHistory;
    NewHistory.Reserve(MaxHistorySize);
    NewHistory.Add(Snapshot);

    EntityStateHistory.Add(EntityId, MoveTemp(NewHistory));
  }
}

TArray<FEntityStateSnapshot>
UEntityStateHistoryManager::GetHistory(const FString &EntityId) const {
  FScopeLock Lock(&EntityStateHistoryMutex);
  const TArray<FEntityStateSnapshot> *HistoryPtr =
      EntityStateHistory.Find(EntityId);

  if (HistoryPtr) {
    return *HistoryPtr;
  }

  return TArray<FEntityStateSnapshot>();
}

TArray<FString> UEntityStateHistoryManager::GetAllEntityIds() const {
  FScopeLock Lock(&EntityStateHistoryMutex);
  TArray<FString> EntityIds;
  EntityStateHistory.GetKeys(EntityIds);
  return EntityIds;
}

void UEntityStateHistoryManager::CleanupOldSnapshots(const FString &EntityId,
                                                     float InterpolationTime) {
  FScopeLock Lock(&EntityStateHistoryMutex);
  TArray<FEntityStateSnapshot> *HistoryPtr = EntityStateHistory.Find(EntityId);

  if (HistoryPtr) {
    while (HistoryPtr->Num() > 1 &&
           (*HistoryPtr)[0].Timestamp < InterpolationTime - 0.5f) {
      HistoryPtr->RemoveAt(0);
    }
  }
}

void UEntityStateHistoryManager::Clear() {
  FScopeLock Lock(&EntityStateHistoryMutex);
  EntityStateHistory.Empty();
}
