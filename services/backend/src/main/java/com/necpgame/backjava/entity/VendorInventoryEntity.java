package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * VendorInventoryEntity - РёРЅРІРµРЅС‚Р°СЂСЊ С‚РѕСЂРіРѕРІС†Р° (Р°СЃСЃРѕСЂС‚РёРјРµРЅС‚ С‚РѕРІР°СЂРѕРІ).
 * 
 * РЎРІСЏР·СЊ РјРµР¶РґСѓ С‚РѕСЂРіРѕРІС†Р°РјРё Рё РїСЂРµРґРјРµС‚Р°РјРё, РєРѕС‚РѕСЂС‹Рµ РѕРЅРё РїСЂРѕРґР°СЋС‚.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/trading/trading.yaml (VendorInventory schema)
 */
@Entity
@Table(name = "vendor_inventory", indexes = {
    @Index(name = "idx_vendor_inventory_vendor", columnList = "vendor_id"),
    @Index(name = "idx_vendor_inventory_item", columnList = "item_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "vendor_id", nullable = false, length = 100)
    private String vendorId;

    @Column(name = "item_id", nullable = false, length = 100)
    private String itemId;

    @Column(name = "stock_quantity")
    private Integer stockQuantity; // null = Р±РµР·Р»РёРјРёС‚РЅС‹Р№ Р·Р°РїР°СЃ

    @Column(name = "price", nullable = false)
    private Integer price; // С†РµРЅР° РІ СЌРґРґРёСЃР°С… (eddies)

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", insertable = false, updatable = false)
    private VendorEntity vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private InventoryItemEntity item;
}

