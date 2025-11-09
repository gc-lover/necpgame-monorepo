package com.necpgame.sessionservice.model;

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
 * SessionInstabilityRecord
 */


public class SessionInstabilityRecord {

  private String playerId;

  private Integer disconnectCount24h;

  private @Nullable BigDecimal averageDowntimeSeconds;

  private @Nullable String lastIncidentId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastDisconnectAt;

  public SessionInstabilityRecord() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SessionInstabilityRecord(String playerId, Integer disconnectCount24h) {
    this.playerId = playerId;
    this.disconnectCount24h = disconnectCount24h;
  }

  public SessionInstabilityRecord playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public SessionInstabilityRecord disconnectCount24h(Integer disconnectCount24h) {
    this.disconnectCount24h = disconnectCount24h;
    return this;
  }

  /**
   * Get disconnectCount24h
   * @return disconnectCount24h
   */
  @NotNull 
  @Schema(name = "disconnectCount24h", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("disconnectCount24h")
  public Integer getDisconnectCount24h() {
    return disconnectCount24h;
  }

  public void setDisconnectCount24h(Integer disconnectCount24h) {
    this.disconnectCount24h = disconnectCount24h;
  }

  public SessionInstabilityRecord averageDowntimeSeconds(@Nullable BigDecimal averageDowntimeSeconds) {
    this.averageDowntimeSeconds = averageDowntimeSeconds;
    return this;
  }

  /**
   * Get averageDowntimeSeconds
   * @return averageDowntimeSeconds
   */
  @Valid 
  @Schema(name = "averageDowntimeSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageDowntimeSeconds")
  public @Nullable BigDecimal getAverageDowntimeSeconds() {
    return averageDowntimeSeconds;
  }

  public void setAverageDowntimeSeconds(@Nullable BigDecimal averageDowntimeSeconds) {
    this.averageDowntimeSeconds = averageDowntimeSeconds;
  }

  public SessionInstabilityRecord lastIncidentId(@Nullable String lastIncidentId) {
    this.lastIncidentId = lastIncidentId;
    return this;
  }

  /**
   * Get lastIncidentId
   * @return lastIncidentId
   */
  
  @Schema(name = "lastIncidentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastIncidentId")
  public @Nullable String getLastIncidentId() {
    return lastIncidentId;
  }

  public void setLastIncidentId(@Nullable String lastIncidentId) {
    this.lastIncidentId = lastIncidentId;
  }

  public SessionInstabilityRecord lastDisconnectAt(@Nullable OffsetDateTime lastDisconnectAt) {
    this.lastDisconnectAt = lastDisconnectAt;
    return this;
  }

  /**
   * Get lastDisconnectAt
   * @return lastDisconnectAt
   */
  @Valid 
  @Schema(name = "lastDisconnectAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastDisconnectAt")
  public @Nullable OffsetDateTime getLastDisconnectAt() {
    return lastDisconnectAt;
  }

  public void setLastDisconnectAt(@Nullable OffsetDateTime lastDisconnectAt) {
    this.lastDisconnectAt = lastDisconnectAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionInstabilityRecord sessionInstabilityRecord = (SessionInstabilityRecord) o;
    return Objects.equals(this.playerId, sessionInstabilityRecord.playerId) &&
        Objects.equals(this.disconnectCount24h, sessionInstabilityRecord.disconnectCount24h) &&
        Objects.equals(this.averageDowntimeSeconds, sessionInstabilityRecord.averageDowntimeSeconds) &&
        Objects.equals(this.lastIncidentId, sessionInstabilityRecord.lastIncidentId) &&
        Objects.equals(this.lastDisconnectAt, sessionInstabilityRecord.lastDisconnectAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, disconnectCount24h, averageDowntimeSeconds, lastIncidentId, lastDisconnectAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionInstabilityRecord {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    disconnectCount24h: ").append(toIndentedString(disconnectCount24h)).append("\n");
    sb.append("    averageDowntimeSeconds: ").append(toIndentedString(averageDowntimeSeconds)).append("\n");
    sb.append("    lastIncidentId: ").append(toIndentedString(lastIncidentId)).append("\n");
    sb.append("    lastDisconnectAt: ").append(toIndentedString(lastDisconnectAt)).append("\n");
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

