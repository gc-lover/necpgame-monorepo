package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CurrencyExchangeOrderEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeOrderRepository extends JpaRepository<CurrencyExchangeOrderEntity, UUID> {
}


