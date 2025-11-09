package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpTextActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpTextActionRepository extends JpaRepository<MvpTextActionEntity, UUID> {

    List<MvpTextActionEntity> findAllByOrderByActionAsc();
}
