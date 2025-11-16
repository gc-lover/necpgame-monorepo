package com.necpgame.workqueue.repository.npc;

import com.necpgame.workqueue.domain.npc.NpcInventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NpcInventoryItemRepository extends JpaRepository<NpcInventoryItemEntity, UUID> {
    List<NpcInventoryItemEntity> findByNpc_Id(UUID npcId);

    void deleteByNpc_Id(UUID npcId);
}

