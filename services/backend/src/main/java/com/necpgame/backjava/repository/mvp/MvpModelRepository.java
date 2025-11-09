package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpModelEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpModelRepository extends JpaRepository<MvpModelEntity, UUID> {

    @EntityGraph(attributePaths = "fields")
    List<MvpModelEntity> findAllByOrderByModelNameAsc();
}

