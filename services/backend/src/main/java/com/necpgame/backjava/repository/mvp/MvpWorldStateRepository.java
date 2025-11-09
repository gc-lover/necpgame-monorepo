package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpWorldStateEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MvpWorldStateRepository extends JpaRepository<MvpWorldStateEntity, UUID> {

    @EntityGraph(attributePaths = "activeEvents")
    Optional<MvpWorldStateEntity> findTopByOrderByCapturedAtDesc();
}


