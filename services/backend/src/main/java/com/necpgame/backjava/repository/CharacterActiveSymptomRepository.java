package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterActiveSymptomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ Р°РєС‚РёРІРЅС‹РјРё СЃРёРјРїС‚РѕРјР°РјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Repository
public interface CharacterActiveSymptomRepository extends JpaRepository<CharacterActiveSymptomEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ СЃРёРјРїС‚РѕРјС‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cas FROM CharacterActiveSymptomEntity cas WHERE cas.character.id = :characterId")
    List<CharacterActiveSymptomEntity> findByCharacterId(UUID characterId);
    
    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅС‹Рµ СЃРёРјРїС‚РѕРјС‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cas FROM CharacterActiveSymptomEntity cas WHERE cas.character.id = :characterId AND cas.isActive = true")
    List<CharacterActiveSymptomEntity> findActiveByCharacterId(UUID characterId);
    
    /**
     * РџРѕРґСЃС‡РёС‚Р°С‚СЊ РєРѕР»РёС‡РµСЃС‚РІРѕ Р°РєС‚РёРІРЅС‹С… СЃРёРјРїС‚РѕРјРѕРІ.
     */
    @Query("SELECT COUNT(cas) FROM CharacterActiveSymptomEntity cas WHERE cas.character.id = :characterId AND cas.isActive = true")
    Long countActiveByCharacterId(UUID characterId);
}

