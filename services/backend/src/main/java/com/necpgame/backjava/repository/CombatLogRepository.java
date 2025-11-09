package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CombatLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * CombatLogRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ Р»РѕРіР°РјРё Р±РѕСЏ.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/combat/combat.yaml
 */
@Repository
public interface CombatLogRepository extends JpaRepository<CombatLogEntity, UUID> {

    /**
     * РќР°Р№С‚Рё РІСЃРµ Р»РѕРіРё Р±РѕРµРІРѕР№ СЃРµСЃСЃРёРё.
     */
    @Query("SELECT cl FROM CombatLogEntity cl WHERE cl.combatSessionId = :combatSessionId ORDER BY cl.round, cl.actionOrder")
    List<CombatLogEntity> findByCombatSessionIdOrderByRoundAndActionOrder(UUID combatSessionId);

    /**
     * РќР°Р№С‚Рё Р»РѕРіРё РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ СЂР°СѓРЅРґР°.
     */
    @Query("SELECT cl FROM CombatLogEntity cl WHERE cl.combatSessionId = :combatSessionId AND cl.round = :round ORDER BY cl.actionOrder")
    List<CombatLogEntity> findBySessionAndRound(UUID combatSessionId, Integer round);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РїРѕСЃР»РµРґРЅРёР№ action order РІ СЂР°СѓРЅРґРµ.
     */
    @Query("SELECT COALESCE(MAX(cl.actionOrder), 0) FROM CombatLogEntity cl WHERE cl.combatSessionId = :combatSessionId AND cl.round = :round")
    Integer getLastActionOrder(UUID combatSessionId, Integer round);
}

