package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.VoiceChannelOwnerType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "voice_channel_metric")
public class VoiceChannelMetricsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private VoiceChannelEntity channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 32)
    private VoiceChannelOwnerType ownerType;

    @Column(name = "owner_id", nullable = false, length = 64)
    private String ownerId;

    @Column(name = "latency_ms")
    private Double latencyMs;

    @Column(name = "packet_loss_percent")
    private Double packetLossPercent;

    @Column(name = "active_speakers")
    private Integer activeSpeakers;

    @Column(name = "average_speak_time_seconds")
    private Double averageSpeakTimeSeconds;

    @Column(name = "peak_concurrent")
    private Integer peakConcurrent;

    @Column(name = "active_participants")
    private Integer activeParticipants;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;
}




