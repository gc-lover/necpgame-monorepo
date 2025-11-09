package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.InvestmentOpportunityEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvestmentOpportunityRepository extends JpaRepository<InvestmentOpportunityEntity, UUID>,
    JpaSpecificationExecutor<InvestmentOpportunityEntity> {
}


