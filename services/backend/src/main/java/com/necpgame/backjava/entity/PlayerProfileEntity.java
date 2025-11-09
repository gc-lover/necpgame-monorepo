package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "player_profiles")
@NoArgsConstructor
public class PlayerProfileEntity {

    @Id
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "premium_currency", nullable = false)
    private Integer premiumCurrency;

    @Column(name = "total_playtime_seconds", nullable = false)
    private Integer totalPlaytimeSeconds;

    @Column(name = "language", nullable = false, length = 16)
    private String language;

    @Column(name = "timezone", nullable = false, length = 64)
    private String timezone;

    @Column(name = "settings_ui_json")
    private String settingsUiJson;

    @Column(name = "settings_audio_json")
    private String settingsAudioJson;

    @Column(name = "settings_graphics_json")
    private String settingsGraphicsJson;

    @Column(name = "friends_json")
    private String friendsJson;

    @Column(name = "blocked_json")
    private String blockedJson;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
