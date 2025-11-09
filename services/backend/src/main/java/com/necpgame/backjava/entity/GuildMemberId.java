package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildMemberId implements Serializable {

    @Column(name = "guild_id", nullable = false, columnDefinition = "UUID")
    private UUID guildId;

    @Column(name = "character_id", nullable = false, columnDefinition = "UUID")
    private UUID characterId;
}

