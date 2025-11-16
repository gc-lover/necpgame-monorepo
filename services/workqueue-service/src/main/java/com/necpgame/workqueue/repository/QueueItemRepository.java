package com.necpgame.workqueue.repository;

import com.necpgame.workqueue.domain.QueueItemEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueItemRepository extends JpaRepository<QueueItemEntity, UUID> {
    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegment(String segment);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentAndCurrentStateStatusCodeIn(String segment, Collection<String> statuses);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentAndAssignedToId(String segment, UUID assignedTo);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentAndAssignedToIdAndCurrentStateStatusCodeIn(String segment, UUID assignedTo, Collection<String> statuses);

    @EntityGraph(attributePaths = {"queue", "currentState", "currentState.actor", "assignedTo", "createdBy"})
    Optional<QueueItemEntity> findById(UUID id);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByAssignedToIdAndCurrentStateStatusCodeIn(UUID assignedTo, Collection<String> statuses);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentAndStatusValueIdIn(String segment, Collection<UUID> statusIds);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentAndAssignedToIdAndStatusValueIdIn(String segment, UUID assignedTo, Collection<UUID> statusIds);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentInAndCurrentStateStatusCodeIn(Collection<String> segments, Collection<String> statuses);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByAssignedToIdAndStatusValueIdIn(UUID assignedTo, Collection<UUID> statusIds);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    List<QueueItemEntity> findByQueueSegmentInAndStatusValueIdIn(Collection<String> segments, Collection<UUID> statusIds);

    @EntityGraph(attributePaths = {"queue", "currentState", "assignedTo"})
    @Query("""
        select qi
        from QueueItemEntity qi
        where qi.queue.segment in :segments
          and qi.assignedTo is null
          and qi.statusValueId in :statusIds
          and (qi.currentState is null or qi.currentState.statusCode not in ('completed','cancelled'))
          and (:priorityFloor is null or qi.priority >= :priorityFloor)
        order by qi.priority desc, qi.createdAt asc
        """)
    List<QueueItemEntity> findNextTaskCandidate(@Param("segments") Collection<String> segments,
                                                @Param("statusIds") Collection<UUID> statusIds,
                                                @Param("priorityFloor") Integer priorityFloor);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select qi from QueueItemEntity qi where qi.id = :id")
    Optional<QueueItemEntity> lockById(@Param("id") UUID id);

    @Query("select qi from QueueItemEntity qi where qi.lockedUntil < :threshold")
    List<QueueItemEntity> findExpiredLocks(@Param("threshold") OffsetDateTime threshold);

    Optional<QueueItemEntity> findByExternalRef(String externalRef);
}