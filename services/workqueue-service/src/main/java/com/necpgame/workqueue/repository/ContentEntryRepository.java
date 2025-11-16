package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ContentEntryRepository extends JpaRepository<ContentEntryEntity, UUID>, JpaSpecificationExecutor<ContentEntryEntity> {
    Optional<ContentEntryEntity> findByCodeIgnoreCase(String code);
}


