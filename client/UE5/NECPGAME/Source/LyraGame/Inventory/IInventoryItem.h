#pragma once

#include "CoreMinimal.h"
#include "UObject/Interface.h"
#include "IInventoryItem.generated.h"

UINTERFACE(MinimalAPI, BlueprintType)
class UInventoryItem : public UInterface
{
	GENERATED_BODY()
};

class LYRAGAME_API IInventoryItem
{
	GENERATED_BODY()

public:
	virtual FString GetItemId() const = 0;
	virtual int32 GetStackCount() const = 0;
	virtual void SetStackCount(int32 NewCount) = 0;
	virtual bool CanStackWith(const IInventoryItem* OtherItem) const = 0;
	virtual bool IsValid() const = 0;
};

