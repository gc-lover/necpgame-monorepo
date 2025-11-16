package com.necpgame.workqueue.repository.item;

import com.necpgame.workqueue.domain.item.ItemModSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemModSlotRepository extends JpaRepository<ItemModSlotEntity, UUID> {
    List<ItemModSlotEntity> findByItem_Id(UUID itemId);
    void deleteByItem_Id(UUID itemId);
}

