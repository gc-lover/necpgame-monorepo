package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradeSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для управления торговыми сессиями
 */
@Repository
public interface TradeSessionRepository extends JpaRepository<TradeSessionEntity, String> {
    
    /**
     * Найти активные торговые сессии для персонажа
     */
    List<TradeSessionEntity> findByInitiatorCharacterIdOrReceiverCharacterIdAndStatusIn(
        String characterId1, String characterId2, List<String> statuses);
    
    /**
     * Найти активные сессии по статусу
     */
    List<TradeSessionEntity> findByStatusIn(List<String> statuses);
}

