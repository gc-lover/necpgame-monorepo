package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.MaintenanceStatusPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaintenanceStatusPayloadRepository extends JpaRepository<MaintenanceStatusPayloadEntity, Long> {

    Optional<MaintenanceStatusPayloadEntity> findFirstByOrderByCreatedAtDesc();
}





