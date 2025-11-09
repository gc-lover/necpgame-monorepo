package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CharacterInventoryEntity - РїСЂРµРґРјРµС‚С‹ РІ РёРЅРІРµРЅС‚Р°СЂРµ РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ РїСЂРµРґРјРµС‚Р°С… РІ РёРЅРІРµРЅС‚Р°СЂРµ РєР°Р¶РґРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р° (РєРѕР»РёС‡РµСЃС‚РІРѕ, РїРѕР·РёС†РёСЏ).
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/inventory/inventory.yaml
 */
@Entity
@Table(
    name = "character_inventory",
    indexes = {
        @Index(name = "idx_character_inventory_character", columnList = "character_id"),
        @Index(name = "idx_character_inventory_item", columnList = "item_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "item_id", nullable = false, length = 100)
    private String itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "slot_position")
    private Integer slotPosition;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type", nullable = false, length = 16)
    private StorageType storageType = StorageType.BACKPACK;

    @Column(name = "current_durability")
    private Integer currentDurability;

    @Column(name = "max_durability")
    private Integer maxDurability;

    @Column(name = "is_bound", nullable = false)
    private Boolean bound = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "bind_type", length = 20, nullable = false)
    private BindType bindType = BindType.NONE;

    @Column(name = "bound_at")
    private OffsetDateTime boundAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private InventoryItemEntity item;

    public enum StorageType {
        BACKPACK,
        EQUIPPED,
        BANK
    }

    public enum BindType {
        NONE,
        PICKUP,
        EQUIP,
        ACCOUNT
    }
}

