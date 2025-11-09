package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CurrencyExchangeRateEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRateEntity, UUID> {

    Optional<CurrencyExchangeRateEntity> findFirstByOrderByTimestampDesc();
}


