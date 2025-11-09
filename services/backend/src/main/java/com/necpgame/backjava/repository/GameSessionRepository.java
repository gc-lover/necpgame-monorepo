package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.GameSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * GameSessionRepository - СЂРµРїРѕР·РёС‚РѕСЂРёР№ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РёРіСЂРѕРІС‹РјРё СЃРµСЃСЃРёСЏРјРё.
 */
@Repository
public interface GameSessionRepository extends JpaRepository<GameSessionEntity, UUID> {

    /**
     * РќР°Р№С‚Рё Р°РєС‚РёРІРЅСѓСЋ СЃРµСЃСЃРёСЋ РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return Р°РєС‚РёРІРЅР°СЏ СЃРµСЃСЃРёСЏ
     */
    Optional<GameSessionEntity> findByCharacterIdAndIsActiveTrue(UUID characterId);

    /**
     * РќР°Р№С‚Рё РІСЃРµ СЃРµСЃСЃРёРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return СЃРїРёСЃРѕРє СЃРµСЃСЃРёР№
     */
    List<GameSessionEntity> findByCharacterIdOrderByCreatedAtDesc(UUID characterId);

    /**
     * РќР°Р№С‚Рё РІСЃРµ Р°РєС‚РёРІРЅС‹Рµ СЃРµСЃСЃРёРё Р°РєРєР°СѓРЅС‚Р°.
     *
     * @param accountId ID Р°РєРєР°СѓРЅС‚Р°
     * @return СЃРїРёСЃРѕРє Р°РєС‚РёРІРЅС‹С… СЃРµСЃСЃРёР№
     */
    List<GameSessionEntity> findByAccountIdAndIsActiveTrue(UUID accountId);

    /**
     * Р”РµР°РєС‚РёРІРёСЂРѕРІР°С‚СЊ РІСЃРµ СЃРµСЃСЃРёРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     *
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     */
    @Query("UPDATE GameSessionEntity s SET s.isActive = false WHERE s.characterId = :characterId AND s.isActive = true")
    void deactivateAllSessionsByCharacterId(@Param("characterId") UUID characterId);
}

