package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.HandoffRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HandoffRuleRepository extends JpaRepository<HandoffRuleEntity, UUID> {
    List<HandoffRuleEntity> findByCurrentSegmentAndStatusCode(String currentSegment, String statusCode);
    List<HandoffRuleEntity> findByCurrentSegmentAndStatusCodeIsNull(String currentSegment);
}

