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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "reset_schedule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetScheduleEntity {

    @Id
    @Column(name = "reset_type", length = 20, nullable = false)
    private String resetType;

    @Column(name = "cron_expression", length = 120)
    private String cronExpression;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "items_to_reset", columnDefinition = "JSONB")
    private String itemsToReset;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

