package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * InitialStateResponse - РЅР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹
 */
@Schema(description = "РќР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹")
public class InitialStateResponse {

    @JsonProperty("location")
    private GameLocation location;

    @JsonProperty("availableNPCs")
    private List<GameNPC> availableNPCs = new ArrayList<>();

    @JsonProperty("firstQuest")
    private GameQuest firstQuest;

    @JsonProperty("availableActions")
    private List<GameAction> availableActions = new ArrayList<>();

    @Schema(description = "РўРµРєСѓС‰Р°СЏ Р»РѕРєР°С†РёСЏ", required = true)
    @NotNull
    @Valid
    public GameLocation getLocation() {
        return location;
    }

    public void setLocation(GameLocation location) {
        this.location = location;
    }

    @Schema(description = "РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… NPC РІ С‚РµРєСѓС‰РµР№ Р»РѕРєР°С†РёРё", required = true)
    @NotNull
    @Valid
    public List<GameNPC> getAvailableNPCs() {
        return availableNPCs;
    }

    public void setAvailableNPCs(List<GameNPC> availableNPCs) {
        this.availableNPCs = availableNPCs;
    }

    @Schema(description = "РџРµСЂРІС‹Р№ РєРІРµСЃС‚", required = true)
    @NotNull
    @Valid
    public GameQuest getFirstQuest() {
        return firstQuest;
    }

    public void setFirstQuest(GameQuest firstQuest) {
        this.firstQuest = firstQuest;
    }

    @Schema(description = "РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… РґРµР№СЃС‚РІРёР№ РІ Р»РѕРєР°С†РёРё", required = true)
    @NotNull
    @Valid
    public List<GameAction> getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(List<GameAction> availableActions) {
        this.availableActions = availableActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitialStateResponse that = (InitialStateResponse) o;
        return Objects.equals(location, that.location) &&
               Objects.equals(availableNPCs, that.availableNPCs) &&
               Objects.equals(firstQuest, that.firstQuest) &&
               Objects.equals(availableActions, that.availableActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, availableNPCs, firstQuest, availableActions);
    }

    @Override
    public String toString() {
        return "InitialStateResponse{" +
                "location=" + location +
                ", availableNPCs=" + availableNPCs +
                ", firstQuest=" + firstQuest +
                ", availableActions=" + availableActions +
                '}';
    }
}

