package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpTextNearbyNpcEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpTextNearbyNpcRepository extends JpaRepository<MvpTextNearbyNpcEntity, UUID> {

    @EntityGraph(attributePaths = "location")
    List<MvpTextNearbyNpcEntity> findByLocationIdOrderByNpcNameAsc(String locationId);
}

