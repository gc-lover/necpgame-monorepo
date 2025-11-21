#pragma once

#include "CoreMinimal.h"
#include "UObject/NoExportTypes.h"
#include "IInventoryItem.h"
#include "LyraInventoryItemInstance.h"
#include "NECPInventoryItem.generated.h"

UCLASS(BlueprintType)
class LYRAGAME_API UNECPInventoryItem : public UObject, public IInventoryItem
{
	GENERATED_BODY()

public:
	UNECPInventoryItem(const FObjectInitializer& ObjectInitializer = FObjectInitializer::Get());

	void SetLyraInstance(ULyraInventoryItemInstance* InLyraInstance);
	ULyraInventoryItemInstance* GetLyraInstance() const { return LyraInstance; }

	virtual FString GetItemId() const override;
	virtual int32 GetStackCount() const override;
	virtual void SetStackCount(int32 NewCount) override;
	virtual bool CanStackWith(const IInventoryItem* OtherItem) const override;
	virtual bool IsValid() const override;

private:
	UPROPERTY()
	TObjectPtr<ULyraInventoryItemInstance> LyraInstance;
};

