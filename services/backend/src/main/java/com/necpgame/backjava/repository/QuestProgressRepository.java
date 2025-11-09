package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * QuestProgressRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РїСЂРѕРіСЂРµСЃСЃРѕРј РєРІРµСЃС‚РѕРІ.
 */
@Repository
public interface QuestProgressRepository extends JpaRepository<QuestProgressEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РїСЂРѕРіСЂРµСЃСЃ РєРІРµСЃС‚Р° РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @param questId ID РєРІРµСЃС‚Р°
     * @return РїСЂРѕРіСЂРµСЃСЃ РєРІРµСЃС‚Р°
     */
    Optional<QuestProgressEntity> findByCharacterIdAndQuestId(UUID characterId, String questId);

    /**
     * РќР°Р№С‚Рё РІСЃРµ РєРІРµСЃС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return СЃРїРёСЃРѕРє РїСЂРѕРіСЂРµСЃСЃР° РєРІРµСЃС‚РѕРІ
     */
    List<QuestProgressEntity> findByCharacterId(UUID characterId);

    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅС‹Рµ РєРІРµСЃС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return СЃРїРёСЃРѕРє Р°РєС‚РёРІРЅС‹С… РєРІРµСЃС‚РѕРІ
     */
    List<QuestProgressEntity> findByCharacterIdAndStatus(UUID characterId, QuestProgressEntity.QuestStatus status);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ СЃСѓС‰РµСЃС‚РІРѕРІР°РЅРёРµ РїСЂРѕРіСЂРµСЃСЃР° РєРІРµСЃС‚Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @param questId ID РєРІРµСЃС‚Р°
     * @return true, РµСЃР»Рё РїСЂРѕРіСЂРµСЃСЃ СЃСѓС‰РµСЃС‚РІСѓРµС‚
     */
    boolean existsByCharacterIdAndQuestId(UUID characterId, String questId);

    /**
     * РќР°Р№С‚Рё Р·Р°РІРµСЂС€РµРЅРЅС‹Рµ РєРІРµСЃС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return СЃРїРёСЃРѕРє Р·Р°РІРµСЂС€РµРЅРЅС‹С… РєРІРµСЃС‚РѕРІ
     */
    default List<QuestProgressEntity> findCompletedQuestsByCharacterId(UUID characterId) {
        return findByCharacterIdAndStatus(characterId, QuestProgressEntity.QuestStatus.COMPLETED);
    }

    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅС‹Рµ РєРІРµСЃС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return СЃРїРёСЃРѕРє Р°РєС‚РёРІРЅС‹С… РєРІРµСЃС‚РѕРІ
     */
    default List<QuestProgressEntity> findActiveQuestsByCharacterId(UUID characterId) {
        return findByCharacterIdAndStatus(characterId, QuestProgressEntity.QuestStatus.ACTIVE);
    }
}

