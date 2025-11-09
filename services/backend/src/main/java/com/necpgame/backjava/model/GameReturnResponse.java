package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * GameReturnResponse - РѕС‚РІРµС‚ РїСЂРё РІРѕР·РІСЂР°С‚Рµ РІ РёРіСЂСѓ
 */
@Schema(description = "РћС‚РІРµС‚ РїСЂРё РІРѕР·РІСЂР°С‚Рµ РІ РёРіСЂСѓ")
public class GameReturnResponse {

    @JsonProperty("gameSessionId")
    private UUID gameSessionId;

    @JsonProperty("characterId")
    private UUID characterId;

    @JsonProperty("currentLocation")
    private GameLocation currentLocation;

    @JsonProperty("characterState")
    private GameCharacterState characterState;

    @JsonProperty("activeQuests")
    private List<GameActiveQuest> activeQuests = new ArrayList<>();

    @Schema(description = "ID РёРіСЂРѕРІРѕР№ СЃРµСЃСЃРёРё", required = true)
    @NotNull
    public UUID getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(UUID gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    @Schema(description = "ID РїРµСЂСЃРѕРЅР°Р¶Р°", required = true)
    @NotNull
    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    @Schema(description = "РўРµРєСѓС‰Р°СЏ Р»РѕРєР°С†РёСЏ", required = true)
    @NotNull
    @Valid
    public GameLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GameLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Schema(description = "РЎРѕСЃС‚РѕСЏРЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р°", required = true)
    @NotNull
    @Valid
    public GameCharacterState getCharacterState() {
        return characterState;
    }

    public void setCharacterState(GameCharacterState characterState) {
        this.characterState = characterState;
    }

    @Schema(description = "РђРєС‚РёРІРЅС‹Рµ РєРІРµСЃС‚С‹")
    public List<GameActiveQuest> getActiveQuests() {
        return activeQuests;
    }

    public void setActiveQuests(List<GameActiveQuest> activeQuests) {
        this.activeQuests = activeQuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameReturnResponse that = (GameReturnResponse) o;
        return Objects.equals(gameSessionId, that.gameSessionId) &&
               Objects.equals(characterId, that.characterId) &&
               Objects.equals(currentLocation, that.currentLocation) &&
               Objects.equals(characterState, that.characterState) &&
               Objects.equals(activeQuests, that.activeQuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameSessionId, characterId, currentLocation, characterState, activeQuests);
    }

    @Override
    public String toString() {
        return "GameReturnResponse{" +
                "gameSessionId=" + gameSessionId +
                ", characterId=" + characterId +
                ", currentLocation=" + currentLocation +
                ", characterState=" + characterState +
                ", activeQuests=" + activeQuests +
                '}';
    }
}

