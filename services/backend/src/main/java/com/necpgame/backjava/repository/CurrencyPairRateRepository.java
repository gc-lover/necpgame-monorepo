package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CurrencyPairRateEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyPairRateRepository extends JpaRepository<CurrencyPairRateEntity, UUID> {

    Optional<CurrencyPairRateEntity> findFirstByPairOrderByTimestampDesc(String pair);

    List<CurrencyPairRateEntity> findTop20ByPairOrderByTimestampDesc(String pair);

    List<CurrencyPairRateEntity> findByBaseCurrencyAndTimestampAfterOrderByTimestampDesc(String baseCurrency, LocalDateTime timestamp);
}


