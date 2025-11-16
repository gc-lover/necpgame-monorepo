package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.repository.AgentRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgentDirectoryService {
    private final AgentRepository agentRepository;

    @Transactional(readOnly = true)
    public AgentEntity requireAgent(UUID id) {
        Objects.requireNonNull(id, "Agent id required");
        return agentRepository.findById(id).filter(AgentEntity::isActive).orElseThrow(() -> new EntityNotFoundException("Agent not found"));
    }

    @Transactional(readOnly = true)
    public List<AgentEntity> listActive() {
        return agentRepository.findByActiveTrueOrderByDisplayNameAsc();
    }

    @Transactional(readOnly = true)
    public AgentEntity requireActiveByRole(String roleKey) {
        Objects.requireNonNull(roleKey, "role key required");
        return agentRepository.findByRoleKeyAndActiveIsTrue(roleKey)
                .orElseThrow(() -> new EntityNotFoundException("Agent not found"));
    }
}

