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
public class PlayerTitleId implements Serializable {

    @Column(name = "player_id", nullable = false, columnDefinition = "UUID")
    private UUID playerId;

    @Column(name = "title_id", nullable = false, columnDefinition = "UUID")
    private UUID titleId;
}

