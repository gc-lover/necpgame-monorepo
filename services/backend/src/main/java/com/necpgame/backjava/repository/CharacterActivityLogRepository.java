package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterActivityLogEntity;
import com.necpgame.backjava.entity.CharacterActivityLogEntity.ActivityType;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterActivityLogRepository extends JpaRepository<CharacterActivityLogEntity, UUID> {

    Page<CharacterActivityLogEntity> findByAccountIdAndActivityTypeInAndOccurredAtBetween(
        UUID accountId,
        List<ActivityType> activityTypes,
        OffsetDateTime from,
        OffsetDateTime to,
        Pageable pageable
    );

    Page<CharacterActivityLogEntity> findByAccountIdAndOccurredAtBetween(
        UUID accountId,
        OffsetDateTime from,
        OffsetDateTime to,
        Pageable pageable
    );

    Page<CharacterActivityLogEntity> findByAccountId(
        UUID accountId,
        Pageable pageable
    );
}
