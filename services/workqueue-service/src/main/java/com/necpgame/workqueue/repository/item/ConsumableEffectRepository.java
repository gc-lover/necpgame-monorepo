package com.necpgame.workqueue.repository.item;

import com.necpgame.workqueue.domain.item.ConsumableEffectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsumableEffectRepository extends JpaRepository<ConsumableEffectEntity, UUID> {
    List<ConsumableEffectEntity> findByItem_Id(UUID itemId);
    void deleteByItem_Id(UUID itemId);
}

