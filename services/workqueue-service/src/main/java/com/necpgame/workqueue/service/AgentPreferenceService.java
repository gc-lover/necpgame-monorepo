package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentPreferenceEntity;
import com.necpgame.workqueue.repository.AgentPreferenceRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.model.AgentPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentPreferenceService {
    private static final String DELIMITER = ",";
    private final AgentPreferenceRepository agentPreferenceRepository;

    @Transactional(readOnly = true)
    public AgentPreference get(UUID agentId) {
        return find(agentId).orElseThrow(() -> new EntityNotFoundException("Agent preference not configured"));
    }

    @Transactional(readOnly = true)
    public Optional<AgentPreference> find(UUID agentId) {
        return agentPreferenceRepository.findByAgentId(agentId).map(this::toModel);
    }

    @Transactional
    public AgentPreference upsert(UUID agentId,
                                  String roleKey,
                                  List<String> primarySegments,
                                  List<String> fallbackSegments,
                                  List<String> pickupStatuses,
                                  List<String> activeStatuses,
                                  String acceptStatus,
                                  String returnStatus,
                                  int maxInProgressMinutes) {
        AgentPreferenceEntity entity = agentPreferenceRepository.findByAgentId(agentId)
                .orElseGet(AgentPreferenceEntity::new);
        entity.setAgentId(agentId);
        entity.setRoleKey(roleKey);
        entity.setPrimarySegments(joinList(primarySegments));
        entity.setFallbackSegments(joinList(fallbackSegments));
        entity.setPickupStatuses(joinList(pickupStatuses));
        entity.setActiveStatuses(joinList(activeStatuses));
        entity.setAcceptStatus(acceptStatus);
        entity.setReturnStatus(returnStatus);
        entity.setMaxInProgressMinutes(maxInProgressMinutes);
        AgentPreferenceEntity saved = agentPreferenceRepository.save(entity);
        return toModel(saved);
    }

    private List<String> parseList(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(DELIMITER))
                .map(String::trim)
                .filter(segment -> !segment.isEmpty())
                .toList();
    }

    private String joinList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.stream()
                .filter(v -> v != null && !v.trim().isEmpty())
                .map(String::trim)
                .collect(Collectors.joining(DELIMITER));
    }

    private AgentPreference toModel(AgentPreferenceEntity entity) {
        return new AgentPreference(
                entity.getRoleKey(),
                parseList(entity.getPrimarySegments()),
                parseList(entity.getFallbackSegments()),
                parseList(entity.getPickupStatuses()),
                parseList(entity.getActiveStatuses()),
                entity.getAcceptStatus(),
                entity.getReturnStatus(),
                entity.getMaxInProgressMinutes()
        );
    }
}

