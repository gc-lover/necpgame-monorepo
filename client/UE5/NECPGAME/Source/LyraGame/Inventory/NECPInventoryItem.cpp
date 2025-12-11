// Issue: #196
#include "NECPInventoryItem.h"

#include "LyraInventoryItemDefinition.h"
#include "NativeGameplayTags.h"
#include "UObject/UObjectGlobals.h"

namespace
{
	UE_DEFINE_GAMEPLAY_TAG_STATIC(TAG_Inventory_StackCount, "Inventory.StackCount");
}

UNECPInventoryItem::UNECPInventoryItem(const FObjectInitializer& ObjectInitializer)
	: Super(ObjectInitializer)
	, LyraInstance(nullptr)
{
}

void UNECPInventoryItem::SetLyraInstance(ULyraInventoryItemInstance* InLyraInstance)
{
	LyraInstance = InLyraInstance;
}

FString UNECPInventoryItem::GetItemId() const
{
	if (!IsValid())
	{
		return FString();
	}

	const TSubclassOf<ULyraInventoryItemDefinition> ItemDef = LyraInstance->GetItemDef();
	return ItemDef ? ItemDef->GetName() : FString();
}

int32 UNECPInventoryItem::GetStackCount() const
{
	if (!IsValid())
	{
		return 0;
	}

	return LyraInstance->GetStatTagStackCount(TAG_Inventory_StackCount);
}

void UNECPInventoryItem::SetStackCount(int32 NewCount)
{
	if (!IsValid())
	{
		return;
	}

	const int32 Clamped = FMath::Max(0, NewCount);
	const int32 Current = LyraInstance->GetStatTagStackCount(TAG_Inventory_StackCount);

	if (Clamped > Current)
	{
		LyraInstance->AddStatTagStack(TAG_Inventory_StackCount, Clamped - Current);
		return;
	}

	if (Current > Clamped)
	{
		LyraInstance->RemoveStatTagStack(TAG_Inventory_StackCount, Current - Clamped);
	}
}

bool UNECPInventoryItem::CanStackWith(const IInventoryItem* OtherItem) const
{
	if (!IsValid() || !OtherItem || !OtherItem->IsValid())
	{
		return false;
	}

	const TSubclassOf<ULyraInventoryItemDefinition> ThisDef = GetDefinition();
	const TSubclassOf<ULyraInventoryItemDefinition> OtherDef = OtherItem->GetDefinition();
	return ThisDef == OtherDef && ThisDef != nullptr;
}

bool UNECPInventoryItem::IsValid() const
{
	ULyraInventoryItemInstance* Instance = LyraInstance.Get();
	return Instance != nullptr && ::IsValid(Instance);
}

TSubclassOf<ULyraInventoryItemDefinition> UNECPInventoryItem::GetDefinition() const
{
	if (!IsValid())
	{
		return nullptr;
	}

	return LyraInstance->GetItemDef();
}

