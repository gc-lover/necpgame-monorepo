package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CombatSessionEventEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombatSessionEventRepository extends JpaRepository<CombatSessionEventEntity, Long> {

    List<CombatSessionEventEntity> findBySessionIdAndIdGreaterThanOrderById(UUID sessionId, Long id);

    List<CombatSessionEventEntity> findBySessionIdOrderById(UUID sessionId);
}



