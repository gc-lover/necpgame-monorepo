package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * CharacterStatsRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєР°РјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml
 */
@Repository
public interface CharacterStatsRepository extends JpaRepository<CharacterStatsEntity, UUID> {

    /**
     * РќР°Р№С‚Рё С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CharacterStatsEntity cs WHERE cs.characterId = :characterId")
    Optional<CharacterStatsEntity> findByCharacterId(UUID characterId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРє РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(cs) > 0 FROM CharacterStatsEntity cs WHERE cs.characterId = :characterId")
    boolean existsByCharacterId(UUID characterId);
}

