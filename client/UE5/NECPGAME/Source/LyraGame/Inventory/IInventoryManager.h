// Issue: #196
#pragma once

#include "CoreMinimal.h"
#include "UObject/Interface.h"

#include "IInventoryManager.generated.h"

class IInventoryItem;

UINTERFACE(MinimalAPI, BlueprintType)
class UInventoryManager : public UInterface
{
	GENERATED_BODY()
};

class LYRAGAME_API IInventoryManager
{
	GENERATED_BODY()

public:
	virtual bool CanAddItem(IInventoryItem* Item) const = 0;
	virtual bool AddItem(IInventoryItem* Item) = 0;
	virtual bool RemoveItem(IInventoryItem* Item) = 0;
	virtual int32 GetItemCount() const = 0;
	virtual TArray<IInventoryItem*> GetAllItems() const = 0;
	virtual IInventoryItem* FindItemByClass(TSubclassOf<UObject> ItemClass) const = 0;
};


