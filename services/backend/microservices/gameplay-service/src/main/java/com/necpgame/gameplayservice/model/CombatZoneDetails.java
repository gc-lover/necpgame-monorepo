package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CombatZoneDetailsAllOfPveSettings;
import com.necpgame.gameplayservice.model.CombatZoneDetailsAllOfPvpRules;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CombatZoneDetails
 */


public class CombatZoneDetails {

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

  private @Nullable CombatZoneDetailsAllOfPvpRules pvpRules;

  private @Nullable CombatZoneDetailsAllOfPveSettings pveSettings;

  private @Nullable Object rewards;

  private @Nullable Object entryRequirements;

  public CombatZoneDetails zoneId(@Nullable String zoneId) {
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

  public CombatZoneDetails name(@Nullable String name) {
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

  public CombatZoneDetails zoneType(@Nullable ZoneTypeEnum zoneType) {
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

  public CombatZoneDetails description(@Nullable String description) {
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

  public CombatZoneDetails playerCount(@Nullable Integer playerCount) {
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

  public CombatZoneDetails maxPlayers(@Nullable Integer maxPlayers) {
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

  public CombatZoneDetails pvpRules(@Nullable CombatZoneDetailsAllOfPvpRules pvpRules) {
    this.pvpRules = pvpRules;
    return this;
  }

  /**
   * Get pvpRules
   * @return pvpRules
   */
  @Valid 
  @Schema(name = "pvp_rules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pvp_rules")
  public @Nullable CombatZoneDetailsAllOfPvpRules getPvpRules() {
    return pvpRules;
  }

  public void setPvpRules(@Nullable CombatZoneDetailsAllOfPvpRules pvpRules) {
    this.pvpRules = pvpRules;
  }

  public CombatZoneDetails pveSettings(@Nullable CombatZoneDetailsAllOfPveSettings pveSettings) {
    this.pveSettings = pveSettings;
    return this;
  }

  /**
   * Get pveSettings
   * @return pveSettings
   */
  @Valid 
  @Schema(name = "pve_settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pve_settings")
  public @Nullable CombatZoneDetailsAllOfPveSettings getPveSettings() {
    return pveSettings;
  }

  public void setPveSettings(@Nullable CombatZoneDetailsAllOfPveSettings pveSettings) {
    this.pveSettings = pveSettings;
  }

  public CombatZoneDetails rewards(@Nullable Object rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable Object getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable Object rewards) {
    this.rewards = rewards;
  }

  public CombatZoneDetails entryRequirements(@Nullable Object entryRequirements) {
    this.entryRequirements = entryRequirements;
    return this;
  }

  /**
   * Get entryRequirements
   * @return entryRequirements
   */
  
  @Schema(name = "entry_requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entry_requirements")
  public @Nullable Object getEntryRequirements() {
    return entryRequirements;
  }

  public void setEntryRequirements(@Nullable Object entryRequirements) {
    this.entryRequirements = entryRequirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatZoneDetails combatZoneDetails = (CombatZoneDetails) o;
    return Objects.equals(this.zoneId, combatZoneDetails.zoneId) &&
        Objects.equals(this.name, combatZoneDetails.name) &&
        Objects.equals(this.zoneType, combatZoneDetails.zoneType) &&
        Objects.equals(this.description, combatZoneDetails.description) &&
        Objects.equals(this.playerCount, combatZoneDetails.playerCount) &&
        Objects.equals(this.maxPlayers, combatZoneDetails.maxPlayers) &&
        Objects.equals(this.pvpRules, combatZoneDetails.pvpRules) &&
        Objects.equals(this.pveSettings, combatZoneDetails.pveSettings) &&
        Objects.equals(this.rewards, combatZoneDetails.rewards) &&
        Objects.equals(this.entryRequirements, combatZoneDetails.entryRequirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, name, zoneType, description, playerCount, maxPlayers, pvpRules, pveSettings, rewards, entryRequirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatZoneDetails {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    zoneType: ").append(toIndentedString(zoneType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
    sb.append("    pvpRules: ").append(toIndentedString(pvpRules)).append("\n");
    sb.append("    pveSettings: ").append(toIndentedString(pveSettings)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    entryRequirements: ").append(toIndentedString(entryRequirements)).append("\n");
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

