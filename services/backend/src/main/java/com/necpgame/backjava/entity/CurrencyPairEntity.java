package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currency_pairs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyPairEntity {

    @Id
    @Column(name = "pair", length = 32, nullable = false)
    private String pair;

    @Column(name = "base", length = 16, nullable = false)
    private String base;

    @Column(name = "quote", length = 16, nullable = false)
    private String quote;

    @Enumerated(EnumType.STRING)
    @Column(name = "pair_type", length = 16, nullable = false)
    private PairType pairType;

    @Column(name = "min_trade_amount", precision = 14, scale = 4)
    private BigDecimal minTradeAmount;

    @Column(name = "max_leverage")
    private Integer maxLeverage;

    @Column(name = "commission_rate")
    private Double commissionRate;

    public enum PairType {
        MAJOR,
        MINOR,
        EXOTIC
    }
}


