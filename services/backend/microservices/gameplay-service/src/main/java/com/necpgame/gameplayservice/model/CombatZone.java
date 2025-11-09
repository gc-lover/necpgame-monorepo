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
 * CombatZone
 */


public class CombatZone {

  private @Nullable String zoneId;

  private @Nullable String name;

  /**
   * Gets or Sets zoneType
   */
  public enum ZoneTypeEnum {
    PVP("pvp"),
    
    PVE("pve"),
    
    SAFE("safe"),
    
    CONDITIONAL_PVP("conditional_pvp"),
    
    MIXED("mixed");

    private final String value;

    ZoneTypeEnum(String value) {
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
    public static ZoneTypeEnum fromValue(String value) {
      for (ZoneTypeEnum b : ZoneTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ZoneTypeEnum zoneType;

  private @Nullable String description;

  private @Nullable Integer playerCount;

  private @Nullable Integer maxPlayers;

  public CombatZone zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zone_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_id")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public CombatZone name(@Nullable String name) {
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

  public CombatZone zoneType(@Nullable ZoneTypeEnum zoneType) {
    this.zoneType = zoneType;
    return this;
  }

  /**
   * Get zoneType
   * @return zoneType
   */
  
  @Schema(name = "zone_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_type")
  public @Nullable ZoneTypeEnum getZoneType() {
    return zoneType;
  }

  public void setZoneType(@Nullable ZoneTypeEnum zoneType) {
    this.zoneType = zoneType;
  }

  public CombatZone description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CombatZone playerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
    return this;
  }

  /**
   * Get playerCount
   * @return playerCount
   */
  
  @Schema(name = "player_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_count")
  public @Nullable Integer getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
  }

  public CombatZone maxPlayers(@Nullable Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  /**
   * Get maxPlayers
   * @return maxPlayers
   */
  
  @Schema(name = "max_players", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_players")
  public @Nullable Integer getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(@Nullable Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatZone combatZone = (CombatZone) o;
    return Objects.equals(this.zoneId, combatZone.zoneId) &&
        Objects.equals(this.name, combatZone.name) &&
        Objects.equals(this.zoneType, combatZone.zoneType) &&
        Objects.equals(this.description, combatZone.description) &&
        Objects.equals(this.playerCount, combatZone.playerCount) &&
        Objects.equals(this.maxPlayers, combatZone.maxPlayers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, name, zoneType, description, playerCount, maxPlayers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatZone {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    zoneType: ").append(toIndentedString(zoneType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
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

