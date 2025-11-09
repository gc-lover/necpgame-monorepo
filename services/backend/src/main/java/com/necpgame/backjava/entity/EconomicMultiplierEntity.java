package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "economic_multipliers")
public class EconomicMultiplierEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "price_multipliers_json", columnDefinition = "jsonb")
    private String priceMultipliersJson;

    @Column(name = "trade_restrictions_json", columnDefinition = "jsonb")
    private String tradeRestrictionsJson;

    @Column(name = "currency_exchange_rates_json", columnDefinition = "jsonb")
    private String currencyExchangeRatesJson;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

