package com.necpgame.backjava.repository.mvp;

import com.necpgame.backjava.entity.mvp.MvpStarterLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MvpStarterLocationRepository extends JpaRepository<MvpStarterLocationEntity, UUID> {

    List<MvpStarterLocationEntity> findAllByOrderByLocationNameAsc();
}
