package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpEndpointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpEndpointRepository extends JpaRepository<MvpEndpointEntity, UUID> {

    List<MvpEndpointEntity> findAllByOrderByCategoryAscEndpointAsc();
}

