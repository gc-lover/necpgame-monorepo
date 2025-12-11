// Issue: #196
#pragma once

#include "CoreMinimal.h"
#include "Components/ActorComponent.h"
#include "HAL/CriticalSection.h"
#include "IInventoryItem.h"
#include "IInventoryManager.h"
#include "LyraInventoryManagerComponent.h"

#include "NECPInventoryComponent.generated.h"

class UNECPInventoryItem;

UCLASS(ClassGroup=(Custom), meta=(BlueprintSpawnableComponent))
class LYRAGAME_API UNECPInventoryComponent : public UActorComponent, public IInventoryManager
{
	GENERATED_BODY()

public:
	UNECPInventoryComponent(const FObjectInitializer& ObjectInitializer = FObjectInitializer::Get());

	virtual void BeginPlay() override;

	UFUNCTION(BlueprintCallable, Category="Inventory")
	void SetLyraInventoryComponent(ULyraInventoryManagerComponent* InLyraInventory);

	virtual bool CanAddItem(IInventoryItem* Item) const override;
	virtual bool AddItem(IInventoryItem* Item) override;
	virtual bool RemoveItem(IInventoryItem* Item) override;
	virtual int32 GetItemCount() const override;
	virtual TArray<IInventoryItem*> GetAllItems() const override;
	virtual IInventoryItem* FindItemByClass(TSubclassOf<UObject> ItemClass) const override;

	UFUNCTION(BlueprintCallable, Category="Inventory")
	bool AddItemDefinition(TSubclassOf<ULyraInventoryItemDefinition> ItemDef, int32 StackCount = 1);

	UFUNCTION(BlueprintCallable, Category="Inventory")
	bool RemoveItemByDefinition(TSubclassOf<ULyraInventoryItemDefinition> ItemDef);

private:
	void InitializeInventory();
	UNECPInventoryItem* CreateWrapper(ULyraInventoryItemInstance* LyraItem);

	UPROPERTY()
	TObjectPtr<ULyraInventoryManagerComponent> LyraInventoryComponent;

	UPROPERTY()
	TArray<TObjectPtr<UNECPInventoryItem>> ItemCache;

	mutable FCriticalSection InventoryCriticalSection;
};