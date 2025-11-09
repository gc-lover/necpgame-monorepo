package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GameNPC - NPC РІ РёРіСЂРµ
 */
@Schema(description = "NPC РІ РёРіСЂРµ")
public class GameNPC {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private TypeEnum type;

    @JsonProperty("faction")
    private String faction;

    @JsonProperty("greeting")
    private String greeting;

    @JsonProperty("availableQuests")
    private List<String> availableQuests = new ArrayList<>();

    /**
     * РўРёРї NPC
     */
    public enum TypeEnum {
        TRADER("trader"),
        QUEST_GIVER("quest_giver"),
        CITIZEN("citizen"),
        ENEMY("enemy");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    @Schema(description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ NPC", required = true)
    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Schema(description = "РРјСЏ NPC", required = true)
    @NotNull
    @Size(min = 1, max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(description = "РћРїРёСЃР°РЅРёРµ NPC")
    @Size(max = 500)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Schema(description = "РўРёРї NPC", required = true)
    @NotNull
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    @Schema(description = "Р¤СЂР°РєС†РёСЏ NPC (РµСЃР»Рё РµСЃС‚СЊ)")
    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    @Schema(description = "РџСЂРёРІРµС‚СЃС‚РІРёРµ NPC РїСЂРё РїРµСЂРІРѕРј РІР·Р°РёРјРѕРґРµР№СЃС‚РІРёРё", required = true)
    @NotNull
    @Size(min = 10, max = 500)
    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    @Schema(description = "РЎРїРёСЃРѕРє ID РґРѕСЃС‚СѓРїРЅС‹С… РєРІРµСЃС‚РѕРІ РѕС‚ СЌС‚РѕРіРѕ NPC")
    public List<String> getAvailableQuests() {
        return availableQuests;
    }

    public void setAvailableQuests(List<String> availableQuests) {
        this.availableQuests = availableQuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameNPC gameNPC = (GameNPC) o;
        return Objects.equals(id, gameNPC.id) &&
               Objects.equals(name, gameNPC.name) &&
               Objects.equals(description, gameNPC.description) &&
               Objects.equals(type, gameNPC.type) &&
               Objects.equals(faction, gameNPC.faction) &&
               Objects.equals(greeting, gameNPC.greeting) &&
               Objects.equals(availableQuests, gameNPC.availableQuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, faction, greeting, availableQuests);
    }

    @Override
    public String toString() {
        return "GameNPC{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", faction='" + faction + '\'' +
                ", greeting='" + greeting + '\'' +
                ", availableQuests=" + availableQuests +
                '}';
    }
}

