package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameCharacterState
 */


public class GameCharacterState {

  private Integer health;

  private Integer energy;

  private Integer humanity;

  private Integer money;

  private Integer level;

  private @Nullable Integer experience;

  public GameCharacterState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterState(Integer health, Integer energy, Integer humanity, Integer money, Integer level) {
    this.health = health;
    this.energy = energy;
    this.humanity = humanity;
    this.money = money;
    this.level = level;
  }

  public GameCharacterState health(Integer health) {
    this.health = health;
    return this;
  }

  /**
   * Здоровье персонажа
   * minimum: 0
   * maximum: 100
   * @return health
   */
  @NotNull @Min(value = 0) @Max(value = 100) 
  @Schema(name = "health", example = "100", description = "Здоровье персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("health")
  public Integer getHealth() {
    return health;
  }

  public void setHealth(Integer health) {
    this.health = health;
  }

  public GameCharacterState energy(Integer energy) {
    this.energy = energy;
    return this;
  }

  /**
   * Энергия персонажа
   * minimum: 0
   * maximum: 100
   * @return energy
   */
  @NotNull @Min(value = 0) @Max(value = 100) 
  @Schema(name = "energy", example = "100", description = "Энергия персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("energy")
  public Integer getEnergy() {
    return energy;
  }

  public void setEnergy(Integer energy) {
    this.energy = energy;
  }

  public GameCharacterState humanity(Integer humanity) {
    this.humanity = humanity;
    return this;
  }

  /**
   * Человечность персонажа
   * minimum: 0
   * maximum: 100
   * @return humanity
   */
  @NotNull @Min(value = 0) @Max(value = 100) 
  @Schema(name = "humanity", example = "100", description = "Человечность персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("humanity")
  public Integer getHumanity() {
    return humanity;
  }

  public void setHumanity(Integer humanity) {
    this.humanity = humanity;
  }

  public GameCharacterState money(Integer money) {
    this.money = money;
    return this;
  }

  /**
   * Деньги (eddies)
   * minimum: 0
   * @return money
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "money", example = "500", description = "Деньги (eddies)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("money")
  public Integer getMoney() {
    return money;
  }

  public void setMoney(Integer money) {
    this.money = money;
  }

  public GameCharacterState level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Уровень персонажа
   * minimum: 1
   * @return level
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "level", example = "1", description = "Уровень персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public GameCharacterState experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Опыт персонажа
   * minimum: 0
   * @return experience
   */
  @Min(value = 0) 
  @Schema(name = "experience", example = "0", description = "Опыт персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterState gameCharacterState = (GameCharacterState) o;
    return Objects.equals(this.health, gameCharacterState.health) &&
        Objects.equals(this.energy, gameCharacterState.energy) &&
        Objects.equals(this.humanity, gameCharacterState.humanity) &&
        Objects.equals(this.money, gameCharacterState.money) &&
        Objects.equals(this.level, gameCharacterState.level) &&
        Objects.equals(this.experience, gameCharacterState.experience);
  }

  @Override
  public int hashCode() {
    return Objects.hash(health, energy, humanity, money, level, experience);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacterState {\n");
    sb.append("    health: ").append(toIndentedString(health)).append("\n");
    sb.append("    energy: ").append(toIndentedString(energy)).append("\n");
    sb.append("    humanity: ").append(toIndentedString(humanity)).append("\n");
    sb.append("    money: ").append(toIndentedString(money)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

