package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CombatSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CombatSessionRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ Р±РѕРµРІС‹РјРё СЃРµСЃСЃРёСЏРјРё.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/combat/combat.yaml
 */
@Repository
public interface CombatSessionRepository extends JpaRepository<CombatSessionEntity, UUID> {

    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅСѓСЋ Р±РѕРµРІСѓСЋ СЃРµСЃСЃРёСЋ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CombatSessionEntity cs WHERE cs.characterId = :characterId AND cs.status = 'ACTIVE'")
    Optional<CombatSessionEntity> findActiveByCharacterId(UUID characterId);

    /**
     * РџСЂРѕРІРµСЂРёС‚СЊ РµСЃС‚СЊ Р»Рё Р°РєС‚РёРІРЅС‹Р№ Р±РѕР№ Сѓ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT COUNT(cs) > 0 FROM CombatSessionEntity cs WHERE cs.characterId = :characterId AND cs.status = 'ACTIVE'")
    boolean hasActiveCombat(UUID characterId);

    /**
     * РќР°Р№С‚Рё РІСЃРµ Р±РѕРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CombatSessionEntity cs WHERE cs.characterId = :characterId ORDER BY cs.createdAt DESC")
    List<CombatSessionEntity> findByCharacterIdOrderByCreatedAtDesc(UUID characterId);

    /**
     * РќР°Р№С‚Рё Р·Р°РІРµСЂС€РµРЅРЅС‹Рµ Р±РѕРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    @Query("SELECT cs FROM CombatSessionEntity cs WHERE cs.characterId = :characterId AND cs.status IN ('ENDED', 'FLED') ORDER BY cs.endedAt DESC")
    List<CombatSessionEntity> findCompletedByCharacterId(UUID characterId);
}

