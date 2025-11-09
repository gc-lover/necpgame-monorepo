package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.MaintenanceAuditEntryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaintenanceAuditEntryRepository extends JpaRepository<MaintenanceAuditEntryEntity, UUID> {

    @Query("""
        select a from MaintenanceAuditEntryEntity a
        where (:windowId is null or a.window.id = :windowId)
          and (:actor is null or lower(a.actor) = lower(:actor))
          and (:action is null or lower(a.action) = lower(:action))
        order by a.createdAt desc
        """)
    Page<MaintenanceAuditEntryEntity> findAllFiltered(@Param("windowId") UUID windowId,
                                                      @Param("actor") String actor,
                                                      @Param("action") String action,
                                                      Pageable pageable);
}





