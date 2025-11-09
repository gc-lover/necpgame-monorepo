package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterNPCInteractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РІР·Р°РёРјРѕРґРµР№СЃС‚РІРёСЏРјРё РїРµСЂСЃРѕРЅР°Р¶Р° СЃ NPC.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Repository
public interface CharacterNPCInteractionRepository extends JpaRepository<CharacterNPCInteractionEntity, UUID> {
    
    /**
     * РќР°Р№С‚Рё РІР·Р°РёРјРѕРґРµР№СЃС‚РІРёРµ РїРµСЂСЃРѕРЅР°Р¶Р° СЃ NPC.
     */
    @Query("SELECT i FROM CharacterNPCInteractionEntity i WHERE i.character.id = :characterId AND i.npc.id = :npcId")
    Optional<CharacterNPCInteractionEntity> findByCharacterIdAndNpcId(UUID characterId, String npcId);
    
    /**
     * РќР°Р№С‚Рё РІСЃРµ РІР·Р°РёРјРѕРґРµР№СЃС‚РІРёСЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT i FROM CharacterNPCInteractionEntity i WHERE i.character.id = :characterId")
    List<CharacterNPCInteractionEntity> findByCharacterId(UUID characterId);
}

