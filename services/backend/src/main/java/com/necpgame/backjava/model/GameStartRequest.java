package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;
import java.util.UUID;

/**
 * GameStartRequest - Р·Р°РїСЂРѕСЃ РЅР° РЅР°С‡Р°Р»Рѕ РёРіСЂС‹
 */
@Schema(description = "Р—Р°РїСЂРѕСЃ РЅР° РЅР°С‡Р°Р»Рѕ РёРіСЂС‹")
public class GameStartRequest {

    @JsonProperty("characterId")
    private UUID characterId;

    @JsonProperty("skipTutorial")
    private Boolean skipTutorial = false;

    /**
     * ID СЃРѕР·РґР°РЅРЅРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р°
     */
    @Schema(description = "ID СЃРѕР·РґР°РЅРЅРѕРіРѕ РїРµСЂСЃРѕРЅР°Р¶Р°", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    @NotNull
    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    /**
     * РџСЂРѕРїСѓСЃС‚РёС‚СЊ С‚СѓС‚РѕСЂРёР°Р»
     */
    @Schema(description = "РџСЂРѕРїСѓСЃС‚РёС‚СЊ С‚СѓС‚РѕСЂРёР°Р»", example = "false")
    public Boolean getSkipTutorial() {
        return skipTutorial;
    }

    public void setSkipTutorial(Boolean skipTutorial) {
        this.skipTutorial = skipTutorial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameStartRequest that = (GameStartRequest) o;
        return Objects.equals(characterId, that.characterId) &&
               Objects.equals(skipTutorial, that.skipTutorial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, skipTutorial);
    }

    @Override
    public String toString() {
        return "GameStartRequest{" +
                "characterId=" + characterId +
                ", skipTutorial=" + skipTutorial +
                '}';
    }
}

