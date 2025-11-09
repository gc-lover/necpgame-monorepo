package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LeaderboardEntryEntity;
import com.necpgame.backjava.entity.LeaderboardEntryId;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntryEntity, LeaderboardEntryId> {

    Page<LeaderboardEntryEntity> findByIdCategory(String category, Pageable pageable);

    long countByIdCategory(String category);

    Optional<LeaderboardEntryEntity> findByIdCategoryAndIdPlayerId(String category, UUID playerId);

    long countByIdCategoryAndScoreGreaterThan(String category, BigDecimal score);

    List<LeaderboardEntryEntity> findByIdCategoryAndIdPlayerIdIn(String category, Collection<UUID> playerIds, Sort sort);
}

