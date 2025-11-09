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
 * CyberspaceZone
 */


public class CyberspaceZone {

  private @Nullable String zoneId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    HUB("hub"),
    
    ARENA("arena"),
    
    PVE_ZONE("pve_zone"),
    
    DEEP_ZONE("deep_zone"),
    
    CUSTOM("custom");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  /**
   * Gets or Sets accessLevel
   */
  public enum AccessLevelEnum {
    BASIC("basic"),
    
    MEDIUM("medium"),
    
    ADVANCED("advanced");

    private final String value;

    AccessLevelEnum(String value) {
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
    public static AccessLevelEnum fromValue(String value) {
      for (AccessLevelEnum b : AccessLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AccessLevelEnum accessLevel;

  private @Nullable Boolean isPvp;

  private @Nullable Integer playerCount;

  private @Nullable String description;

  public CyberspaceZone zoneId(@Nullable String zoneId) {
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

  public CyberspaceZone name(@Nullable String name) {
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

  public CyberspaceZone type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public CyberspaceZone accessLevel(@Nullable AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  /**
   * Get accessLevel
   * @return accessLevel
   */
  
  @Schema(name = "access_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_level")
  public @Nullable AccessLevelEnum getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(@Nullable AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
  }

  public CyberspaceZone isPvp(@Nullable Boolean isPvp) {
    this.isPvp = isPvp;
    return this;
  }

  /**
   * Get isPvp
   * @return isPvp
   */
  
  @Schema(name = "is_pvp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_pvp")
  public @Nullable Boolean getIsPvp() {
    return isPvp;
  }

  public void setIsPvp(@Nullable Boolean isPvp) {
    this.isPvp = isPvp;
  }

  public CyberspaceZone playerCount(@Nullable Integer playerCount) {
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

  public CyberspaceZone description(@Nullable String description) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberspaceZone cyberspaceZone = (CyberspaceZone) o;
    return Objects.equals(this.zoneId, cyberspaceZone.zoneId) &&
        Objects.equals(this.name, cyberspaceZone.name) &&
        Objects.equals(this.type, cyberspaceZone.type) &&
        Objects.equals(this.accessLevel, cyberspaceZone.accessLevel) &&
        Objects.equals(this.isPvp, cyberspaceZone.isPvp) &&
        Objects.equals(this.playerCount, cyberspaceZone.playerCount) &&
        Objects.equals(this.description, cyberspaceZone.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, name, type, accessLevel, isPvp, playerCount, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberspaceZone {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
    sb.append("    isPvp: ").append(toIndentedString(isPvp)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

