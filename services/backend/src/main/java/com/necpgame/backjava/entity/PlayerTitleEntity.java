package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "player_titles", indexes = {
    @Index(name = "idx_player_titles_player", columnList = "player_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerTitleEntity {

    @EmbeddedId
    private PlayerTitleId id;

    @Column(name = "name", length = 120)
    private String name;

    @Column(name = "display_name", length = 160)
    private String displayName;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "rarity", length = 20)
    private String rarity;

    @Column(name = "unlocked_at")
    private OffsetDateTime unlockedAt;

    @Default
    @Column(name = "active", nullable = false)
    private boolean active = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

