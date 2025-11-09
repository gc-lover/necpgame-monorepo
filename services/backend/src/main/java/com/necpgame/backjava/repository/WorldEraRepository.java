package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.WorldEraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorldEraRepository extends JpaRepository<WorldEraEntity, String> {

    Optional<WorldEraEntity> findFirstByCurrentTrue();
}

