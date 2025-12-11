// Issue: #196
#pragma once

#include "CoreMinimal.h"
#include "IInventoryItem.h"
#include "UObject/NoExportTypes.h"

#include "NECPInventoryItem.generated.h"

class ULyraInventoryItemInstance;
class ULyraInventoryItemDefinition;

UCLASS(BlueprintType)
class LYRAGAME_API UNECPInventoryItem : public UObject, public IInventoryItem
{
	GENERATED_BODY()

public:
	UNECPInventoryItem(const FObjectInitializer& ObjectInitializer = FObjectInitializer::Get());

	void SetLyraInstance(ULyraInventoryItemInstance* InLyraInstance);

	virtual FString GetItemId() const override;
	virtual int32 GetStackCount() const override;
	virtual void SetStackCount(int32 NewCount) override;
	virtual bool CanStackWith(const IInventoryItem* OtherItem) const override;
	virtual bool IsValid() const override;
	virtual ULyraInventoryItemInstance* GetLyraInstance() const override { return LyraInstance; }
	virtual TSubclassOf<ULyraInventoryItemDefinition> GetDefinition() const override;

private:
	UPROPERTY()
	TObjectPtr<ULyraInventoryItemInstance> LyraInstance;
};
