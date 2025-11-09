package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.realtimeservice.model.ZoneSettings;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ZoneUpdateRequest
 */


public class ZoneUpdateRequest {

  private @Nullable Integer maxPlayers;

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

  private @Nullable StatusEnum status;

  private @Nullable ZoneSettings settings;

  public ZoneUpdateRequest maxPlayers(@Nullable Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  /**
   * Get maxPlayers
   * minimum: 10
   * maximum: 400
   * @return maxPlayers
   */
  @Min(value = 10) @Max(value = 400) 
  @Schema(name = "maxPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxPlayers")
  public @Nullable Integer getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(@Nullable Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public ZoneUpdateRequest status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ZoneUpdateRequest settings(@Nullable ZoneSettings settings) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneUpdateRequest zoneUpdateRequest = (ZoneUpdateRequest) o;
    return Objects.equals(this.maxPlayers, zoneUpdateRequest.maxPlayers) &&
        Objects.equals(this.status, zoneUpdateRequest.status) &&
        Objects.equals(this.settings, zoneUpdateRequest.settings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxPlayers, status, settings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneUpdateRequest {\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
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

