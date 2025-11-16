package com.necpgame.workqueue.domain.item;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "item_mod_slots")
@Getter
@Setter
@NoArgsConstructor
public class ItemModSlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_entity_id")
    private ContentEntryEntity item;

    @Column(name = "slot_code", length = 64, nullable = false)
    private String slotCode;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadata;
}

