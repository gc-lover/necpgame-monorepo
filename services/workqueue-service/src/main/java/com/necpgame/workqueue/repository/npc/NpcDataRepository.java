package com.necpgame.workqueue.repository.npc;

import com.necpgame.workqueue.domain.npc.NpcDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NpcDataRepository extends JpaRepository<NpcDataEntity, UUID> {
}

