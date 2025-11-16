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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "handoff_rules")
public class HandoffRuleEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "current_segment", nullable = false, length = 64)
    private String currentSegment;

    @Column(name = "status_code", length = 64)
    private String statusCode;

    @Column(name = "next_segment", nullable = false, length = 64)
    private String nextSegment;

    @Column(name = "template_codes", columnDefinition = "TEXT")
    private String templateCodes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}

