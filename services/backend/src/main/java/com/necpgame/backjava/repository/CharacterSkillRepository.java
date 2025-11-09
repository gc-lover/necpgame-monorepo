package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CharacterSkillRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РЅР°РІС‹РєР°РјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml
 */
@Repository
public interface CharacterSkillRepository extends JpaRepository<CharacterSkillEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РІСЃРµ РЅР°РІС‹РєРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CharacterSkillEntity cs WHERE cs.characterId = :characterId ORDER BY cs.level DESC, cs.experience DESC")
    List<CharacterSkillEntity> findByCharacterIdOrderByLevelDesc(UUID characterId);

    /**
     * РќР°Р№С‚Рё РєРѕРЅРєСЂРµС‚РЅС‹Р№ РЅР°РІС‹Рє РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CharacterSkillEntity cs WHERE cs.characterId = :characterId AND cs.skillId = :skillId")
    Optional<CharacterSkillEntity> findByCharacterIdAndSkillId(UUID characterId, String skillId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ РµСЃС‚СЊ Р»Рё Сѓ РїРµСЂСЃРѕРЅР°Р¶Р° РЅР°РІС‹Рє.
     */
    @Query("SELECT COUNT(cs) > 0 FROM CharacterSkillEntity cs WHERE cs.characterId = :characterId AND cs.skillId = :skillId")
    boolean existsByCharacterIdAndSkillId(UUID characterId, String skillId);

    /**
     * РџРѕСЃС‡РёС‚Р°С‚СЊ РєРѕР»РёС‡РµСЃС‚РІРѕ РЅР°РІС‹РєРѕРІ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(cs) FROM CharacterSkillEntity cs WHERE cs.characterId = :characterId")
    long countByCharacterId(UUID characterId);
}

