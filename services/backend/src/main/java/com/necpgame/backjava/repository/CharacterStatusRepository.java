package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CharacterStatusRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃРѕ СЃС‚Р°С‚СѓСЃРѕРј РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml
 */
@Repository
public interface CharacterStatusRepository extends JpaRepository<CharacterStatusEntity, UUID> {

    /**
     * РќР°Р№С‚Рё СЃС‚Р°С‚СѓСЃ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CharacterStatusEntity cs WHERE cs.characterId = :characterId")
    Optional<CharacterStatusEntity> findByCharacterId(UUID characterId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ СЃС‚Р°С‚СѓСЃР° РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(cs) > 0 FROM CharacterStatusEntity cs WHERE cs.characterId = :characterId")
    boolean existsByCharacterId(UUID characterId);

    List<CharacterStatusEntity> findByCharacterIdIn(List<UUID> characterIds);
}

