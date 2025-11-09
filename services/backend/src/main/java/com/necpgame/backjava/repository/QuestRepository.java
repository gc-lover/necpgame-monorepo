package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * QuestRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РєРІРµСЃС‚Р°РјРё.
 */
@Repository
public interface QuestRepository extends JpaRepository<QuestEntity, String> {

    /**
     * РќР°Р№С‚Рё РєРІРµСЃС‚ РїРѕ ID.
     *
     * @param id ID РєРІРµСЃС‚Р°
     * @return РєРІРµСЃС‚
     */
    Optional<QuestEntity> findById(String id);

    /**
     * РќР°Р№С‚Рё РІСЃРµ РєРІРµСЃС‚С‹ РѕРїСЂРµРґРµР»РµРЅРЅРѕРіРѕ С‚РёРїР°.
     *
     * @param type С‚РёРї РєРІРµСЃС‚Р°
     * @return СЃРїРёСЃРѕРє РєРІРµСЃС‚РѕРІ
     */
    List<QuestEntity> findByType(QuestEntity.QuestType type);

    /**
     * РќР°Р№С‚Рё РІСЃРµ РєРІРµСЃС‚С‹ РѕС‚ NPC.
     *
     * @param giverNpcId ID NPC
     * @return СЃРїРёСЃРѕРє РєРІРµСЃС‚РѕРІ
     */
    List<QuestEntity> findByGiverNpcId(String giverNpcId);

    /**
     * РќР°Р№С‚Рё РІСЃРµ РєРІРµСЃС‚С‹ РґР»СЏ СѓСЂРѕРІРЅСЏ.
     *
     * @param level СѓСЂРѕРІРµРЅСЊ
     * @return СЃРїРёСЃРѕРє РєРІРµСЃС‚РѕРІ
     */
    List<QuestEntity> findByLevel(Integer level);

    /**
     * РќР°Р№С‚Рё РєРІРµСЃС‚С‹ РїРѕРґС…РѕРґСЏС‰РёРµ РґР»СЏ СѓСЂРѕРІРЅСЏ.
     *
     * @param level СѓСЂРѕРІРµРЅСЊ РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return СЃРїРёСЃРѕРє РєРІРµСЃС‚РѕРІ
     */
    List<QuestEntity> findByLevelLessThanEqual(Integer level);

    /**
     * РќР°Р№С‚Рё РїРµСЂРІС‹Р№ РєРІРµСЃС‚ РґР»СЏ РЅРѕРІС‹С… РёРіСЂРѕРєРѕРІ.
     *
     * @return РїРµСЂРІС‹Р№ РєРІРµСЃС‚
     */
    default Optional<QuestEntity> findFirstQuest() {
        return findById("quest-delivery-001");
    }
}

