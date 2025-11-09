package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterEquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CharacterEquipmentRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ СЌРєРёРїРёСЂРѕРІРєРѕР№ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/inventory/inventory.yaml
 */
@Repository
public interface CharacterEquipmentRepository extends JpaRepository<CharacterEquipmentEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РІСЃСЋ СЌРєРёРїРёСЂРѕРІРєСѓ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ce FROM CharacterEquipmentEntity ce WHERE ce.characterId = :characterId")
    List<CharacterEquipmentEntity> findByCharacterId(UUID characterId);

    /**
     * РќР°Р№С‚Рё РїСЂРµРґРјРµС‚ РІ СЃР»РѕС‚Рµ СЌРєРёРїРёСЂРѕРІРєРё.
     */
    @Query("SELECT ce FROM CharacterEquipmentEntity ce WHERE ce.characterId = :characterId AND ce.slotType = :slotType")
    Optional<CharacterEquipmentEntity> findByCharacterIdAndSlotType(UUID characterId, CharacterEquipmentEntity.SlotType slotType);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ Р·Р°РЅСЏС‚ Р»Рё СЃР»РѕС‚.
     */
    @Query("SELECT COUNT(ce) > 0 FROM CharacterEquipmentEntity ce WHERE ce.characterId = :characterId AND ce.slotType = :slotType AND ce.itemId IS NOT NULL")
    boolean isSlotOccupied(UUID characterId, CharacterEquipmentEntity.SlotType slotType);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЌРєРёРїРёСЂРѕРІР°РЅ Р»Рё РїСЂРµРґРјРµС‚.
     */
    @Query("SELECT COUNT(ce) > 0 FROM CharacterEquipmentEntity ce WHERE ce.characterId = :characterId AND ce.itemId = :itemId")
    boolean isItemEquipped(UUID characterId, String itemId);

    /**
     * РќР°Р№С‚Рё СЃР»РѕС‚ РІ РєРѕС‚РѕСЂРѕРј СЌРєРёРїРёСЂРѕРІР°РЅ РїСЂРµРґРјРµС‚.
     */
    @Query("SELECT ce FROM CharacterEquipmentEntity ce WHERE ce.characterId = :characterId AND ce.itemId = :itemId")
    Optional<CharacterEquipmentEntity> findByCharacterIdAndItemId(UUID characterId, String itemId);

    @Query("SELECT ce FROM CharacterEquipmentEntity ce WHERE ce.characterId = :characterId AND ce.inventoryItemId = :inventoryItemId")
    Optional<CharacterEquipmentEntity> findByCharacterIdAndInventoryItemId(UUID characterId, UUID inventoryItemId);
}

