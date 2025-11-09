package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "currency_rate_history", indexes = {
    @Index(name = "idx_currency_rate_history_pair", columnList = "pair"),
    @Index(name = "idx_currency_rate_history_period", columnList = "period"),
    @Index(name = "idx_currency_rate_history_interval", columnList = "interval"),
    @Index(name = "idx_currency_rate_history_timestamp", columnList = "timestamp")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pair", length = 32, nullable = false)
    private String pair;

    @Column(name = "period", length = 16)
    private String period;

    @Column(name = "interval", length = 16)
    private String interval;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "open_rate")
    private Double openRate;

    @Column(name = "high_rate")
    private Double highRate;

    @Column(name = "low_rate")
    private Double lowRate;

    @Column(name = "close_rate")
    private Double closeRate;

    @Column(name = "volume")
    private Long volume;

    @CreationTimestamp
    @Column(name = "recorded_at", nullable = false, updatable = false)
    private LocalDateTime recordedAt;
}


