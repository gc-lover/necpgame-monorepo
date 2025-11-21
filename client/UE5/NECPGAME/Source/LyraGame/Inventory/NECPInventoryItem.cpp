#include "NECPInventoryItem.h"
#include "LyraInventoryItemDefinition.h"
#include "UObject/UObjectGlobals.h"

UNECPInventoryItem::UNECPInventoryItem(
    const FObjectInitializer &ObjectInitializer)
    : Super(ObjectInitializer), LyraInstance(nullptr) {}

void UNECPInventoryItem::SetLyraInstance(
    ULyraInventoryItemInstance *InLyraInstance) {
  LyraInstance = InLyraInstance;
}

FString UNECPInventoryItem::GetItemId() const {
  if (!IsValid()) {
    return FString();
  }

  TSubclassOf<ULyraInventoryItemDefinition> ItemDef =
      LyraInstance->GetItemDef();
  if (ItemDef) {
    return ItemDef->GetName();
  }

  return FString();
}

int32 UNECPInventoryItem::GetStackCount() const {
  if (!IsValid()) {
    return 0;
  }

  return 1;
}

void UNECPInventoryItem::SetStackCount(int32 NewCount) {}

bool UNECPInventoryItem::CanStackWith(const IInventoryItem *OtherItem) const {
  if (!IsValid() || !OtherItem || !OtherItem->IsValid()) {
    return false;
  }

  const UNECPInventoryItem *OtherNECPItem = Cast<UNECPInventoryItem>(OtherItem);
  if (!OtherNECPItem) {
    return false;
  }

  TSubclassOf<ULyraInventoryItemDefinition> ThisItemDef =
      LyraInstance->GetItemDef();
  TSubclassOf<ULyraInventoryItemDefinition> OtherItemDef =
      OtherNECPItem->GetLyraInstance()->GetItemDef();

  return ThisItemDef == OtherItemDef;
}

bool UNECPInventoryItem::IsValid() const {
  ULyraInventoryItemInstance *Instance = LyraInstance.Get();
  return Instance != nullptr && ::IsValid(Instance);
}
