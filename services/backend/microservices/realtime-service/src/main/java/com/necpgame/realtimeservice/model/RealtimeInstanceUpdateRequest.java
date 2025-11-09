package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RealtimeInstanceUpdateRequest
 */


public class RealtimeInstanceUpdateRequest {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ONLINE("ONLINE"),
    
    MAINTENANCE("MAINTENANCE"),
    
    DRAINING("DRAINING"),
    
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

  /**
   * Gets or Sets tickRate
   */
  public enum TickRateEnum {
    NUMBER_20(20),
    
    NUMBER_30(30),
    
    NUMBER_60(60);

    private final Integer value;

    TickRateEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TickRateEnum fromValue(Integer value) {
      for (TickRateEnum b : TickRateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TickRateEnum tickRate;

  private @Nullable Integer maxZones;

  private @Nullable Integer maxPlayers;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    NORMAL("normal"),
    
    HIGH("high");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public RealtimeInstanceUpdateRequest status(@Nullable StatusEnum status) {
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

  public RealtimeInstanceUpdateRequest tickRate(@Nullable TickRateEnum tickRate) {
    this.tickRate = tickRate;
    return this;
  }

  /**
   * Get tickRate
   * @return tickRate
   */
  
  @Schema(name = "tickRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tickRate")
  public @Nullable TickRateEnum getTickRate() {
    return tickRate;
  }

  public void setTickRate(@Nullable TickRateEnum tickRate) {
    this.tickRate = tickRate;
  }

  public RealtimeInstanceUpdateRequest maxZones(@Nullable Integer maxZones) {
    this.maxZones = maxZones;
    return this;
  }

  /**
   * Get maxZones
   * minimum: 1
   * maximum: 12
   * @return maxZones
   */
  @Min(value = 1) @Max(value = 12) 
  @Schema(name = "maxZones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxZones")
  public @Nullable Integer getMaxZones() {
    return maxZones;
  }

  public void setMaxZones(@Nullable Integer maxZones) {
    this.maxZones = maxZones;
  }

  public RealtimeInstanceUpdateRequest maxPlayers(@Nullable Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  /**
   * Get maxPlayers
   * minimum: 100
   * maximum: 2000
   * @return maxPlayers
   */
  @Min(value = 100) @Max(value = 2000) 
  @Schema(name = "maxPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxPlayers")
  public @Nullable Integer getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(@Nullable Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public RealtimeInstanceUpdateRequest priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  public RealtimeInstanceUpdateRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public RealtimeInstanceUpdateRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealtimeInstanceUpdateRequest realtimeInstanceUpdateRequest = (RealtimeInstanceUpdateRequest) o;
    return Objects.equals(this.status, realtimeInstanceUpdateRequest.status) &&
        Objects.equals(this.tickRate, realtimeInstanceUpdateRequest.tickRate) &&
        Objects.equals(this.maxZones, realtimeInstanceUpdateRequest.maxZones) &&
        Objects.equals(this.maxPlayers, realtimeInstanceUpdateRequest.maxPlayers) &&
        Objects.equals(this.priority, realtimeInstanceUpdateRequest.priority) &&
        Objects.equals(this.metadata, realtimeInstanceUpdateRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, tickRate, maxZones, maxPlayers, priority, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeInstanceUpdateRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tickRate: ").append(toIndentedString(tickRate)).append("\n");
    sb.append("    maxZones: ").append(toIndentedString(maxZones)).append("\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

