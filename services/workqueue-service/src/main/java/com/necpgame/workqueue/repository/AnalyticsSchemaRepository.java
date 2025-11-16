package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.analytics.AnalyticsSchemaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnalyticsSchemaRepository extends JpaRepository<AnalyticsSchemaEntity, UUID> {
    @EntityGraph(attributePaths = {"contentEntity"})
    List<AnalyticsSchemaEntity> findAllByOrderByFeatureNameAsc();

    @EntityGraph(attributePaths = {"contentEntity", "metrics"})
    Optional<AnalyticsSchemaEntity> findDetailedById(UUID id);

    @EntityGraph(attributePaths = {"contentEntity", "metrics"})
    Optional<AnalyticsSchemaEntity> findDetailedByContentEntity_Id(UUID contentEntityId);
}


