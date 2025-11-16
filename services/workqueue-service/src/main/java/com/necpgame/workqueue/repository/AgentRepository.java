package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<AgentEntity, UUID> {
    Optional<AgentEntity> findByRoleKeyAndActiveIsTrue(String roleKey);
    List<AgentEntity> findByActiveTrueOrderByDisplayNameAsc();
}


