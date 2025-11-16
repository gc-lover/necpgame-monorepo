package com.necpgame.workqueue.repository.npc;

import com.necpgame.workqueue.domain.npc.NpcDialogueLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NpcDialogueLinkRepository extends JpaRepository<NpcDialogueLinkEntity, UUID> {
    List<NpcDialogueLinkEntity> findByNpc_IdOrderByPriorityDesc(UUID npcId);

    void deleteByNpc_Id(UUID npcId);
}

