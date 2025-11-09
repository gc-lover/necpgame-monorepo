package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
 * ZoneCellSummary
 */


public class ZoneCellSummary {

  private String cellKey;

  private Integer playerCount;

  private Integer npcCount;

  private @Nullable BigDecimal averageLatencyMs;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdate;

  public ZoneCellSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ZoneCellSummary(String cellKey, Integer playerCount, Integer npcCount) {
    this.cellKey = cellKey;
    this.playerCount = playerCount;
    this.npcCount = npcCount;
  }

  public ZoneCellSummary cellKey(String cellKey) {
    this.cellKey = cellKey;
    return this;
  }

  /**
   * Get cellKey
   * @return cellKey
   */
  @NotNull @Pattern(regexp = "^[0-9]+:[0-9]+$") 
  @Schema(name = "cellKey", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cellKey")
  public String getCellKey() {
    return cellKey;
  }

  public void setCellKey(String cellKey) {
    this.cellKey = cellKey;
  }

  public ZoneCellSummary playerCount(Integer playerCount) {
    this.playerCount = playerCount;
    return this;
  }

  /**
   * Get playerCount
   * minimum: 0
   * @return playerCount
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "playerCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerCount")
  public Integer getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(Integer playerCount) {
    this.playerCount = playerCount;
  }

  public ZoneCellSummary npcCount(Integer npcCount) {
    this.npcCount = npcCount;
    return this;
  }

  /**
   * Get npcCount
   * minimum: 0
   * @return npcCount
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "npcCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcCount")
  public Integer getNpcCount() {
    return npcCount;
  }

  public void setNpcCount(Integer npcCount) {
    this.npcCount = npcCount;
  }

  public ZoneCellSummary averageLatencyMs(@Nullable BigDecimal averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
    return this;
  }

  /**
   * Get averageLatencyMs
   * @return averageLatencyMs
   */
  @Valid 
  @Schema(name = "averageLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageLatencyMs")
  public @Nullable BigDecimal getAverageLatencyMs() {
    return averageLatencyMs;
  }

  public void setAverageLatencyMs(@Nullable BigDecimal averageLatencyMs) {
    this.averageLatencyMs = averageLatencyMs;
  }

  public ZoneCellSummary lastUpdate(@Nullable OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  /**
   * Get lastUpdate
   * @return lastUpdate
   */
  @Valid 
  @Schema(name = "lastUpdate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdate")
  public @Nullable OffsetDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(@Nullable OffsetDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneCellSummary zoneCellSummary = (ZoneCellSummary) o;
    return Objects.equals(this.cellKey, zoneCellSummary.cellKey) &&
        Objects.equals(this.playerCount, zoneCellSummary.playerCount) &&
        Objects.equals(this.npcCount, zoneCellSummary.npcCount) &&
        Objects.equals(this.averageLatencyMs, zoneCellSummary.averageLatencyMs) &&
        Objects.equals(this.lastUpdate, zoneCellSummary.lastUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cellKey, playerCount, npcCount, averageLatencyMs, lastUpdate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneCellSummary {\n");
    sb.append("    cellKey: ").append(toIndentedString(cellKey)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
    sb.append("    npcCount: ").append(toIndentedString(npcCount)).append("\n");
    sb.append("    averageLatencyMs: ").append(toIndentedString(averageLatencyMs)).append("\n");
    sb.append("    lastUpdate: ").append(toIndentedString(lastUpdate)).append("\n");
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

