package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildRouteEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingGuildRouteRepository extends JpaRepository<TradingGuildRouteEntity, UUID> {

    List<TradingGuildRouteEntity> findByGuildId(UUID guildId);
}

