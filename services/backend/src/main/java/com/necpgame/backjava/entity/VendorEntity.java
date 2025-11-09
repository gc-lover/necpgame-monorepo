package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * VendorEntity - С‚РѕСЂРіРѕРІРµС† (NPC РїСЂРѕРґР°РІРµС†).
 * 
 * РЎРїСЂР°РІРѕС‡РЅРёРє РІСЃРµС… С‚РѕСЂРіРѕРІС†РµРІ РІ РёРіСЂРµ СЃ РёРЅС„РѕСЂРјР°С†РёРµР№ Рѕ СЃРїРµС†РёР°Р»РёР·Р°С†РёРё Рё Р°СЃСЃРѕСЂС‚РёРјРµРЅС‚Рµ.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/trading/trading.yaml (Vendor schema)
 */
@Entity
@Table(name = "vendors", indexes = {
    @Index(name = "idx_vendors_location", columnList = "location_id"),
    @Index(name = "idx_vendors_type", columnList = "vendor_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "location_id", length = 100)
    private String locationId;

    @Column(name = "vendor_type", nullable = false, length = 50)
    private String vendorType; // weapons, armor, cyberware, general, black_market

    @Column(name = "reputation_required")
    private Integer reputationRequired = 0;

    @Column(name = "discount_percent")
    private Integer discountPercent = 0;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

