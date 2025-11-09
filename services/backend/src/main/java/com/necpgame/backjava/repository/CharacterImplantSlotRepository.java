package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterImplantSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ СЃР»РѕС‚Р°РјРё РёРјРїР»Р°РЅС‚РѕРІ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Repository
public interface CharacterImplantSlotRepository extends JpaRepository<CharacterImplantSlotEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ СЃР»РѕС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cis FROM CharacterImplantSlotEntity cis WHERE cis.character.id = :characterId")
    List<CharacterImplantSlotEntity> findByCharacterId(UUID characterId);
    
    /**
     * РќР°Р№С‚Рё СЃР»РѕС‚ РїРµСЂСЃРѕРЅР°Р¶Р° РїРѕ С‚РёРїСѓ.
     */
    @Query("SELECT cis FROM CharacterImplantSlotEntity cis WHERE cis.character.id = :characterId AND cis.slotType = :slotType")
    Optional<CharacterImplantSlotEntity> findByCharacterIdAndSlotType(UUID characterId, CharacterImplantSlotEntity.SlotType slotType);
}

