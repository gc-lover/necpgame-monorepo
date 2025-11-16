package com.necpgame.workqueue.repository.item;

import com.necpgame.workqueue.domain.item.ItemComponentRequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemComponentRequirementRepository extends JpaRepository<ItemComponentRequirementEntity, UUID> {
    List<ItemComponentRequirementEntity> findByItem_Id(UUID itemId);
    void deleteByItem_Id(UUID itemId);
}

