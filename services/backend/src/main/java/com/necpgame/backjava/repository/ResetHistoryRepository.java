package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.ResetHistoryEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetHistoryRepository extends JpaRepository<ResetHistoryEntity, UUID> {

    List<ResetHistoryEntity> findByExecutionTimeGreaterThanEqualOrderByExecutionTimeDesc(OffsetDateTime from);

    List<ResetHistoryEntity> findByResetTypeAndExecutionTimeGreaterThanEqualOrderByExecutionTimeDesc(String resetType, OffsetDateTime from);
}

