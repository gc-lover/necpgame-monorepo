package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "currency_exchange_orders", indexes = {
    @Index(name = "idx_currency_exchange_orders_character", columnList = "character_id"),
    @Index(name = "idx_currency_exchange_orders_pair", columnList = "pair"),
    @Index(name = "idx_currency_exchange_orders_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "pair", length = 32, nullable = false)
    private String pair;

    @Enumerated(EnumType.STRING)
    @Column(name = "side", length = 8, nullable = false)
    private OrderSide side;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 16, nullable = false)
    private OrderType orderType;

    @Column(name = "limit_price")
    private Double limitPrice;

    @Column(name = "leverage")
    private Integer leverage;

    @Column(name = "stop_loss")
    private Double stopLoss;

    @Column(name = "take_profit")
    private Double takeProfit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 24, nullable = false)
    private OrderStatus status;

    @Column(name = "filled_amount")
    private Double filledAmount;

    @Column(name = "average_price")
    private Double averagePrice;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "filled_at")
    private LocalDateTime filledAt;

    public enum OrderSide {
        BUY,
        SELL
    }

    public enum OrderType {
        MARKET,
        LIMIT
    }

    public enum OrderStatus {
        PENDING,
        FILLED,
        PARTIALLY_FILLED,
        CANCELLED,
        EXPIRED
    }
}


