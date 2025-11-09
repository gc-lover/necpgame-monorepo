package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "player_reset_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResetStatusEntity {

    @Id
    @Column(name = "player_id", nullable = false, columnDefinition = "UUID")
    private UUID playerId;

    @Column(name = "last_daily_reset")
    private OffsetDateTime lastDailyReset;

    @Column(name = "last_weekly_reset")
    private OffsetDateTime lastWeeklyReset;

    @Column(name = "daily_details", columnDefinition = "JSONB")
    private String dailyDetails;

    @Column(name = "weekly_details", columnDefinition = "JSONB")
    private String weeklyDetails;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

