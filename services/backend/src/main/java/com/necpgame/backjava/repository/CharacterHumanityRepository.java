package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterHumanityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊСЋ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Repository
public interface CharacterHumanityRepository extends JpaRepository<CharacterHumanityEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ РїРѕ ID РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT ch FROM CharacterHumanityEntity ch WHERE ch.character.id = :characterId")
    Optional<CharacterHumanityEntity> findByCharacterId(UUID characterId);
    
    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ Р·Р°РїРёСЃРё С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(ch) > 0 FROM CharacterHumanityEntity ch WHERE ch.character.id = :characterId")
    boolean existsByCharacterId(UUID characterId);
}

