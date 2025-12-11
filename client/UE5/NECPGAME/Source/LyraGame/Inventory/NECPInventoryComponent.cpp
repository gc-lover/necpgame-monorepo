// Issue: #196
#include "NECPInventoryComponent.h"

#include "LyraInventoryItemDefinition.h"
#include "LyraInventoryItemInstance.h"
#include "Misc/ScopeLock.h"
#include "NECPInventoryItem.h"

UNECPInventoryComponent::UNECPInventoryComponent(const FObjectInitializer& ObjectInitializer)
	: Super(ObjectInitializer)
	, LyraInventoryComponent(nullptr)
{
	PrimaryComponentTick.bCanEverTick = false;
}

void UNECPInventoryComponent::BeginPlay()
{
	Super::BeginPlay();
	InitializeInventory();
}

void UNECPInventoryComponent::InitializeInventory()
{
	if (!LyraInventoryComponent)
	{
		LyraInventoryComponent = GetOwner()->FindComponentByClass<ULyraInventoryManagerComponent>();
	}

	if (!LyraInventoryComponent)
	{
		return;
	}

	const TArray<ULyraInventoryItemInstance*> AllItems = LyraInventoryComponent->GetAllItems();

	FScopeLock Lock(&InventoryCriticalSection);
	ItemCache.Empty();

	for (ULyraInventoryItemInstance* LyraItem : AllItems)
	{
		if (IsValid(LyraItem))
		{
			if (UNECPInventoryItem* Wrapper = CreateWrapper(LyraItem))
			{
				ItemCache.Add(Wrapper);
			}
		}
	}
}

UNECPInventoryItem* UNECPInventoryComponent::CreateWrapper(ULyraInventoryItemInstance* LyraItem)
{
	if (!LyraItem)
	{
		return nullptr;
	}

	UNECPInventoryItem* NewItem = NewObject<UNECPInventoryItem>(this);
	NewItem->SetLyraInstance(LyraItem);
	return NewItem;
}

void UNECPInventoryComponent::SetLyraInventoryComponent(ULyraInventoryManagerComponent* InLyraInventory)
{
	FScopeLock Lock(&InventoryCriticalSection);
	LyraInventoryComponent = InLyraInventory;
	InitializeInventory();
}

bool UNECPInventoryComponent::CanAddItem(IInventoryItem* Item) const
{
	if (!Item || !Item->IsValid())
	{
		return false;
	}

	FScopeLock Lock(&InventoryCriticalSection);

	if (!LyraInventoryComponent)
	{
		return false;
	}

	ULyraInventoryItemInstance* LyraInstance = Item->GetLyraInstance();
	if (!LyraInstance)
	{
		return false;
	}

	const TSubclassOf<ULyraInventoryItemDefinition> ItemDef = Item->GetDefinition();
	const int32 StackCount = FMath::Max(1, Item->GetStackCount());
	if (!ItemDef || StackCount <= 0)
	{
		return false;
	}

	return LyraInventoryComponent->CanAddItemDefinition(ItemDef, StackCount);
}

bool UNECPInventoryComponent::AddItem(IInventoryItem* Item)
{
	if (!CanAddItem(Item))
	{
		return false;
	}

	FScopeLock Lock(&InventoryCriticalSection);

	const TSubclassOf<ULyraInventoryItemDefinition> ItemDef = Item->GetDefinition();
	const int32 StackCount = FMath::Max(1, Item->GetStackCount());
	if (!ItemDef || StackCount <= 0)
	{
		return false;
	}

	ULyraInventoryItemInstance* AddedInstance = LyraInventoryComponent->AddItemDefinition(ItemDef, StackCount);
	if (!AddedInstance)
	{
		return false;
	}

	if (UNECPInventoryItem* NewItem = CreateWrapper(AddedInstance))
	{
		ItemCache.Add(NewItem);
		return true;
	}

	return false;
}

bool UNECPInventoryComponent::RemoveItem(IInventoryItem* Item)
{
	if (!Item || !Item->IsValid())
	{
		return false;
	}

	FScopeLock Lock(&InventoryCriticalSection);

	ULyraInventoryItemInstance* LyraInstance = Item->GetLyraInstance();
	if (!LyraInstance || !LyraInventoryComponent)
	{
		return false;
	}

	LyraInventoryComponent->RemoveItemInstance(LyraInstance);

	const int32 RemovedIndex = ItemCache.IndexOfByPredicate(
		[LyraInstance](const UNECPInventoryItem* CachedItem)
		{
			return CachedItem && CachedItem->GetLyraInstance() == LyraInstance;
		});
	if (RemovedIndex != INDEX_NONE)
	{
		ItemCache.RemoveAt(RemovedIndex);
		return true;
	}

	return false;
}

int32 UNECPInventoryComponent::GetItemCount() const
{
	FScopeLock Lock(&InventoryCriticalSection);
	return ItemCache.Num();
}

TArray<IInventoryItem*> UNECPInventoryComponent::GetAllItems() const
{
	FScopeLock Lock(&InventoryCriticalSection);

	TArray<IInventoryItem*> Result;
	Result.Reserve(ItemCache.Num());

	for (UNECPInventoryItem* Item : ItemCache)
	{
		if (IsValid(Item) && Item->IsValid())
		{
			Result.Add(Item);
		}
	}

	return Result;
}

IInventoryItem* UNECPInventoryComponent::FindItemByClass(TSubclassOf<UObject> ItemClass) const
{
	FScopeLock Lock(&InventoryCriticalSection);

	TSubclassOf<ULyraInventoryItemDefinition> ItemDef = Cast<UClass>(ItemClass);
	if (!ItemDef)
	{
		return nullptr;
	}

	for (UNECPInventoryItem* Item : ItemCache)
	{
		if (!IsValid(Item) || !Item->IsValid())
		{
			continue;
		}

		if (Item->GetDefinition() == ItemDef)
		{
			return Item;
		}
	}

	return nullptr;
}

bool UNECPInventoryComponent::AddItemDefinition(TSubclassOf<ULyraInventoryItemDefinition> ItemDef, int32 StackCount)
{
	if (!ItemDef || StackCount <= 0)
	{
		return false;
	}

	FScopeLock Lock(&InventoryCriticalSection);

	if (!LyraInventoryComponent)
	{
		return false;
	}

	ULyraInventoryItemInstance* AddedInstance = LyraInventoryComponent->AddItemDefinition(ItemDef, StackCount);
	if (!AddedInstance)
	{
		return false;
	}

	if (UNECPInventoryItem* NewItem = CreateWrapper(AddedInstance))
	{
		ItemCache.Add(NewItem);
		return true;
	}

	return false;
}

bool UNECPInventoryComponent::RemoveItemByDefinition(TSubclassOf<ULyraInventoryItemDefinition> ItemDef)
{
	if (!ItemDef)
	{
		return false;
	}

	FScopeLock Lock(&InventoryCriticalSection);

	if (!LyraInventoryComponent)
	{
		return false;
	}

	ULyraInventoryItemInstance* FoundInstance = LyraInventoryComponent->FindFirstItemStackByDefinition(ItemDef);
	if (!FoundInstance)
	{
		return false;
	}

	LyraInventoryComponent->RemoveItemInstance(FoundInstance);

	for (int32 Index = ItemCache.Num() - 1; Index >= 0; --Index)
	{
		if (ItemCache[Index] && ItemCache[Index]->GetLyraInstance() == FoundInstance)
		{
			ItemCache.RemoveAt(Index);
			break;
		}
	}

	return true;
}

