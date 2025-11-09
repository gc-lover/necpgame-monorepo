package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestObjectiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuestObjectiveRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С†РµР»СЏРјРё РєРІРµСЃС‚РѕРІ.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/quests/quests.yaml
 */
@Repository
public interface QuestObjectiveRepository extends JpaRepository<QuestObjectiveEntity, String> {

    /**
     * РќР°Р№С‚Рё РІСЃРµ С†РµР»Рё РєРІРµСЃС‚Р°.
     */
    @Query("SELECT o FROM QuestObjectiveEntity o WHERE o.questId = :questId ORDER BY o.orderIndex")
    List<QuestObjectiveEntity> findByQuestIdOrderByOrderIndex(String questId);

    /**
     * РќР°Р№С‚Рё РѕР±СЏР·Р°С‚РµР»СЊРЅС‹Рµ С†РµР»Рё РєРІРµСЃС‚Р°.
     */
    @Query("SELECT o FROM QuestObjectiveEntity o WHERE o.questId = :questId AND o.optional = false ORDER BY o.orderIndex")
    List<QuestObjectiveEntity> findRequiredByQuestId(String questId);

    /**
     * РќР°Р№С‚Рё РѕРїС†РёРѕРЅР°Р»СЊРЅС‹Рµ С†РµР»Рё РєРІРµСЃС‚Р°.
     */
    @Query("SELECT o FROM QuestObjectiveEntity o WHERE o.questId = :questId AND o.optional = true ORDER BY o.orderIndex")
    List<QuestObjectiveEntity> findOptionalByQuestId(String questId);
}

