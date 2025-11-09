package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.WorldEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface WorldEventRepository extends JpaRepository<WorldEventEntity, UUID>, JpaSpecificationExecutor<WorldEventEntity> {
}

