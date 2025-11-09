package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.InvestmentEntity;
import com.necpgame.backjava.entity.InvestmentEntity.InvestmentStatus;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvestmentRepository extends JpaRepository<InvestmentEntity, UUID> {

    List<InvestmentEntity> findAllByCharacterId(UUID characterId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InvestmentEntity i where i.id = :investmentId")
    Optional<InvestmentEntity> lockById(@Param("investmentId") UUID investmentId);

    long countByOpportunityIdAndStatusNot(UUID opportunityId, InvestmentStatus status);

    List<InvestmentEntity> findAllByOpportunityIdAndMaturityDateBefore(UUID opportunityId, OffsetDateTime cutoff);
}


