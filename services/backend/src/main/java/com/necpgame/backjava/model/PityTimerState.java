package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PityTimerState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PityTimerState {

  private UUID playerId;

  private String tableId;

  private Integer counter;

  private @Nullable Integer threshold;

  private @Nullable String guaranteedReward;

  public PityTimerState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PityTimerState(UUID playerId, String tableId, Integer counter) {
    this.playerId = playerId;
    this.tableId = tableId;
    this.counter = counter;
  }

  public PityTimerState playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public PityTimerState tableId(String tableId) {
    this.tableId = tableId;
    return this;
  }

  /**
   * Get tableId
   * @return tableId
   */
  @NotNull 
  @Schema(name = "tableId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tableId")
  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public PityTimerState counter(Integer counter) {
    this.counter = counter;
    return this;
  }

  /**
   * Get counter
   * @return counter
   */
  @NotNull 
  @Schema(name = "counter", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("counter")
  public Integer getCounter() {
    return counter;
  }

  public void setCounter(Integer counter) {
    this.counter = counter;
  }

  public PityTimerState threshold(@Nullable Integer threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable Integer getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable Integer threshold) {
    this.threshold = threshold;
  }

  public PityTimerState guaranteedReward(@Nullable String guaranteedReward) {
    this.guaranteedReward = guaranteedReward;
    return this;
  }

  /**
   * Get guaranteedReward
   * @return guaranteedReward
   */
  
  @Schema(name = "guaranteedReward", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteedReward")
  public @Nullable String getGuaranteedReward() {
    return guaranteedReward;
  }

  public void setGuaranteedReward(@Nullable String guaranteedReward) {
    this.guaranteedReward = guaranteedReward;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PityTimerState pityTimerState = (PityTimerState) o;
    return Objects.equals(this.playerId, pityTimerState.playerId) &&
        Objects.equals(this.tableId, pityTimerState.tableId) &&
        Objects.equals(this.counter, pityTimerState.counter) &&
        Objects.equals(this.threshold, pityTimerState.threshold) &&
        Objects.equals(this.guaranteedReward, pityTimerState.guaranteedReward);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, tableId, counter, threshold, guaranteedReward);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PityTimerState {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    tableId: ").append(toIndentedString(tableId)).append("\n");
    sb.append("    counter: ").append(toIndentedString(counter)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
    sb.append("    guaranteedReward: ").append(toIndentedString(guaranteedReward)).append("\n");
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

