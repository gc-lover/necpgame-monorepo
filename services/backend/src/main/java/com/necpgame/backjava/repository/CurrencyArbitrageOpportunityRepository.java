package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CurrencyArbitrageOpportunityEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyArbitrageOpportunityRepository extends JpaRepository<CurrencyArbitrageOpportunityEntity, UUID> {

    List<CurrencyArbitrageOpportunityEntity> findAllByOrderByProfitPotentialDesc();
}


