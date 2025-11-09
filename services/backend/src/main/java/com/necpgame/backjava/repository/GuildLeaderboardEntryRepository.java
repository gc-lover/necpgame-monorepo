package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.GuildLeaderboardEntryEntity;
import com.necpgame.backjava.entity.GuildLeaderboardEntryId;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildLeaderboardEntryRepository extends JpaRepository<GuildLeaderboardEntryEntity, GuildLeaderboardEntryId> {

    Page<GuildLeaderboardEntryEntity> findByIdCategory(String category, Pageable pageable);

    long countByIdCategory(String category);

    Optional<GuildLeaderboardEntryEntity> findByIdCategoryAndIdGuildId(String category, UUID guildId);

    long countByIdCategoryAndScoreGreaterThan(String category, BigDecimal score);
}

