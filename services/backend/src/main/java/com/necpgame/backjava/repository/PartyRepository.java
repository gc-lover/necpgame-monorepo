package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PartyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository для управления группами (party)
 */
@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, String> {
    
    /**
     * Найти группу по лидеру
     */
    Optional<PartyEntity> findByLeaderCharacterId(String leaderCharacterId);
    
    /**
     * Найти группу по члену (через JSON search)
     */
    @Query("SELECT p FROM PartyEntity p WHERE p.membersJson LIKE %:characterId%")
    Optional<PartyEntity> findByMemberCharacterId(String characterId);
}

