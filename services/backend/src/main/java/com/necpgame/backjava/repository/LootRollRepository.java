package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LootRollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для управления роллами на лут
 */
@Repository
public interface LootRollRepository extends JpaRepository<LootRollEntity, String> {
    
    /**
     * Найти все роллы для конкретного дропа
     */
    List<LootRollEntity> findByDropId(String dropId);
    
    /**
     * Найти активные роллы для персонажа
     */
    List<LootRollEntity> findByCharacterIdAndStatus(String characterId, String status);
}

