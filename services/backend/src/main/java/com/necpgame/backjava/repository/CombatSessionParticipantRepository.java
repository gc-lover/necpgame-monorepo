package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CombatSessionParticipantEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombatSessionParticipantRepository extends JpaRepository<CombatSessionParticipantEntity, UUID> {

    List<CombatSessionParticipantEntity> findBySessionIdOrderByOrderIndex(UUID sessionId);

    Optional<CombatSessionParticipantEntity> findBySessionIdAndReferenceId(UUID sessionId, String referenceId);
}



