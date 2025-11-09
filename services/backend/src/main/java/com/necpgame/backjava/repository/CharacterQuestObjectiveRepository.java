package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterQuestObjectiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CharacterQuestObjectiveRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїСЂРѕРіСЂРµСЃСЃРѕРј С†РµР»РµР№ РєРІРµСЃС‚РѕРІ РїРµСЂСЃРѕРЅР°Р¶РµР№.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/quests/quests.yaml
 */
@Repository
public interface CharacterQuestObjectiveRepository extends JpaRepository<CharacterQuestObjectiveEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РїСЂРѕРіСЂРµСЃСЃ РІСЃРµС… С†РµР»РµР№ РєРІРµСЃС‚Р° РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cqo FROM CharacterQuestObjectiveEntity cqo WHERE cqo.characterId = :characterId AND cqo.questId = :questId")
    List<CharacterQuestObjectiveEntity> findByCharacterIdAndQuestId(UUID characterId, String questId);

    /**
     * РќР°Р№С‚Рё РїСЂРѕРіСЂРµСЃСЃ РєРѕРЅРєСЂРµС‚РЅРѕР№ С†РµР»Рё РєРІРµСЃС‚Р° РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cqo FROM CharacterQuestObjectiveEntity cqo WHERE cqo.characterId = :characterId AND cqo.objectiveId = :objectiveId")
    Optional<CharacterQuestObjectiveEntity> findByCharacterIdAndObjectiveId(UUID characterId, String objectiveId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ РІС‹РїРѕР»РЅРµРЅР° Р»Рё С†РµР»СЊ РєРІРµСЃС‚Р°.
     */
    @Query("SELECT COUNT(cqo) > 0 FROM CharacterQuestObjectiveEntity cqo WHERE cqo.characterId = :characterId AND cqo.objectiveId = :objectiveId AND cqo.completed = true")
    boolean isObjectiveCompleted(UUID characterId, String objectiveId);

    /**
     * РџРѕСЃС‡РёС‚Р°С‚СЊ РІС‹РїРѕР»РЅРµРЅРЅС‹Рµ С†РµР»Рё РєРІРµСЃС‚Р°.
     */
    @Query("SELECT COUNT(cqo) FROM CharacterQuestObjectiveEntity cqo WHERE cqo.characterId = :characterId AND cqo.questId = :questId AND cqo.completed = true")
    long countCompletedObjectivesByQuest(UUID characterId, String questId);
}

