package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.process.ChecklistDefinitionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChecklistDefinitionRepository extends JpaRepository<ChecklistDefinitionEntity, UUID> {
    @EntityGraph(attributePaths = {"code"})
    List<ChecklistDefinitionEntity> findAllByOrderByNameAsc();

    @EntityGraph(attributePaths = {"code", "items"})
    Optional<ChecklistDefinitionEntity> findDetailedByCode_CodeIgnoreCase(String code);
}


