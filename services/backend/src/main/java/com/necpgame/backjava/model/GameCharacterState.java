package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;

/**
 * GameCharacterState - СЃРѕСЃС‚РѕСЏРЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р° РІ РёРіСЂРµ
 */
@Schema(description = "РЎРѕСЃС‚РѕСЏРЅРёРµ РїРµСЂСЃРѕРЅР°Р¶Р° РІ РёРіСЂРµ")
public class GameCharacterState {

    @JsonProperty("health")
    private Integer health;

    @JsonProperty("energy")
    private Integer energy;

    @JsonProperty("humanity")
    private Integer humanity;

    @JsonProperty("money")
    private Integer money;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("experience")
    private Integer experience;

    @Schema(description = "Р—РґРѕСЂРѕРІСЊРµ РїРµСЂСЃРѕРЅР°Р¶Р°", example = "100", required = true)
    @NotNull
    @Min(0)
    @Max(100)
    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    @Schema(description = "Р­РЅРµСЂРіРёСЏ РїРµСЂСЃРѕРЅР°Р¶Р°", example = "100", required = true)
    @NotNull
    @Min(0)
    @Max(100)
    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }

    @Schema(description = "Р§РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ РїРµСЂСЃРѕРЅР°Р¶Р°", example = "100", required = true)
    @NotNull
    @Min(0)
    @Max(100)
    public Integer getHumanity() {
        return humanity;
    }

    public void setHumanity(Integer humanity) {
        this.humanity = humanity;
    }

    @Schema(description = "Р”РµРЅСЊРіРё (eddies)", example = "500", required = true)
    @NotNull
    @Min(0)
    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    @Schema(description = "РЈСЂРѕРІРµРЅСЊ РїРµСЂСЃРѕРЅР°Р¶Р°", example = "1", required = true)
    @NotNull
    @Min(1)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Schema(description = "РћРїС‹С‚ РїРµСЂСЃРѕРЅР°Р¶Р°", example = "0")
    @Min(0)
    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacterState that = (GameCharacterState) o;
        return Objects.equals(health, that.health) &&
               Objects.equals(energy, that.energy) &&
               Objects.equals(humanity, that.humanity) &&
               Objects.equals(money, that.money) &&
               Objects.equals(level, that.level) &&
               Objects.equals(experience, that.experience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(health, energy, humanity, money, level, experience);
    }

    @Override
    public String toString() {
        return "GameCharacterState{" +
                "health=" + health +
                ", energy=" + energy +
                ", humanity=" + humanity +
                ", money=" + money +
                ", level=" + level +
                ", experience=" + experience +
                '}';
    }
}

