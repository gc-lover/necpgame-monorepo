package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LatencySample;
import com.necpgame.gameplayservice.model.RangeExpansionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MatchTelemetryBatch
 */


public class MatchTelemetryBatch {

  private UUID queueId;

  private Integer waitDurationMs;

  @Valid
  private List<@Valid RangeExpansionEvent> rangeExpansions = new ArrayList<>();

  @Valid
  private List<@Valid LatencySample> latencySamples = new ArrayList<>();

  private @Nullable Integer partySize;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PVP_RANKED("PVP_RANKED"),
    
    PVP_CASUAL("PVP_CASUAL"),
    
    PVE_DUNGEON("PVE_DUNGEON"),
    
    RAID("RAID"),
    
    ARENA_EVENT("ARENA_EVENT");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ModeEnum mode;

  private @Nullable Integer reconnectCount;

  public MatchTelemetryBatch() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchTelemetryBatch(UUID queueId, Integer waitDurationMs) {
    this.queueId = queueId;
    this.waitDurationMs = waitDurationMs;
  }

  public MatchTelemetryBatch queueId(UUID queueId) {
    this.queueId = queueId;
    return this;
  }

  /**
   * Get queueId
   * @return queueId
   */
  @NotNull @Valid 
  @Schema(name = "queueId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("queueId")
  public UUID getQueueId() {
    return queueId;
  }

  public void setQueueId(UUID queueId) {
    this.queueId = queueId;
  }

  public MatchTelemetryBatch waitDurationMs(Integer waitDurationMs) {
    this.waitDurationMs = waitDurationMs;
    return this;
  }

  /**
   * Get waitDurationMs
   * minimum: 0
   * @return waitDurationMs
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "waitDurationMs", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("waitDurationMs")
  public Integer getWaitDurationMs() {
    return waitDurationMs;
  }

  public void setWaitDurationMs(Integer waitDurationMs) {
    this.waitDurationMs = waitDurationMs;
  }

  public MatchTelemetryBatch rangeExpansions(List<@Valid RangeExpansionEvent> rangeExpansions) {
    this.rangeExpansions = rangeExpansions;
    return this;
  }

  public MatchTelemetryBatch addRangeExpansionsItem(RangeExpansionEvent rangeExpansionsItem) {
    if (this.rangeExpansions == null) {
      this.rangeExpansions = new ArrayList<>();
    }
    this.rangeExpansions.add(rangeExpansionsItem);
    return this;
  }

  /**
   * Get rangeExpansions
   * @return rangeExpansions
   */
  @Valid @Size(max = 10) 
  @Schema(name = "rangeExpansions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rangeExpansions")
  public List<@Valid RangeExpansionEvent> getRangeExpansions() {
    return rangeExpansions;
  }

  public void setRangeExpansions(List<@Valid RangeExpansionEvent> rangeExpansions) {
    this.rangeExpansions = rangeExpansions;
  }

  public MatchTelemetryBatch latencySamples(List<@Valid LatencySample> latencySamples) {
    this.latencySamples = latencySamples;
    return this;
  }

  public MatchTelemetryBatch addLatencySamplesItem(LatencySample latencySamplesItem) {
    if (this.latencySamples == null) {
      this.latencySamples = new ArrayList<>();
    }
    this.latencySamples.add(latencySamplesItem);
    return this;
  }

  /**
   * Get latencySamples
   * @return latencySamples
   */
  @Valid @Size(max = 50) 
  @Schema(name = "latencySamples", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencySamples")
  public List<@Valid LatencySample> getLatencySamples() {
    return latencySamples;
  }

  public void setLatencySamples(List<@Valid LatencySample> latencySamples) {
    this.latencySamples = latencySamples;
  }

  public MatchTelemetryBatch partySize(@Nullable Integer partySize) {
    this.partySize = partySize;
    return this;
  }

  /**
   * Get partySize
   * minimum: 1
   * maximum: 8
   * @return partySize
   */
  @Min(value = 1) @Max(value = 8) 
  @Schema(name = "partySize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partySize")
  public @Nullable Integer getPartySize() {
    return partySize;
  }

  public void setPartySize(@Nullable Integer partySize) {
    this.partySize = partySize;
  }

  public MatchTelemetryBatch mode(@Nullable ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable ModeEnum getMode() {
    return mode;
  }

  public void setMode(@Nullable ModeEnum mode) {
    this.mode = mode;
  }

  public MatchTelemetryBatch reconnectCount(@Nullable Integer reconnectCount) {
    this.reconnectCount = reconnectCount;
    return this;
  }

  /**
   * Get reconnectCount
   * minimum: 0
   * @return reconnectCount
   */
  @Min(value = 0) 
  @Schema(name = "reconnectCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reconnectCount")
  public @Nullable Integer getReconnectCount() {
    return reconnectCount;
  }

  public void setReconnectCount(@Nullable Integer reconnectCount) {
    this.reconnectCount = reconnectCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchTelemetryBatch matchTelemetryBatch = (MatchTelemetryBatch) o;
    return Objects.equals(this.queueId, matchTelemetryBatch.queueId) &&
        Objects.equals(this.waitDurationMs, matchTelemetryBatch.waitDurationMs) &&
        Objects.equals(this.rangeExpansions, matchTelemetryBatch.rangeExpansions) &&
        Objects.equals(this.latencySamples, matchTelemetryBatch.latencySamples) &&
        Objects.equals(this.partySize, matchTelemetryBatch.partySize) &&
        Objects.equals(this.mode, matchTelemetryBatch.mode) &&
        Objects.equals(this.reconnectCount, matchTelemetryBatch.reconnectCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queueId, waitDurationMs, rangeExpansions, latencySamples, partySize, mode, reconnectCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchTelemetryBatch {\n");
    sb.append("    queueId: ").append(toIndentedString(queueId)).append("\n");
    sb.append("    waitDurationMs: ").append(toIndentedString(waitDurationMs)).append("\n");
    sb.append("    rangeExpansions: ").append(toIndentedString(rangeExpansions)).append("\n");
    sb.append("    latencySamples: ").append(toIndentedString(latencySamples)).append("\n");
    sb.append("    partySize: ").append(toIndentedString(partySize)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    reconnectCount: ").append(toIndentedString(reconnectCount)).append("\n");
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

