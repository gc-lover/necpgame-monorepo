package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpStarterQuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpStarterQuestRepository extends JpaRepository<MvpStarterQuestEntity, UUID> {

    List<MvpStarterQuestEntity> findAllByOrderByQuestCodeAsc();
}
