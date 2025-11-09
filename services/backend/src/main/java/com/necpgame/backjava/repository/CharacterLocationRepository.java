package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * CharacterLocationRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ Р»РѕРєР°С†РёСЏРјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/locations/locations.yaml
 */
@Repository
public interface CharacterLocationRepository extends JpaRepository<CharacterLocationEntity, UUID> {

    /**
     * РќР°Р№С‚Рё С‚РµРєСѓС‰СѓСЋ Р»РѕРєР°С†РёСЋ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cl FROM CharacterLocationEntity cl WHERE cl.characterId = :characterId")
    Optional<CharacterLocationEntity> findByCharacterId(UUID characterId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ Р·Р°РїРёСЃРё Рѕ Р»РѕРєР°С†РёРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(cl) > 0 FROM CharacterLocationEntity cl WHERE cl.characterId = :characterId")
    boolean existsByCharacterId(UUID characterId);
}

