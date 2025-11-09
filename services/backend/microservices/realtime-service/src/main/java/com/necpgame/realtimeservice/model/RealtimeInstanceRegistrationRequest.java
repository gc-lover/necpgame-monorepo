package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
 * RealtimeInstanceRegistrationRequest
 */


public class RealtimeInstanceRegistrationRequest {

  private String instanceId;

  private String region;

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

  private TickRateEnum tickRate;

  private Integer maxZones;

  private Integer maxPlayers;

  @Valid
  private List<String> supportedZoneTypes = new ArrayList<>();

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public RealtimeInstanceRegistrationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RealtimeInstanceRegistrationRequest(String instanceId, String region, TickRateEnum tickRate, Integer maxZones, Integer maxPlayers) {
    this.instanceId = instanceId;
    this.region = region;
    this.tickRate = tickRate;
    this.maxZones = maxZones;
    this.maxPlayers = maxPlayers;
  }

  public RealtimeInstanceRegistrationRequest instanceId(String instanceId) {
    this.instanceId = instanceId;
    return this;
  }

  /**
   * Get instanceId
   * @return instanceId
   */
  @NotNull 
  @Schema(name = "instanceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("instanceId")
  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public RealtimeInstanceRegistrationRequest region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  @NotNull 
  @Schema(name = "region", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public RealtimeInstanceRegistrationRequest tickRate(TickRateEnum tickRate) {
    this.tickRate = tickRate;
    return this;
  }

  /**
   * Get tickRate
   * @return tickRate
   */
  @NotNull 
  @Schema(name = "tickRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tickRate")
  public TickRateEnum getTickRate() {
    return tickRate;
  }

  public void setTickRate(TickRateEnum tickRate) {
    this.tickRate = tickRate;
  }

  public RealtimeInstanceRegistrationRequest maxZones(Integer maxZones) {
    this.maxZones = maxZones;
    return this;
  }

  /**
   * Get maxZones
   * minimum: 1
   * maximum: 12
   * @return maxZones
   */
  @NotNull @Min(value = 1) @Max(value = 12) 
  @Schema(name = "maxZones", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxZones")
  public Integer getMaxZones() {
    return maxZones;
  }

  public void setMaxZones(Integer maxZones) {
    this.maxZones = maxZones;
  }

  public RealtimeInstanceRegistrationRequest maxPlayers(Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
    return this;
  }

  /**
   * Get maxPlayers
   * minimum: 100
   * maximum: 2000
   * @return maxPlayers
   */
  @NotNull @Min(value = 100) @Max(value = 2000) 
  @Schema(name = "maxPlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxPlayers")
  public Integer getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(Integer maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public RealtimeInstanceRegistrationRequest supportedZoneTypes(List<String> supportedZoneTypes) {
    this.supportedZoneTypes = supportedZoneTypes;
    return this;
  }

  public RealtimeInstanceRegistrationRequest addSupportedZoneTypesItem(String supportedZoneTypesItem) {
    if (this.supportedZoneTypes == null) {
      this.supportedZoneTypes = new ArrayList<>();
    }
    this.supportedZoneTypes.add(supportedZoneTypesItem);
    return this;
  }

  /**
   * Get supportedZoneTypes
   * @return supportedZoneTypes
   */
  
  @Schema(name = "supportedZoneTypes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supportedZoneTypes")
  public List<String> getSupportedZoneTypes() {
    return supportedZoneTypes;
  }

  public void setSupportedZoneTypes(List<String> supportedZoneTypes) {
    this.supportedZoneTypes = supportedZoneTypes;
  }

  public RealtimeInstanceRegistrationRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public RealtimeInstanceRegistrationRequest putMetadataItem(String key, Object metadataItem) {
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
    RealtimeInstanceRegistrationRequest realtimeInstanceRegistrationRequest = (RealtimeInstanceRegistrationRequest) o;
    return Objects.equals(this.instanceId, realtimeInstanceRegistrationRequest.instanceId) &&
        Objects.equals(this.region, realtimeInstanceRegistrationRequest.region) &&
        Objects.equals(this.tickRate, realtimeInstanceRegistrationRequest.tickRate) &&
        Objects.equals(this.maxZones, realtimeInstanceRegistrationRequest.maxZones) &&
        Objects.equals(this.maxPlayers, realtimeInstanceRegistrationRequest.maxPlayers) &&
        Objects.equals(this.supportedZoneTypes, realtimeInstanceRegistrationRequest.supportedZoneTypes) &&
        Objects.equals(this.metadata, realtimeInstanceRegistrationRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(instanceId, region, tickRate, maxZones, maxPlayers, supportedZoneTypes, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeInstanceRegistrationRequest {\n");
    sb.append("    instanceId: ").append(toIndentedString(instanceId)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    tickRate: ").append(toIndentedString(tickRate)).append("\n");
    sb.append("    maxZones: ").append(toIndentedString(maxZones)).append("\n");
    sb.append("    maxPlayers: ").append(toIndentedString(maxPlayers)).append("\n");
    sb.append("    supportedZoneTypes: ").append(toIndentedString(supportedZoneTypes)).append("\n");
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

