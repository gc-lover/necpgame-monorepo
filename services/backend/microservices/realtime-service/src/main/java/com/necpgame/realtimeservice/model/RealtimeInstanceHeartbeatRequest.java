package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RealtimeInstanceHeartbeatRequest
 */


public class RealtimeInstanceHeartbeatRequest {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private BigDecimal tickDurationMs;

  private Integer activeZones;

  private Integer activePlayers;

  /**
   * Gets or Sets warnings
   */
  public enum WarningsEnum {
    TICK_OVER_50_MS("TICK_OVER_50MS"),
    
    CAPACITY_OVER_85("CAPACITY_OVER_85"),
    
    REDIS_LATENCY_HIGH("REDIS_LATENCY_HIGH");

    private final String value;

    WarningsEnum(String value) {
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
    public static WarningsEnum fromValue(String value) {
      for (WarningsEnum b : WarningsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<WarningsEnum> warnings = new ArrayList<>();

  private @Nullable String buildVersion;

  public RealtimeInstanceHeartbeatRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RealtimeInstanceHeartbeatRequest(OffsetDateTime timestamp, BigDecimal tickDurationMs, Integer activeZones, Integer activePlayers) {
    this.timestamp = timestamp;
    this.tickDurationMs = tickDurationMs;
    this.activeZones = activeZones;
    this.activePlayers = activePlayers;
  }

  public RealtimeInstanceHeartbeatRequest timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public RealtimeInstanceHeartbeatRequest tickDurationMs(BigDecimal tickDurationMs) {
    this.tickDurationMs = tickDurationMs;
    return this;
  }

  /**
   * Get tickDurationMs
   * minimum: 0
   * @return tickDurationMs
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "tickDurationMs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tickDurationMs")
  public BigDecimal getTickDurationMs() {
    return tickDurationMs;
  }

  public void setTickDurationMs(BigDecimal tickDurationMs) {
    this.tickDurationMs = tickDurationMs;
  }

  public RealtimeInstanceHeartbeatRequest activeZones(Integer activeZones) {
    this.activeZones = activeZones;
    return this;
  }

  /**
   * Get activeZones
   * minimum: 0
   * @return activeZones
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "activeZones", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activeZones")
  public Integer getActiveZones() {
    return activeZones;
  }

  public void setActiveZones(Integer activeZones) {
    this.activeZones = activeZones;
  }

  public RealtimeInstanceHeartbeatRequest activePlayers(Integer activePlayers) {
    this.activePlayers = activePlayers;
    return this;
  }

  /**
   * Get activePlayers
   * minimum: 0
   * @return activePlayers
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "activePlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activePlayers")
  public Integer getActivePlayers() {
    return activePlayers;
  }

  public void setActivePlayers(Integer activePlayers) {
    this.activePlayers = activePlayers;
  }

  public RealtimeInstanceHeartbeatRequest warnings(List<WarningsEnum> warnings) {
    this.warnings = warnings;
    return this;
  }

  public RealtimeInstanceHeartbeatRequest addWarningsItem(WarningsEnum warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<WarningsEnum> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<WarningsEnum> warnings) {
    this.warnings = warnings;
  }

  public RealtimeInstanceHeartbeatRequest buildVersion(@Nullable String buildVersion) {
    this.buildVersion = buildVersion;
    return this;
  }

  /**
   * Get buildVersion
   * @return buildVersion
   */
  
  @Schema(name = "buildVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buildVersion")
  public @Nullable String getBuildVersion() {
    return buildVersion;
  }

  public void setBuildVersion(@Nullable String buildVersion) {
    this.buildVersion = buildVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealtimeInstanceHeartbeatRequest realtimeInstanceHeartbeatRequest = (RealtimeInstanceHeartbeatRequest) o;
    return Objects.equals(this.timestamp, realtimeInstanceHeartbeatRequest.timestamp) &&
        Objects.equals(this.tickDurationMs, realtimeInstanceHeartbeatRequest.tickDurationMs) &&
        Objects.equals(this.activeZones, realtimeInstanceHeartbeatRequest.activeZones) &&
        Objects.equals(this.activePlayers, realtimeInstanceHeartbeatRequest.activePlayers) &&
        Objects.equals(this.warnings, realtimeInstanceHeartbeatRequest.warnings) &&
        Objects.equals(this.buildVersion, realtimeInstanceHeartbeatRequest.buildVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, tickDurationMs, activeZones, activePlayers, warnings, buildVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeInstanceHeartbeatRequest {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    tickDurationMs: ").append(toIndentedString(tickDurationMs)).append("\n");
    sb.append("    activeZones: ").append(toIndentedString(activeZones)).append("\n");
    sb.append("    activePlayers: ").append(toIndentedString(activePlayers)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    buildVersion: ").append(toIndentedString(buildVersion)).append("\n");
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

