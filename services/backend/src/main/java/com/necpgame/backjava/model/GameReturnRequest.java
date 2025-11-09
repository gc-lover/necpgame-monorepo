package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;
import java.util.UUID;

/**
 * GameReturnRequest - Р·Р°РїСЂРѕСЃ РЅР° РІРѕР·РІСЂР°С‚ РІ РёРіСЂСѓ
 */
@Schema(description = "Р—Р°РїСЂРѕСЃ РЅР° РІРѕР·РІСЂР°С‚ РІ РёРіСЂСѓ")
public class GameReturnRequest {

    @JsonProperty("characterId")
    private UUID characterId;

    /**
     * ID РїРµСЂСЃРѕРЅР°Р¶Р°
     */
    @Schema(description = "ID РїРµСЂСЃРѕРЅР°Р¶Р°", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull
    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameReturnRequest that = (GameReturnRequest) o;
        return Objects.equals(characterId, that.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId);
    }

    @Override
    public String toString() {
        return "GameReturnRequest{" +
                "characterId=" + characterId +
                '}';
    }
}

