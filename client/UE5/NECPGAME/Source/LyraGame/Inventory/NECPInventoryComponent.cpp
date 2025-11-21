#include "NECPInventoryComponent.h"
#include "NECPInventoryItem.h"
#include "LyraInventoryItemDefinition.h"
#include "LyraInventoryItemInstance.h"
#include "HAL/CriticalSection.h"

UNECPInventoryComponent::UNECPInventoryComponent(const FObjectInitializer& ObjectInitializer)
	: Super(ObjectInitializer)
	, LyraInventoryComponent(nullptr)
{
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

	if (LyraInventoryComponent)
	{
		TArray<ULyraInventoryItemInstance*> AllLyraItems = LyraInventoryComponent->GetAllItems();
		
		FScopeLock Lock(&InventoryCriticalSection);
		
		ItemCache.Empty();
		for (ULyraInventoryItemInstance* LyraItem : AllLyraItems)
		{
			if (IsValid(LyraItem))
			{
				UNECPInventoryItem* NewItem = NewObject<UNECPInventoryItem>(this);
				NewItem->SetLyraInstance(LyraItem);
				ItemCache.Add(NewItem);
			}
		}
	}
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

	UNECPInventoryItem* NECPItem = Cast<UNECPInventoryItem>(Item);
	if (!NECPItem)
	{
		return false;
	}

	ULyraInventoryItemInstance* LyraInstance = NECPItem->GetLyraInstance();
	if (!LyraInstance)
	{
		return false;
	}

	TSubclassOf<ULyraInventoryItemDefinition> ItemDef = LyraInstance->GetItemDef();
	if (!ItemDef)
	{
		return false;
	}

	return LyraInventoryComponent->CanAddItemDefinition(ItemDef, 1);
}

bool UNECPInventoryComponent::AddItem(IInventoryItem* Item)
{
	if (!CanAddItem(Item))
	{
		return false;
	}

	FScopeLock Lock(&InventoryCriticalSection);

	UNECPInventoryItem* NECPItem = Cast<UNECPInventoryItem>(Item);
	if (!NECPItem)
	{
		return false;
	}

	ULyraInventoryItemInstance* LyraInstance = NECPItem->GetLyraInstance();
	if (!LyraInstance)
	{
		return false;
	}

	TSubclassOf<ULyraInventoryItemDefinition> ItemDef = LyraInstance->GetItemDef();
	if (!ItemDef)
	{
		return false;
	}

	ULyraInventoryItemInstance* AddedInstance = LyraInventoryComponent->AddItemDefinition(ItemDef, 1);
	if (AddedInstance)
	{
		UNECPInventoryItem* NewItem = NewObject<UNECPInventoryItem>(this);
		NewItem->SetLyraInstance(AddedInstance);
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

	UNECPInventoryItem* NECPItem = Cast<UNECPInventoryItem>(Item);
	if (!NECPItem)
	{
		return false;
	}

	ULyraInventoryItemInstance* LyraInstance = NECPItem->GetLyraInstance();
	if (!LyraInstance)
	{
		return false;
	}

	LyraInventoryComponent->RemoveItemInstance(LyraInstance);
	
	int32 RemovedIndex = ItemCache.Find(NECPItem);
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

		ULyraInventoryItemInstance* LyraInstance = Item->GetLyraInstance();
		if (LyraInstance && LyraInstance->GetItemDef() == ItemDef)
		{
			return Item;
		}
	}

	return nullptr;
}

bool UNECPInventoryComponent::AddItemDefinition(TSubclassOf<ULyraInventoryItemDefinition> ItemDef, int32 StackCount)
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

	ULyraInventoryItemInstance* AddedInstance = LyraInventoryComponent->AddItemDefinition(ItemDef, StackCount);
	if (AddedInstance)
	{
		UNECPInventoryItem* NewItem = NewObject<UNECPInventoryItem>(this);
		NewItem->SetLyraInstance(AddedInstance);
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
	if (FoundInstance)
	{
		LyraInventoryComponent->RemoveItemInstance(FoundInstance);
		
		for (int32 i = ItemCache.Num() - 1; i >= 0; --i)
		{
			if (ItemCache[i] && ItemCache[i]->GetLyraInstance() == FoundInstance)
			{
				ItemCache.RemoveAt(i);
				break;
			}
		}
		
		return true;
	}

	return false;
}

