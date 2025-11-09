package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CurrencyRateHistoryEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateHistoryRepository extends JpaRepository<CurrencyRateHistoryEntity, UUID> {

    List<CurrencyRateHistoryEntity> findTop500ByPairOrderByTimestampDesc(String pair);

    List<CurrencyRateHistoryEntity> findTop500ByPairAndPeriodIgnoreCaseAndIntervalIgnoreCaseOrderByTimestampDesc(String pair, String period, String interval);
}


