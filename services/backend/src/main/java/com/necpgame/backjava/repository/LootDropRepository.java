package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LootDropEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Repository для управления лутом в мире
 */
@Repository
public interface LootDropRepository extends JpaRepository<LootDropEntity, String> {
    
    /**
     * Найти все дропы, которые еще не залучены и не истекли
     */
    List<LootDropEntity> findByLootedFalseAndExpiresAtAfter(OffsetDateTime now);
    
    /**
     * Найти все дропы для группы
     */
    List<LootDropEntity> findByPartyIdAndLootedFalse(String partyId);
}

