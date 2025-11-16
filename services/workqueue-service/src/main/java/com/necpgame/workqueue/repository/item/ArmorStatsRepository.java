package com.necpgame.workqueue.repository.item;

import com.necpgame.workqueue.domain.item.ArmorStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArmorStatsRepository extends JpaRepository<ArmorStatsEntity, UUID> {
}

