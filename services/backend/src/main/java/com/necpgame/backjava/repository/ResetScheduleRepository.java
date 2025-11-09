package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.ResetScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetScheduleRepository extends JpaRepository<ResetScheduleEntity, String> {
}

