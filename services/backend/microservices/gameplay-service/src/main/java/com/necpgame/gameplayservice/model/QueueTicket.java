package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ActivityType;
import com.necpgame.gameplayservice.model.QueueMode;
import com.necpgame.gameplayservice.model.QueueStatusCode;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * QueueTicket
 */


public class QueueTicket {

  private UUID ticketId;

  private UUID playerId;

  private @Nullable UUID partyId;

  private @Nullable Integer partySize;

  private @Nullable ActivityType activityType;

  private @Nullable QueueMode mode;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime queuedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable Integer priority;

  private QueueStatusCode status;

  private @Nullable Integer currentRatingRange;

  private @Nullable Integer waitTimeEstimateSeconds;

  private @Nullable String traceId;

  public QueueTicket() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueTicket(UUID ticketId, UUID playerId, OffsetDateTime queuedAt, QueueStatusCode status) {
    this.ticketId = ticketId;
    this.playerId = playerId;
    this.queuedAt = queuedAt;
    this.status = status;
  }

  public QueueTicket ticketId(UUID ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
   */
  @NotNull @Valid 
  @Schema(name = "ticketId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticketId")
  public UUID getTicketId() {
    return ticketId;
  }

  public void setTicketId(UUID ticketId) {
    this.ticketId = ticketId;
  }

  public QueueTicket playerId(UUID playerId) {
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

  public QueueTicket partyId(@Nullable UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable UUID partyId) {
    this.partyId = partyId;
  }

  public QueueTicket partySize(@Nullable Integer partySize) {
    this.partySize = partySize;
    return this;
  }

  /**
   * Get partySize
   * @return partySize
   */
  
  @Schema(name = "partySize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partySize")
  public @Nullable Integer getPartySize() {
    return partySize;
  }

  public void setPartySize(@Nullable Integer partySize) {
    this.partySize = partySize;
  }

  public QueueTicket activityType(@Nullable ActivityType activityType) {
    this.activityType = activityType;
    return this;
  }

  /**
   * Get activityType
   * @return activityType
   */
  @Valid 
  @Schema(name = "activityType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activityType")
  public @Nullable ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(@Nullable ActivityType activityType) {
    this.activityType = activityType;
  }

  public QueueTicket mode(@Nullable QueueMode mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @Valid 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable QueueMode getMode() {
    return mode;
  }

  public void setMode(@Nullable QueueMode mode) {
    this.mode = mode;
  }

  public QueueTicket queuedAt(OffsetDateTime queuedAt) {
    this.queuedAt = queuedAt;
    return this;
  }

  /**
   * Get queuedAt
   * @return queuedAt
   */
  @NotNull @Valid 
  @Schema(name = "queuedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("queuedAt")
  public OffsetDateTime getQueuedAt() {
    return queuedAt;
  }

  public void setQueuedAt(OffsetDateTime queuedAt) {
    this.queuedAt = queuedAt;
  }

  public QueueTicket expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public QueueTicket priority(@Nullable Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable Integer getPriority() {
    return priority;
  }

  public void setPriority(@Nullable Integer priority) {
    this.priority = priority;
  }

  public QueueTicket status(QueueStatusCode status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public QueueStatusCode getStatus() {
    return status;
  }

  public void setStatus(QueueStatusCode status) {
    this.status = status;
  }

  public QueueTicket currentRatingRange(@Nullable Integer currentRatingRange) {
    this.currentRatingRange = currentRatingRange;
    return this;
  }

  /**
   * Get currentRatingRange
   * @return currentRatingRange
   */
  
  @Schema(name = "currentRatingRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentRatingRange")
  public @Nullable Integer getCurrentRatingRange() {
    return currentRatingRange;
  }

  public void setCurrentRatingRange(@Nullable Integer currentRatingRange) {
    this.currentRatingRange = currentRatingRange;
  }

  public QueueTicket waitTimeEstimateSeconds(@Nullable Integer waitTimeEstimateSeconds) {
    this.waitTimeEstimateSeconds = waitTimeEstimateSeconds;
    return this;
  }

  /**
   * Get waitTimeEstimateSeconds
   * @return waitTimeEstimateSeconds
   */
  
  @Schema(name = "waitTimeEstimateSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("waitTimeEstimateSeconds")
  public @Nullable Integer getWaitTimeEstimateSeconds() {
    return waitTimeEstimateSeconds;
  }

  public void setWaitTimeEstimateSeconds(@Nullable Integer waitTimeEstimateSeconds) {
    this.waitTimeEstimateSeconds = waitTimeEstimateSeconds;
  }

  public QueueTicket traceId(@Nullable String traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * Get traceId
   * @return traceId
   */
  
  @Schema(name = "traceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("traceId")
  public @Nullable String getTraceId() {
    return traceId;
  }

  public void setTraceId(@Nullable String traceId) {
    this.traceId = traceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueTicket queueTicket = (QueueTicket) o;
    return Objects.equals(this.ticketId, queueTicket.ticketId) &&
        Objects.equals(this.playerId, queueTicket.playerId) &&
        Objects.equals(this.partyId, queueTicket.partyId) &&
        Objects.equals(this.partySize, queueTicket.partySize) &&
        Objects.equals(this.activityType, queueTicket.activityType) &&
        Objects.equals(this.mode, queueTicket.mode) &&
        Objects.equals(this.queuedAt, queueTicket.queuedAt) &&
        Objects.equals(this.expiresAt, queueTicket.expiresAt) &&
        Objects.equals(this.priority, queueTicket.priority) &&
        Objects.equals(this.status, queueTicket.status) &&
        Objects.equals(this.currentRatingRange, queueTicket.currentRatingRange) &&
        Objects.equals(this.waitTimeEstimateSeconds, queueTicket.waitTimeEstimateSeconds) &&
        Objects.equals(this.traceId, queueTicket.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, playerId, partyId, partySize, activityType, mode, queuedAt, expiresAt, priority, status, currentRatingRange, waitTimeEstimateSeconds, traceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueTicket {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    partySize: ").append(toIndentedString(partySize)).append("\n");
    sb.append("    activityType: ").append(toIndentedString(activityType)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    queuedAt: ").append(toIndentedString(queuedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    currentRatingRange: ").append(toIndentedString(currentRatingRange)).append("\n");
    sb.append("    waitTimeEstimateSeconds: ").append(toIndentedString(waitTimeEstimateSeconds)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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

