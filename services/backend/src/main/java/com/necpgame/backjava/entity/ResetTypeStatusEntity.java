package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reset_type_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetTypeStatusEntity {

    @Id
    @Column(name = "reset_type", length = 20, nullable = false)
    private String resetType;

    @Column(name = "last_reset_at")
    private OffsetDateTime lastResetAt;

    @Column(name = "next_reset_at")
    private OffsetDateTime nextResetAt;

    @Column(name = "reset_items", columnDefinition = "JSONB")
    private String resetItems;
}

