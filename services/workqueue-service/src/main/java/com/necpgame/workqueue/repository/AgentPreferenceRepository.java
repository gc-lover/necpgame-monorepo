package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.AgentPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgentPreferenceRepository extends JpaRepository<AgentPreferenceEntity, UUID> {
    Optional<AgentPreferenceEntity> findByAgentId(UUID agentId);
}

