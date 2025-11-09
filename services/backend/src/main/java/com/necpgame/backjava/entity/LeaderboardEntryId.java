package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntryId implements Serializable {

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "player_id", nullable = false, columnDefinition = "UUID")
    private UUID playerId;
}

