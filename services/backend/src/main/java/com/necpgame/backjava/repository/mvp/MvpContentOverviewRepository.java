package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpContentOverviewEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MvpContentOverviewRepository extends JpaRepository<MvpContentOverviewEntity, UUID> {

    @EntityGraph(attributePaths = "keyEvents")
    Optional<MvpContentOverviewEntity> findByPeriod(String period);

    @EntityGraph(attributePaths = "keyEvents")
    Optional<MvpContentOverviewEntity> findTopByOrderByPeriodAsc();

}
