package com.necpgame.backjava.entity.mvp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mvp_world_state", indexes = {
    @Index(name = "idx_mvp_world_state_created_at", columnList = "captured_at")
})
public class MvpWorldStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "captured_at", nullable = false)
    private OffsetDateTime capturedAt;

    @Column(name = "time_of_day", nullable = false, length = 32)
    private String timeOfDay;

    @Column(name = "weather", nullable = false, length = 64)
    private String weather;

    @OneToMany(mappedBy = "worldState", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MvpWorldStateEventEntity> activeEvents = new ArrayList<>();
}
