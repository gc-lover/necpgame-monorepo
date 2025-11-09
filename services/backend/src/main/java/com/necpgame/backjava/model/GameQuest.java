package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Objects;

/**
 * GameQuest - РєРІРµСЃС‚ РІ РёРіСЂРµ
 */
@Schema(description = "РљРІРµСЃС‚ РІ РёРіСЂРµ")
public class GameQuest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("type")
    private TypeEnum type;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("giverNpcId")
    private String giverNpcId;

    @JsonProperty("rewards")
    private GameQuestRewards rewards;

    /**
     * РўРёРї РєРІРµСЃС‚Р°
     */
    public enum TypeEnum {
        MAIN("main"),
        SIDE("side"),
        CONTRACT("contract");

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

    @Schema(description = "РЈРЅРёРєР°Р»СЊРЅС‹Р№ РёРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РєРІРµСЃС‚Р°", required = true)
    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Schema(description = "РќР°Р·РІР°РЅРёРµ РєРІРµСЃС‚Р°", required = true)
    @NotNull
    @Size(min = 1, max = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Schema(description = "РћРїРёСЃР°РЅРёРµ РєРІРµСЃС‚Р°", required = true)
    @NotNull
    @Size(min = 10, max = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Schema(description = "РўРёРї РєРІРµСЃС‚Р°")
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    @Schema(description = "Р РµРєРѕРјРµРЅРґСѓРµРјС‹Р№ СѓСЂРѕРІРµРЅСЊ РґР»СЏ РєРІРµСЃС‚Р°", required = true)
    @NotNull
    @Min(1)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Schema(description = "ID NPC, РґР°СЋС‰РµРіРѕ РєРІРµСЃС‚", required = true)
    @NotNull
    public String getGiverNpcId() {
        return giverNpcId;
    }

    public void setGiverNpcId(String giverNpcId) {
        this.giverNpcId = giverNpcId;
    }

    @Schema(description = "РќР°РіСЂР°РґС‹ Р·Р° РєРІРµСЃС‚", required = true)
    @NotNull
    @Valid
    public GameQuestRewards getRewards() {
        return rewards;
    }

    public void setRewards(GameQuestRewards rewards) {
        this.rewards = rewards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameQuest gameQuest = (GameQuest) o;
        return Objects.equals(id, gameQuest.id) &&
               Objects.equals(name, gameQuest.name) &&
               Objects.equals(description, gameQuest.description) &&
               Objects.equals(type, gameQuest.type) &&
               Objects.equals(level, gameQuest.level) &&
               Objects.equals(giverNpcId, gameQuest.giverNpcId) &&
               Objects.equals(rewards, gameQuest.rewards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, type, level, giverNpcId, rewards);
    }

    @Override
    public String toString() {
        return "GameQuest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", level=" + level +
                ", giverNpcId='" + giverNpcId + '\'' +
                ", rewards=" + rewards +
                '}';
    }
}

