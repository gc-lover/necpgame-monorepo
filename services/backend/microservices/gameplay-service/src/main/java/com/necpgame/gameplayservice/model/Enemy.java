package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Enemy
 */


public class Enemy {

  private @Nullable String enemyId;

  private @Nullable String name;

  private @Nullable String type;

  private @Nullable Integer level;

  private @Nullable Object stats;

  private @Nullable Object aiBehavior;

  /**
   * Gets or Sets morale
   */
  public enum MoraleEnum {
    HIGH("high"),
    
    NORMAL("normal"),
    
    LOW("low"),
    
    BROKEN("broken");

    private final String value;

    MoraleEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static MoraleEnum fromValue(String value) {
      for (MoraleEnum b : MoraleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MoraleEnum morale;

  public Enemy enemyId(@Nullable String enemyId) {
    this.enemyId = enemyId;
    return this;
  }

  /**
   * Get enemyId
   * @return enemyId
   */
  
  @Schema(name = "enemy_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enemy_id")
  public @Nullable String getEnemyId() {
    return enemyId;
  }

  public void setEnemyId(@Nullable String enemyId) {
    this.enemyId = enemyId;
  }

  public Enemy name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Enemy type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public Enemy level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public Enemy stats(@Nullable Object stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stats")
  public @Nullable Object getStats() {
    return stats;
  }

  public void setStats(@Nullable Object stats) {
    this.stats = stats;
  }

  public Enemy aiBehavior(@Nullable Object aiBehavior) {
    this.aiBehavior = aiBehavior;
    return this;
  }

  /**
   * Get aiBehavior
   * @return aiBehavior
   */
  
  @Schema(name = "ai_behavior", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ai_behavior")
  public @Nullable Object getAiBehavior() {
    return aiBehavior;
  }

  public void setAiBehavior(@Nullable Object aiBehavior) {
    this.aiBehavior = aiBehavior;
  }

  public Enemy morale(@Nullable MoraleEnum morale) {
    this.morale = morale;
    return this;
  }

  /**
   * Get morale
   * @return morale
   */
  
  @Schema(name = "morale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("morale")
  public @Nullable MoraleEnum getMorale() {
    return morale;
  }

  public void setMorale(@Nullable MoraleEnum morale) {
    this.morale = morale;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Enemy enemy = (Enemy) o;
    return Objects.equals(this.enemyId, enemy.enemyId) &&
        Objects.equals(this.name, enemy.name) &&
        Objects.equals(this.type, enemy.type) &&
        Objects.equals(this.level, enemy.level) &&
        Objects.equals(this.stats, enemy.stats) &&
        Objects.equals(this.aiBehavior, enemy.aiBehavior) &&
        Objects.equals(this.morale, enemy.morale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enemyId, name, type, level, stats, aiBehavior, morale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Enemy {\n");
    sb.append("    enemyId: ").append(toIndentedString(enemyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
    sb.append("    aiBehavior: ").append(toIndentedString(aiBehavior)).append("\n");
    sb.append("    morale: ").append(toIndentedString(morale)).append("\n");
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

