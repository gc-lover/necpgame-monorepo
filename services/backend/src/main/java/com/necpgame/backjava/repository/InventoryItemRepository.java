package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * InventoryItemRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїСЂРµРґРјРµС‚Р°РјРё.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/inventory/inventory.yaml
 */
@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, String> {

    /**
     * РќР°Р№С‚Рё РїСЂРµРґРјРµС‚С‹ РїРѕ РєР°С‚РµРіРѕСЂРёРё.
     */
    @Query("SELECT i FROM InventoryItemEntity i WHERE i.category = :category")
    List<InventoryItemEntity> findByCategory(InventoryItemEntity.ItemCategory category);

    /**
     * РќР°Р№С‚Рё СЌРєРёРїРёСЂСѓРµРјС‹Рµ РїСЂРµРґРјРµС‚С‹ РїРѕ С‚РёРїСѓ СЃР»РѕС‚Р°.
     */
    @Query("SELECT i FROM InventoryItemEntity i WHERE i.equippable = true AND i.slotType = :slotType")
    List<InventoryItemEntity> findEquippableBySlotType(String slotType);

    /**
     * РќР°Р№С‚Рё РёСЃРїРѕР»СЊР·СѓРµРјС‹Рµ РїСЂРµРґРјРµС‚С‹.
     */
    @Query("SELECT i FROM InventoryItemEntity i WHERE i.usable = true")
    List<InventoryItemEntity> findUsableItems();

    /**
     * РќР°Р№С‚Рё РєРІРµСЃС‚РѕРІС‹Рµ РїСЂРµРґРјРµС‚С‹.
     */
    @Query("SELECT i FROM InventoryItemEntity i WHERE i.questItem = true")
    List<InventoryItemEntity> findQuestItems();

    /**
     * РќР°Р№С‚Рё РїСЂРµРґРјРµС‚С‹ РїРѕ СЂРµРґРєРѕСЃС‚Рё.
     */
    @Query("SELECT i FROM InventoryItemEntity i WHERE i.rarity = :rarity")
    List<InventoryItemEntity> findByRarity(InventoryItemEntity.ItemRarity rarity);
}

