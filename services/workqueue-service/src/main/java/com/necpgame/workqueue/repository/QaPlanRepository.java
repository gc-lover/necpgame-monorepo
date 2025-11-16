package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.qa.QaPlanEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QaPlanRepository extends JpaRepository<QaPlanEntity, UUID> {
    @EntityGraph(attributePaths = {"contentEntity"})
    List<QaPlanEntity> findAllByOrderByPlanDateDesc();

    @EntityGraph(attributePaths = {
            "contentEntity",
            "preparedBy",
            "items",
            "items.testType",
            "report",
            "report.tester",
            "report.releaseDecision"
    })
    Optional<QaPlanEntity> findDetailedById(UUID id);

    @EntityGraph(attributePaths = {
            "contentEntity",
            "preparedBy",
            "items",
            "items.testType",
            "report",
            "report.tester",
            "report.releaseDecision"
    })
    Optional<QaPlanEntity> findDetailedByPlanCodeIgnoreCase(String planCode);
}


