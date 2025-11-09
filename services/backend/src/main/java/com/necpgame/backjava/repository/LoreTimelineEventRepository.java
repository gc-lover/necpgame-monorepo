package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreTimelineEventEntity;
import com.necpgame.backjava.entity.enums.TimelineEventType;
import com.necpgame.backjava.entity.enums.TimelineImpactLevel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoreTimelineEventRepository extends JpaRepository<LoreTimelineEventEntity, UUID> {

    List<LoreTimelineEventEntity> findAllByOrderByYearAsc();

    List<LoreTimelineEventEntity> findByEraIgnoreCaseOrderByYearAsc(String era);

    List<LoreTimelineEventEntity> findByTypeOrderByYearAsc(TimelineEventType type);

    List<LoreTimelineEventEntity> findByEraIgnoreCaseAndTypeOrderByYearAsc(String era, TimelineEventType type);

    List<LoreTimelineEventEntity> findByImpactLevelOrderByYearAsc(TimelineImpactLevel impactLevel);

    List<LoreTimelineEventEntity> findTop10ByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}


