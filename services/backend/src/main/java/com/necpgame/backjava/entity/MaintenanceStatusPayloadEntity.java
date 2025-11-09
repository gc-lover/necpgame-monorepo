package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "maintenance_status_payloads", indexes = {
    @Index(name = "idx_maintenance_status_payloads_created", columnList = "created_at")
})
public class MaintenanceStatusPayloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "progress_percent")
    private Double progressPercent;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_public", nullable = false)
    private boolean publicVisible;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}





