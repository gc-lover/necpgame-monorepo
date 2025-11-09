package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CombatParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CombatParticipantRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ СѓС‡Р°СЃС‚РЅРёРєР°РјРё Р±РѕСЏ.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/combat/combat.yaml
 */
@Repository
public interface CombatParticipantRepository extends JpaRepository<CombatParticipantEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РІСЃРµС… СѓС‡Р°СЃС‚РЅРёРєРѕРІ Р±РѕРµРІРѕР№ СЃРµСЃСЃРёРё.
     */
    @Query("SELECT cp FROM CombatParticipantEntity cp WHERE cp.combatSessionId = :combatSessionId ORDER BY cp.initiative DESC")
    List<CombatParticipantEntity> findByCombatSessionIdOrderByInitiativeDesc(UUID combatSessionId);

    /**
     * РќР°Р№С‚Рё Р¶РёРІС‹С… СѓС‡Р°СЃС‚РЅРёРєРѕРІ Р±РѕСЏ.
     */
    @Query("SELECT cp FROM CombatParticipantEntity cp WHERE cp.combatSessionId = :combatSessionId AND cp.isAlive = true ORDER BY cp.initiative DESC")
    List<CombatParticipantEntity> findAliveByCombatSessionId(UUID combatSessionId);

    /**
     * РќР°Р№С‚Рё СѓС‡Р°СЃС‚РЅРёРєР° РїРѕ ID (participantId).
     */
    @Query("SELECT cp FROM CombatParticipantEntity cp WHERE cp.combatSessionId = :combatSessionId AND cp.participantId = :participantId")
    Optional<CombatParticipantEntity> findBySessionAndParticipantId(UUID combatSessionId, String participantId);

    /**
     * РџРѕСЃС‡РёС‚Р°С‚СЊ Р¶РёРІС‹С… РІСЂР°РіРѕРІ РІ Р±РѕСЋ.
     */
    @Query("SELECT COUNT(cp) FROM CombatParticipantEntity cp WHERE cp.combatSessionId = :combatSessionId AND cp.participantType = 'ENEMY' AND cp.isAlive = true")
    long countAliveEnemies(UUID combatSessionId);

    /**
     * РџРѕСЃС‡РёС‚Р°С‚СЊ Р¶РёРІС‹С… СЃРѕСЋР·РЅРёРєРѕРІ (РёРіСЂРѕРє + NPC).
     */
    @Query("SELECT COUNT(cp) FROM CombatParticipantEntity cp WHERE cp.combatSessionId = :combatSessionId AND cp.participantType IN ('PLAYER', 'NPC') AND cp.isAlive = true")
    long countAliveAllies(UUID combatSessionId);
}

