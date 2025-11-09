package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * InventoryItemEntity - СЃРїСЂР°РІРѕС‡РЅРёРє РїСЂРµРґРјРµС‚РѕРІ РІ РёРіСЂРµ.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ РІСЃРµС… РїСЂРµРґРјРµС‚Р°С… (РѕСЂСѓР¶РёРµ, Р±СЂРѕРЅСЏ, РёРјРїР»Р°РЅС‚С‹, СЂР°СЃС…РѕРґРЅРёРєРё).
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/inventory/inventory.yaml (InventoryItem schema)
 */
@Entity
@Table(name = "inventory_items", indexes = {
    @Index(name = "idx_inventory_items_category", columnList = "category"),
    @Index(name = "idx_inventory_items_rarity", columnList = "rarity"),
    @Index(name = "idx_inventory_items_equippable", columnList = "equippable"),
    @Index(name = "idx_inventory_items_usable", columnList = "usable")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "category", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Column(name = "weight", nullable = false, precision = 8, scale = 2)
    private BigDecimal weight;

    @Column(name = "stackable", nullable = false)
    private Boolean stackable = false;

    @Column(name = "max_stack_size")
    private Integer maxStackSize = 1;

    @Column(name = "rarity", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ItemRarity rarity = ItemRarity.COMMON;

    @Column(name = "value", nullable = false)
    private Integer value = 0;

    @Column(name = "equippable", nullable = false)
    private Boolean equippable = false;

    @Column(name = "usable", nullable = false)
    private Boolean usable = false;

    @Column(name = "quest_item", nullable = false)
    private Boolean questItem = false;

    // Requirements for using/equipping
    @Column(name = "min_level")
    private Integer minLevel;

    @Column(name = "min_strength")
    private Integer minStrength;

    @Column(name = "min_dexterity")
    private Integer minDexterity;

    @Column(name = "min_intelligence")
    private Integer minIntelligence;

    // Equipment slot type (if equippable)
    @Column(name = "slot_type", length = 50)
    private String slotType;

    // Effects when used (JSON)
    @Column(name = "use_effects", length = 1000)
    private String useEffects;

    // Bonuses when equipped (JSON)
    @Column(name = "equip_bonuses", length = 1000)
    private String equipBonuses;

    @Column(name = "bind_on_pickup", nullable = false)
    private Boolean bindOnPickup = false;

    @Column(name = "bind_on_equip", nullable = false)
    private Boolean bindOnEquip = false;

    @Column(name = "has_durability", nullable = false)
    private Boolean hasDurability = false;

    @Column(name = "base_durability")
    private Integer baseDurability;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * РљР°С‚РµРіРѕСЂРёСЏ РїСЂРµРґРјРµС‚Р° (РёР· OpenAPI - ItemCategory enum)
     */
    public enum ItemCategory {
        WEAPONS,
        ARMOR,
        IMPLANTS,
        CONSUMABLES,
        RESOURCES,
        QUEST_ITEMS,
        MISC
    }

    /**
     * Р РµРґРєРѕСЃС‚СЊ РїСЂРµРґРјРµС‚Р° (РёР· OpenAPI)
     */
    public enum ItemRarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY
    }
}

