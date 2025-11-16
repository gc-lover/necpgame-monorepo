package com.necpgame.workqueue.repository.npc;

import com.necpgame.workqueue.domain.npc.NpcScheduleEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NpcScheduleEntryRepository extends JpaRepository<NpcScheduleEntryEntity, UUID> {
    List<NpcScheduleEntryEntity> findByNpc_IdOrderByDayTimeRangeAsc(UUID npcId);

    void deleteByNpc_Id(UUID npcId);
}

