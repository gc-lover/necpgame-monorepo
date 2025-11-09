package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.InvestmentDividendEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestmentDividendRepository extends JpaRepository<InvestmentDividendEntity, UUID> {

    List<InvestmentDividendEntity> findAllByInvestmentIdOrderByPaidAtAsc(UUID investmentId);
}