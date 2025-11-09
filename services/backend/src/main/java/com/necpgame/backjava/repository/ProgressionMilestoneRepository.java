package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.ProgressionMilestoneEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressionMilestoneRepository extends JpaRepository<ProgressionMilestoneEntity, UUID> {
}



