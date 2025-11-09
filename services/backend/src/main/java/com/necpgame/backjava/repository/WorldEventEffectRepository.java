package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.WorldEventEffectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface WorldEventEffectRepository extends JpaRepository<WorldEventEffectEntity, UUID> {

    @Query("select effect from WorldEventEffectEntity effect where effect.event.id = :eventId")
    List<WorldEventEffectEntity> findByEventId(@Param("eventId") UUID eventId);
}

