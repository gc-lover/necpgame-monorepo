package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TradingGuildEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TradingGuildRepository extends JpaRepository<TradingGuildEntity, UUID>,
    JpaSpecificationExecutor<TradingGuildEntity> {

    boolean existsByNameIgnoreCase(String name);
}

