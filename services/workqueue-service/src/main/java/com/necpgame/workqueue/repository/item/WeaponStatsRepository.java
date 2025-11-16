package com.necpgame.workqueue.repository.item;

import com.necpgame.workqueue.domain.item.WeaponStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WeaponStatsRepository extends JpaRepository<WeaponStatsEntity, UUID> {
}

