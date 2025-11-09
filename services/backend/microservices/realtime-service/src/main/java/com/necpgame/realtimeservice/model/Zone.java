package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.realtimeservice.model.ZoneBoundaries;
import com.necpgame.realtimeservice.model.ZoneSettings;
import com.necpgame.realtimeservice.model.ZoneState;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Zone
 */


public class Zone {

  private String zoneId;

  private String zoneName;

  /**
   * Gets or Sets zoneType
   */
  public enum ZoneTypeEnum {
    URBAN("urban"),
    
    SUBURBAN("suburban"),
    
    CORPORATE("corporate"),
    
    INDUSTRIAL("industrial"),
    
    RAID("raid"),
    
    SAFE("safe");

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

  private ZoneTypeEnum zoneType;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ONLINE("ONLINE"),
    
    MAINTENANCE("MAINTENANCE"),
    
    MIGRATING("MIGRATING"),
    
    OFFLINE("OFFLINE");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable String assignedServerId;

  private Integer maxPlayers;

  private Integer currentPlayers;

  private @Nullable ZoneBoundaries boundaries;

  private @Nullable ZoneSettings settings;

  private @Nullable ZoneState stability;

  public Zone() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Zone(String zoneId, String zoneName, ZoneTypeEnum zoneType, StatusEnum status, Integer maxPlayers, Integer currentPlayers) {
    this.zoneId = zoneId;
    this.zoneName = zoneName;
    this.zoneType = zoneType;
    this.status = status;
    this.maxPlayers = maxPlayers;
    this.currentPlayers = currentPlayers;
  }

  public Zone zoneId(String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  @NotNull 
  @Schema(name = "zoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("zoneId")
  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  public Zone zoneName(String zoneName) {
    this.zoneName = zoneName;
    return this;
  }

  /**
   * Get zoneName
   * @return zoneName
   */
  @NotNull 
  @Schema(name = "zoneName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("zoneName")
  public String getZoneName() {
    return zoneName;
  }

  public void setZoneName(String zoneName) {
    this.zoneName = zoneName;
  }

  public Zone zoneType(ZoneTypeEnum zoneType) {
    this.zoneType = zoneType;
    return this;
  }

  /**
   * Get zoneType
   * @return zoneType
   */
  @NotNull 
  @Schema(name = "zoneType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("zoneType")
  public ZoneTypeEnum getZoneType() {
    return zoneType;
  }

  public void setZoneType(ZoneTypeEnum zoneType) {
    this.zoneType = zoneType;
  }

  public Zone status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Zone assignedServerId(@Nullable String assignedServerId) {
    this.assignedServerId = assignedServerId;
    return this;
  }

  /**
   * Get assignedServerId
   * @return assignedServerId
   */
  
  @Schema(name = "assignedServerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedServerId")
  public @Nullable String getAssignedServerId() {
    return assignedServerId;
  }

  public void setAssignedServerId(@Nullable String assignedServerId) {
    this.assignedServerId = assignedServerId;
  }

  public Zone maxPlayers(Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  /**
   * Get maxPlayers
   * minimum: 10
   * maximum: 400
   * @return maxPlayers
   */
  @NotNull @Min(value = 10) @Max(value = 400) 
  @Schema(name = "maxPlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxPlayers")
  public Integer getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public Zone currentPlayers(Integer currentPlayers) {
    this.currentPlayers = currentPlayers;
    return this;
  }

  /**
   * Get currentPlayers
   * minimum: 0
   * @return currentPlayers
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "currentPlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentPlayers")
  public Integer getCurrentPlayers() {
    return currentPlayers;
  }

  public void setCurrentPlayers(Integer currentPlayers) {
    this.currentPlayers = currentPlayers;
  }

  public Zone boundaries(@Nullable ZoneBoundaries boundaries) {
    this.boundaries = boundaries;
    return this;
  }

  /**
   * Get boundaries
   * @return boundaries
   */
  @Valid 
  @Schema(name = "boundaries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boundaries")
  public @Nullable ZoneBoundaries getBoundaries() {
    return boundaries;
  }

  public void setBoundaries(@Nullable ZoneBoundaries boundaries) {
    this.boundaries = boundaries;
  }

  public Zone settings(@Nullable ZoneSettings settings) {
    this.settings = settings;
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
  @Valid 
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("settings")
  public @Nullable ZoneSettings getSettings() {
    return settings;
  }

  public void setSettings(@Nullable ZoneSettings settings) {
    this.settings = settings;
  }

  public Zone stability(@Nullable ZoneState stability) {
    this.stability = stability;
    return this;
  }

  /**
   * Get stability
   * @return stability
   */
  @Valid 
  @Schema(name = "stability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stability")
  public @Nullable ZoneState getStability() {
    return stability;
  }

  public void setStability(@Nullable ZoneState stability) {
    this.stability = stability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Zone zone = (Zone) o;
    return Objects.equals(this.zoneId, zone.zoneId) &&
        Objects.equals(this.zoneName, zone.zoneName) &&
        Objects.equals(this.zoneType, zone.zoneType) &&
        Objects.equals(this.status, zone.status) &&
        Objects.equals(this.assignedServerId, zone.assignedServerId) &&
        Objects.equals(this.maxPlayers, zone.maxPlayers) &&
        Objects.equals(this.currentPlayers, zone.currentPlayers) &&
        Objects.equals(this.boundaries, zone.boundaries) &&
        Objects.equals(this.settings, zone.settings) &&
        Objects.equals(this.stability, zone.stability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zoneId, zoneName, zoneType, status, assignedServerId, maxPlayers, currentPlayers, boundaries, settings, stability);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Zone {\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    zoneName: ").append(toIndentedString(zoneName)).append("\n");
    sb.append("    zoneType: ").append(toIndentedString(zoneType)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    assignedServerId: ").append(toIndentedString(assignedServerId)).append("\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
    sb.append("    currentPlayers: ").append(toIndentedString(currentPlayers)).append("\n");
    sb.append("    boundaries: ").append(toIndentedString(boundaries)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    stability: ").append(toIndentedString(stability)).append("\n");
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

