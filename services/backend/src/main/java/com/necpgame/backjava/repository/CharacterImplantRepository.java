package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterImplantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅС‹РјРё РёРјРїР»Р°РЅС‚Р°РјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Repository
public interface CharacterImplantRepository extends JpaRepository<CharacterImplantEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ РёРјРїР»Р°РЅС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ci FROM CharacterImplantEntity ci WHERE ci.character.id = :characterId")
    List<CharacterImplantEntity> findByCharacterId(UUID characterId);
    
    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅС‹Рµ РёРјРїР»Р°РЅС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ci FROM CharacterImplantEntity ci WHERE ci.character.id = :characterId AND ci.isActive = true")
    List<CharacterImplantEntity> findActiveByCharacterId(UUID characterId);
    
    /**
     * РџРѕРґСЃС‡РёС‚Р°С‚СЊ РєРѕР»РёС‡РµСЃС‚РІРѕ Р°РєС‚РёРІРЅС‹С… РёРјРїР»Р°РЅС‚РѕРІ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(ci) FROM CharacterImplantEntity ci WHERE ci.character.id = :characterId AND ci.isActive = true")
    Long countActiveByCharacterId(UUID characterId);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ СѓСЃС‚Р°РЅРѕРІР»РµРЅРЅРѕРіРѕ РёРјРїР»Р°РЅС‚Р° Сѓ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(ci) > 0 FROM CharacterImplantEntity ci WHERE ci.character.id = :characterId AND ci.implant.id = :implantId")
    boolean existsByCharacterIdAndImplantId(UUID characterId, String implantId);
}

