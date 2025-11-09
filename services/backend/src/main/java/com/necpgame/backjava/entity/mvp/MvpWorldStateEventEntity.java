package com.necpgame.backjava.entity.mvp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mvp_world_state_events", indexes = {
    @Index(name = "idx_mvp_world_state_events_state", columnList = "world_state_id")
})
public class MvpWorldStateEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_state_id", nullable = false)
    private MvpWorldStateEntity worldState;

    @Column(name = "event_name", nullable = false, length = 150)
    private String eventName;

    @Column(name = "severity", nullable = false, length = 32)
    private String severity;
}
