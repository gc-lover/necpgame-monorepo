package com.necpgame.workqueue.repository.item;

import com.necpgame.workqueue.domain.item.ItemDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemDataRepository extends JpaRepository<ItemDataEntity, UUID> {
}

