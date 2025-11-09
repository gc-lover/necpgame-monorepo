package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.VoiceParticipantAudioQuality;
import com.necpgame.backjava.entity.enums.VoiceParticipantStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "voice_participant", indexes = {
        @Index(name = "idx_voice_participant_channel", columnList = "channel_id"),
        @Index(name = "idx_voice_participant_player", columnList = "player_id")
}, uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "uk_voice_participant_channel_player", columnNames = {"channel_id", "player_id"})
})
public class VoiceParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private VoiceChannelEntity channel;

    @Column(name = "player_id", nullable = false, length = 64)
    private String playerId;

    @Column(name = "display_name", length = 128)
    private String displayName;

    @Column(name = "role", length = 64)
    private String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private VoiceParticipantStatus status;

    @Column(name = "is_muted", nullable = false)
    private boolean muted;

    @Column(name = "mute_reason", length = 256)
    private String muteReason;

    @Column(name = "mute_moderator_id", length = 64)
    private String muteModeratorId;

    @Column(name = "mute_expires_at")
    private OffsetDateTime muteExpiresAt;

    @Column(name = "is_deafened", nullable = false)
    private boolean deafened;

    @Column(name = "is_speaking")
    private Boolean speaking;

    @Enumerated(EnumType.STRING)
    @Column(name = "audio_quality", length = 32)
    private VoiceParticipantAudioQuality audioQuality;

    @Column(name = "joined_at", nullable = false)
    private OffsetDateTime joinedAt;

    @Column(name = "left_at")
    private OffsetDateTime leftAt;

    @Column(name = "connection_id", length = 128)
    private String connectionId;

    @Column(name = "world_id", length = 64)
    private String worldId;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "position_z")
    private Double positionZ;

    @Column(name = "velocity_x")
    private Double velocityX;

    @Column(name = "velocity_y")
    private Double velocityY;

    @Column(name = "velocity_z")
    private Double velocityZ;

    @Column(name = "last_proximity_update")
    private OffsetDateTime lastProximityUpdate;

    @Column(name = "last_heartbeat_at")
    private OffsetDateTime lastHeartbeatAt;
}

