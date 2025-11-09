package com.necpgame.backjava.entity;

import com.necpgame.backjava.converter.JsonMapConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "players",
    indexes = {
        @Index(name = "idx_players_account_id", columnList = "account_id", unique = true),
        @Index(name = "idx_players_active_character", columnList = "active_character_id")
    },
    uniqueConstraints = @UniqueConstraint(name = "uc_players_account", columnNames = "account_id")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(name = "premium_currency", nullable = false)
    private Long premiumCurrency = 0L;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "settings", columnDefinition = "JSONB", nullable = false)
    private Map<String, Object> settings = new HashMap<>();

    @Column(name = "active_character_id")
    private UUID activeCharacterId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CharacterSlotEntity> slots = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlayerBankSlotEntity> bankSlots = new ArrayList<>();

    public void addSlot(CharacterSlotEntity slot) {
        slots.add(slot);
        slot.setPlayer(this);
    }

    public void removeSlot(CharacterSlotEntity slot) {
        if (slots.remove(slot)) {
            slot.setPlayer(null);
        }
    }

    public void addBankSlot(PlayerBankSlotEntity slot) {
        bankSlots.add(slot);
        slot.setPlayer(this);
    }

    public void removeBankSlot(PlayerBankSlotEntity slot) {
        if (bankSlots.remove(slot)) {
            slot.setPlayer(null);
        }
    }
}

