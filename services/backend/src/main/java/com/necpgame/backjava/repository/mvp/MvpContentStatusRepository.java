package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpContentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MvpContentStatusRepository extends JpaRepository<MvpContentStatusEntity, UUID> {

    Optional<MvpContentStatusEntity> findTopByOrderByIdAsc();
}
