package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerOrderEntity;
import com.necpgame.backjava.entity.PlayerOrderReviewEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerOrderReviewRepository extends JpaRepository<PlayerOrderReviewEntity, UUID> {

    List<PlayerOrderReviewEntity> findAllByOrder(PlayerOrderEntity order);
}

