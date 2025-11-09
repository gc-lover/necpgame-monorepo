package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    Page<NotificationEntity> findByPlayerId(UUID playerId, Pageable pageable);

    Page<NotificationEntity> findByPlayerIdAndIsReadFalse(UUID playerId, Pageable pageable);

    Page<NotificationEntity> findByPlayerIdAndTypeIgnoreCase(UUID playerId, String type, Pageable pageable);

    Page<NotificationEntity> findByPlayerIdAndTypeIgnoreCaseAndIsReadFalse(UUID playerId, String type, Pageable pageable);

    List<NotificationEntity> findByPlayerIdAndIsReadFalse(UUID playerId);
}

