package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.InvestmentFundEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentFundRepository extends JpaRepository<InvestmentFundEntity, UUID> {

    List<InvestmentFundEntity> findAllByOrderByPerformanceYtdDesc();
}

