package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpStarterFactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpStarterFactionRepository extends JpaRepository<MvpStarterFactionEntity, UUID> {

    List<MvpStarterFactionEntity> findAllByOrderByNameAsc();

}
