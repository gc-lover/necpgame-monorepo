package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildBankTransactionEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingGuildBankTransactionRepository extends JpaRepository<TradingGuildBankTransactionEntity, UUID> {

    List<TradingGuildBankTransactionEntity> findTop10ByGuildIdOrderByCreatedAtDesc(UUID guildId);
}

