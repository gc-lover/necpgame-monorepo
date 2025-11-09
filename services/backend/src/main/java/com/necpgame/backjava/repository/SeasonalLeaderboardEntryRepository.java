package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.SeasonalLeaderboardEntryEntity;
import com.necpgame.backjava.entity.SeasonalLeaderboardEntryId;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonalLeaderboardEntryRepository extends JpaRepository<SeasonalLeaderboardEntryEntity, SeasonalLeaderboardEntryId> {

    Page<SeasonalLeaderboardEntryEntity> findByIdSeasonIdAndIdCategory(String seasonId, String category, Pageable pageable);

    long countByIdSeasonIdAndIdCategory(String seasonId, String category);

    Optional<SeasonalLeaderboardEntryEntity> findByIdSeasonIdAndIdCategoryAndIdPlayerId(String seasonId, String category, UUID playerId);

    long countByIdSeasonIdAndIdCategoryAndScoreGreaterThan(String seasonId, String category, BigDecimal score);
}

