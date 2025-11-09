package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.NPCDialogueOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РѕРїС†РёСЏРјРё РґРёР°Р»РѕРіРѕРІ NPC.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Repository
public interface NPCDialogueOptionRepository extends JpaRepository<NPCDialogueOptionEntity, String> {
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ РѕРїС†РёРё РґРёР°Р»РѕРіР°.
     */
    @Query("SELECT o FROM NPCDialogueOptionEntity o WHERE o.dialogue.id = :dialogueId")
    List<NPCDialogueOptionEntity> findByDialogueId(String dialogueId);
}

