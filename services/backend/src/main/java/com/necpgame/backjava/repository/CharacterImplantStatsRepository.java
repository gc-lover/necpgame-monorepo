package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterImplantStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ СЃС‚Р°С‚РёСЃС‚РёРєРѕР№ РёРјРїР»Р°РЅС‚РѕРІ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Repository
public interface CharacterImplantStatsRepository extends JpaRepository<CharacterImplantStatsEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё СЃС‚Р°С‚РёСЃС‚РёРєСѓ РїРѕ ID РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cis FROM CharacterImplantStatsEntity cis WHERE cis.character.id = :characterId")
    Optional<CharacterImplantStatsEntity> findByCharacterId(UUID characterId);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ СЃС‚Р°С‚РёСЃС‚РёРєРё РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(cis) > 0 FROM CharacterImplantStatsEntity cis WHERE cis.character.id = :characterId")
    boolean existsByCharacterId(UUID characterId);
}

