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
@Table(name = "currency_pair_rates", indexes = {
    @Index(name = "idx_currency_pair_rates_pair", columnList = "pair"),
    @Index(name = "idx_currency_pair_rates_timestamp", columnList = "timestamp")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyPairRateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pair", length = 32, nullable = false)
    private String pair;

    @Column(name = "base_currency", length = 16, nullable = false)
    private String baseCurrency;

    @Column(name = "quote_currency", length = 16, nullable = false)
    private String quoteCurrency;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "bid")
    private Double bid;

    @Column(name = "ask")
    private Double ask;

    @Column(name = "spread")
    private Double spread;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}


