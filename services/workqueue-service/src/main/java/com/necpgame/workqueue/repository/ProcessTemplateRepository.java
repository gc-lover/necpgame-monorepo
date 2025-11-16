package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.process.ProcessTemplateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcessTemplateRepository extends JpaRepository<ProcessTemplateEntity, UUID> {
    @EntityGraph(attributePaths = {"code"})
    List<ProcessTemplateEntity> findAllByOrderByNameAsc();

    @EntityGraph(attributePaths = {"code"})
    Optional<ProcessTemplateEntity> findByCode_CodeIgnoreCase(String code);
}


