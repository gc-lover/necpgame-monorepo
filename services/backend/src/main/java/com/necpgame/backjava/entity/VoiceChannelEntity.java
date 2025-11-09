package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.VoiceChannelOwnerType;
import com.necpgame.backjava.entity.enums.VoiceChannelStatus;
import com.necpgame.backjava.entity.enums.VoiceChannelType;
import com.necpgame.backjava.entity.enums.VoiceQualityPreset;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "voice_channel", indexes = {
        @Index(name = "idx_voice_channel_owner", columnList = "owner_type, owner_id"),
        @Index(name = "idx_voice_channel_status", columnList = "status")
})
public class VoiceChannelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "channel_name", nullable = false, length = 128)
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false, length = 32)
    private VoiceChannelType channelType;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 32)
    private VoiceChannelOwnerType ownerType;

    @Column(name = "owner_id", nullable = false, length = 64)
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private VoiceChannelStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality_preset", nullable = false, length = 32)
    private VoiceQualityPreset qualityPreset;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "active_participants", nullable = false)
    private Integer activeParticipants;

    @Column(name = "description", length = 512)
    private String description;

    @Embedded
    private VoiceChannelPermissionsEmbeddable permissions;

    @Embedded
    private VoiceProximityEmbeddable proximity;

    @ElementCollection(fetch = FetchType.LAZY)
    @jakarta.persistence.CollectionTable(name = "voice_channel_allowed_role", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "role", length = 64)
    private Set<String> allowedRoles = new HashSet<>();

    @Column(name = "max_bitrate_kbps")
    private Integer maxBitrateKbps;

    @Column(name = "auto_close_minutes")
    private Integer autoCloseMinutes;

    @Column(name = "sample_rate_hz")
    private Integer sampleRateHz;

    @Column(name = "quality_max_participants")
    private Integer qualityMaxParticipants;

    @ElementCollection(fetch = FetchType.LAZY)
    @jakarta.persistence.CollectionTable(name = "voice_channel_recommended_device", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "device_name", length = 128)
    private List<String> recommendedDevices = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @Version
    private long version;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VoiceParticipantEntity> participants = new HashSet<>();

    public void addParticipant(VoiceParticipantEntity participant) {
        participants.add(participant);
        participant.setChannel(this);
    }

    public void removeParticipant(VoiceParticipantEntity participant) {
        participants.remove(participant);
        participant.setChannel(null);
    }
}

