package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Entity
@Table(name = "character_slot_states")
@NoArgsConstructor
public class CharacterSlotStateEntity {

    @Id
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "total_slots", nullable = false)
    private Integer totalSlots;

    @Column(name = "used_slots", nullable = false)
    private Integer usedSlots;

    @Column(name = "premium_slots_purchased", nullable = false)
    private Integer premiumSlotsPurchased;

    @Column(name = "max_slots", nullable = false)
    private Integer maxSlots;

    @Column(name = "next_tier_currency", length = 32)
    private String nextTierCurrency;

    @Column(name = "next_tier_amount")
    private Integer nextTierAmount;

    @Column(name = "next_tier_requires_approval")
    private Boolean nextTierRequiresApproval;

    @Column(name = "restrictions_json")
    private String restrictionsJson;

    @Column(name = "active_character_id")
    private UUID activeCharacterId;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
