package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildDistributionEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingGuildDistributionRepository extends JpaRepository<TradingGuildDistributionEntity, UUID> {

    List<TradingGuildDistributionEntity> findTop10ByGuildIdOrderByCreatedAtDesc(UUID guildId);
}

