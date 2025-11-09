package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsConvoyEntity;
import com.necpgame.backjava.entity.LogisticsConvoyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsConvoyMemberRepository extends JpaRepository<LogisticsConvoyMemberEntity, UUID> {

    List<LogisticsConvoyMemberEntity> findByConvoy(LogisticsConvoyEntity convoy);
}
