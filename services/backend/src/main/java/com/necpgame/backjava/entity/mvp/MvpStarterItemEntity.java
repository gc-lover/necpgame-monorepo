package com.necpgame.backjava.entity.mvp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mvp_starter_items", indexes = {
    @Index(name = "idx_mvp_starter_items_item", columnList = "item_id")
})
public class MvpStarterItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
