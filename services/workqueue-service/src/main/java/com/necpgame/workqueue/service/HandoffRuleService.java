package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.HandoffRuleEntity;
import com.necpgame.workqueue.repository.HandoffRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HandoffRuleService {
    private final HandoffRuleRepository handoffRuleRepository;

    public List<HandoffRuleEntity> findRules(String currentSegment, String statusCode) {
        if (currentSegment == null || currentSegment.isBlank()) {
            return List.of();
        }
        List<HandoffRuleEntity> rules = List.of();
        if (statusCode != null && !statusCode.isBlank()) {
            rules = handoffRuleRepository.findByCurrentSegmentAndStatusCode(currentSegment, statusCode);
        }
        if (rules.isEmpty()) {
            rules = handoffRuleRepository.findByCurrentSegmentAndStatusCodeIsNull(currentSegment);
        }
        return rules;
    }
}

