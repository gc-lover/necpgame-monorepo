package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildTreasuryEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingGuildTreasuryRepository extends JpaRepository<TradingGuildTreasuryEntity, UUID> {
}

