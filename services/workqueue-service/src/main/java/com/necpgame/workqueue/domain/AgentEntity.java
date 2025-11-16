package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "agents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "role_key", nullable = false, length = 64)
    private String roleKey;

    @Column(name = "display_name", nullable = false, length = 128)
    private String displayName;

    @Column(length = 256)
    private String contact;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

