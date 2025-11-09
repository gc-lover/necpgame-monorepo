package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.MaintenanceWindowEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaintenanceWindowRepository extends JpaRepository<MaintenanceWindowEntity, UUID> {

    @Query("""
        select w from MaintenanceWindowEntity w
        where (:status is null or w.status = :status)
          and (:type is null or w.type = :type)
          and (:environment is null or w.environment = :environment)
          and (:service is null or lower(coalesce(w.servicesJson, '')) like lower(concat('%', :service, '%')))
        order by w.startAt desc
        """)
    Page<MaintenanceWindowEntity> findAllFiltered(@Param("status") String status,
                                                  @Param("type") String type,
                                                  @Param("environment") String environment,
                                                  @Param("service") String service,
                                                  Pageable pageable);

    Optional<MaintenanceWindowEntity> findFirstByStatusInOrderByUpdatedAtDesc(Collection<String> statuses);

    Optional<MaintenanceWindowEntity> findFirstByStatusInOrderByStartAtAsc(Collection<String> statuses);

    @Query("""
        select w from MaintenanceWindowEntity w
        where w.environment = :environment
          and w.status in :statuses
          and (w.startAt <= :endAt)
          and (w.endAt is null or w.endAt >= :startAt)
        """)
    List<MaintenanceWindowEntity> findConflictingWindows(@Param("environment") String environment,
                                                         @Param("statuses") Collection<String> statuses,
                                                         @Param("startAt") OffsetDateTime startAt,
                                                         @Param("endAt") OffsetDateTime endAt);
}





