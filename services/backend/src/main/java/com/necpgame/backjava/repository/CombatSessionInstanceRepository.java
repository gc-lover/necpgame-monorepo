package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CombatSessionInstanceEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombatSessionInstanceRepository extends JpaRepository<CombatSessionInstanceEntity, UUID> {
}



