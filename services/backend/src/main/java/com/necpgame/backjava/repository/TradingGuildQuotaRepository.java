package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildQuotaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingGuildQuotaRepository extends JpaRepository<TradingGuildQuotaEntity, UUID> {

    List<TradingGuildQuotaEntity> findByGuildId(UUID guildId);
}

