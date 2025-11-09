package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildMemberId implements Serializable {

    @Column(name = "guild_id", nullable = false)
    private UUID guildId;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TradingGuildMemberId that = (TradingGuildMemberId) o;
        return Objects.equals(guildId, that.guildId) && Objects.equals(characterId, that.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guildId, characterId);
    }
}

