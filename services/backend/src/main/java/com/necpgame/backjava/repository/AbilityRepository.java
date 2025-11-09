package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.AbilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbilityRepository extends JpaRepository<AbilityEntity, String> {

    @Query("SELECT a FROM AbilityEntity a WHERE a.slot = :slot AND a.available = true ORDER BY a.minLevel, a.name")
    List<AbilityEntity> findBySlot(String slot);

    @Query("SELECT a FROM AbilityEntity a WHERE a.sourceType = :sourceType AND a.available = true ORDER BY a.slot, a.name")
    List<AbilityEntity> findBySourceType(String sourceType);

    @Query("SELECT a FROM AbilityEntity a WHERE a.available = true ORDER BY a.slot, a.sourceType, a.name")
    List<AbilityEntity> findAllAvailable();
}

