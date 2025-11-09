package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterInventoryEntity;
import com.necpgame.backjava.entity.CharacterInventoryEntity.StorageType;
import com.necpgame.backjava.entity.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CharacterInventoryRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РёРЅРІРµРЅС‚Р°СЂРµРј РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/inventory/inventory.yaml
 */
@Repository
public interface CharacterInventoryRepository extends JpaRepository<CharacterInventoryEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РІСЃРµ РїСЂРµРґРјРµС‚С‹ РІ РёРЅРІРµРЅС‚Р°СЂРµ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ci FROM CharacterInventoryEntity ci WHERE ci.characterId = :characterId AND ci.storageType = :storageType ORDER BY ci.slotPosition")
    List<CharacterInventoryEntity> findByCharacterIdAndStorageTypeOrderBySlotPosition(UUID characterId, StorageType storageType);

    /**
     * РќР°Р№С‚Рё РїСЂРµРґРјРµС‚ РІ РёРЅРІРµРЅС‚Р°СЂРµ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ci FROM CharacterInventoryEntity ci WHERE ci.characterId = :characterId AND ci.itemId = :itemId AND ci.storageType = :storageType ORDER BY ci.slotPosition")
    List<CharacterInventoryEntity> findByCharacterIdAndItemIdAndStorageType(UUID characterId, String itemId, StorageType storageType);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ РµСЃС‚СЊ Р»Рё РїСЂРµРґРјРµС‚ РІ РёРЅРІРµРЅС‚Р°СЂРµ.
     */
    @Query("SELECT COUNT(ci) > 0 FROM CharacterInventoryEntity ci WHERE ci.characterId = :characterId AND ci.itemId = :itemId")
    boolean existsByCharacterIdAndItemId(UUID characterId, String itemId);

    /**
     * РџРѕСЃС‡РёС‚Р°С‚СЊ РѕР±С‰РёР№ РІРµСЃ РёРЅРІРµРЅС‚Р°СЂСЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COALESCE(SUM(ii.weight * ci.quantity), 0) FROM CharacterInventoryEntity ci " +
           "JOIN InventoryItemEntity ii ON ci.itemId = ii.id " +
           "WHERE ci.characterId = :characterId AND ci.storageType IN (:storageTypes)")
    BigDecimal calculateTotalWeight(UUID characterId, List<StorageType> storageTypes);

    /**
     * РќР°Р№С‚Рё РїСЂРµРґРјРµС‚С‹ РїРѕ РєР°С‚РµРіРѕСЂРёРё РІ РёРЅРІРµРЅС‚Р°СЂРµ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ci FROM CharacterInventoryEntity ci " +
           "JOIN InventoryItemEntity ii ON ci.itemId = ii.id " +
           "WHERE ci.characterId = :characterId AND ii.category = :category")
    List<CharacterInventoryEntity> findByCharacterIdAndCategory(UUID characterId, InventoryItemEntity.ItemCategory category);

    /**
     * РЈРґР°Р»РёС‚СЊ РїСЂРµРґРјРµС‚ РёР· РёРЅРІРµРЅС‚Р°СЂСЏ.
     */
    void deleteByCharacterIdAndItemId(UUID characterId, String itemId);

    Optional<CharacterInventoryEntity> findByIdAndCharacterId(UUID id, UUID characterId);

    Optional<CharacterInventoryEntity> findFirstByCharacterIdAndItemIdAndStorageType(UUID characterId, String itemId, StorageType storageType);
}

