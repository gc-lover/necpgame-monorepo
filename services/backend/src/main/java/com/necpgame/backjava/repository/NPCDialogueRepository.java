package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.NPCDialogueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РґРёР°Р»РѕРіР°РјРё NPC.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Repository
public interface NPCDialogueRepository extends JpaRepository<NPCDialogueEntity, String> {
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ РґРёР°Р»РѕРіРё NPC.
     */
    @Query("SELECT d FROM NPCDialogueEntity d WHERE d.npc.id = :npcId")
    List<NPCDialogueEntity> findByNpcId(String npcId);
    
    /**
     * РќР°Р№С‚Рё РЅР°С‡Р°Р»СЊРЅС‹Р№ РґРёР°Р»РѕРі NPC.
     */
    @Query("SELECT d FROM NPCDialogueEntity d WHERE d.npc.id = :npcId AND d.isInitial = true")
    Optional<NPCDialogueEntity> findInitialDialogueByNpcId(String npcId);
}

