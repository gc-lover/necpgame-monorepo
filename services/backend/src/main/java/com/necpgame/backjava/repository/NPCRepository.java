package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.NPCEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * NPCRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ NPC.
 */
@Repository
public interface NPCRepository extends JpaRepository<NPCEntity, String> {

    /**
     * РќР°Р№С‚Рё NPC РїРѕ ID.
     *
     * @param id ID NPC
     * @return NPC
     */
    Optional<NPCEntity> findById(String id);

    /**
     * РќР°Р№С‚Рё РІСЃРµС… NPC РІ Р»РѕРєР°С†РёРё.
     *
     * @param locationId ID Р»РѕРєР°С†РёРё
     * @return СЃРїРёСЃРѕРє NPC
     */
    List<NPCEntity> findByLocationId(String locationId);

    /**
     * РќР°Р№С‚Рё РІСЃРµС… NPC РѕРїСЂРµРґРµР»РµРЅРЅРѕРіРѕ С‚РёРїР°.
     *
     * @param type С‚РёРї NPC
     * @return СЃРїРёСЃРѕРє NPC
     */
    List<NPCEntity> findByType(NPCEntity.NPCType type);

    /**
     * РќР°Р№С‚Рё РІСЃРµС… NPC С„СЂР°РєС†РёРё.
     *
     * @param faction С„СЂР°РєС†РёСЏ
     * @return СЃРїРёСЃРѕРє NPC
     */
    List<NPCEntity> findByFaction(String faction);

    /**
     * РќР°Р№С‚Рё РІСЃРµС… РєРІРµСЃС‚РѕРґР°С‚РµР»РµР№ РІ Р»РѕРєР°С†РёРё.
     *
     * @param locationId ID Р»РѕРєР°С†РёРё
     * @return СЃРїРёСЃРѕРє РєРІРµСЃС‚РѕРґР°С‚РµР»РµР№
     */
    default List<NPCEntity> findQuestGiversInLocation(String locationId) {
        return findByLocationId(locationId).stream()
                .filter(npc -> npc.getType() == NPCEntity.NPCType.QUEST_GIVER)
                .toList();
    }
}

