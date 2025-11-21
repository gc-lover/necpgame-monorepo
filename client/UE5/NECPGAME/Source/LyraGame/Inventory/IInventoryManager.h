#pragma once

#include "CoreMinimal.h"
#include "UObject/Interface.h"
#include "IInventoryItem.h"
#include "IInventoryManager.generated.h"

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

