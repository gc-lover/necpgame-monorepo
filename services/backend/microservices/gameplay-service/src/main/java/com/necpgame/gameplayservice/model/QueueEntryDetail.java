package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ActivityType;
import com.necpgame.gameplayservice.model.QueueEntryDetailAllOfHistory;
import com.necpgame.gameplayservice.model.QueueMode;
import com.necpgame.gameplayservice.model.QueuePriorityState;
import com.necpgame.gameplayservice.model.QueueStatusCode;
import com.necpgame.gameplayservice.model.RangeExpansion;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * QueueEntryDetail
 */


public class QueueEntryDetail {

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

  private @Nullable Integer etaSeconds;

  private @Nullable Integer waitedSeconds;

  @Valid
  private List<@Valid RangeExpansion> expansions = new ArrayList<>();

  private @Nullable QueuePriorityState priorityState;

  @Valid
  private List<@Valid QueueEntryDetailAllOfHistory> history = new ArrayList<>();

  public QueueEntryDetail() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueEntryDetail(UUID ticketId, UUID playerId, OffsetDateTime queuedAt, QueueStatusCode status) {
    this.ticketId = ticketId;
    this.playerId = playerId;
    this.queuedAt = queuedAt;
    this.status = status;
  }

  public QueueEntryDetail ticketId(UUID ticketId) {
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

  public QueueEntryDetail playerId(UUID playerId) {
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

  public QueueEntryDetail partyId(@Nullable UUID partyId) {
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

  public QueueEntryDetail partySize(@Nullable Integer partySize) {
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

  public QueueEntryDetail activityType(@Nullable ActivityType activityType) {
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

  public QueueEntryDetail mode(@Nullable QueueMode mode) {
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

  public QueueEntryDetail queuedAt(OffsetDateTime queuedAt) {
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

  public QueueEntryDetail expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public QueueEntryDetail priority(@Nullable Integer priority) {
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

  public QueueEntryDetail status(QueueStatusCode status) {
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

  public QueueEntryDetail currentRatingRange(@Nullable Integer currentRatingRange) {
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

  public QueueEntryDetail waitTimeEstimateSeconds(@Nullable Integer waitTimeEstimateSeconds) {
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

  public QueueEntryDetail traceId(@Nullable String traceId) {
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

  public QueueEntryDetail etaSeconds(@Nullable Integer etaSeconds) {
    this.etaSeconds = etaSeconds;
    return this;
  }

  /**
   * Get etaSeconds
   * @return etaSeconds
   */
  
  @Schema(name = "etaSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("etaSeconds")
  public @Nullable Integer getEtaSeconds() {
    return etaSeconds;
  }

  public void setEtaSeconds(@Nullable Integer etaSeconds) {
    this.etaSeconds = etaSeconds;
  }

  public QueueEntryDetail waitedSeconds(@Nullable Integer waitedSeconds) {
    this.waitedSeconds = waitedSeconds;
    return this;
  }

  /**
   * Get waitedSeconds
   * @return waitedSeconds
   */
  
  @Schema(name = "waitedSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("waitedSeconds")
  public @Nullable Integer getWaitedSeconds() {
    return waitedSeconds;
  }

  public void setWaitedSeconds(@Nullable Integer waitedSeconds) {
    this.waitedSeconds = waitedSeconds;
  }

  public QueueEntryDetail expansions(List<@Valid RangeExpansion> expansions) {
    this.expansions = expansions;
    return this;
  }

  public QueueEntryDetail addExpansionsItem(RangeExpansion expansionsItem) {
    if (this.expansions == null) {
      this.expansions = new ArrayList<>();
    }
    this.expansions.add(expansionsItem);
    return this;
  }

  /**
   * Get expansions
   * @return expansions
   */
  @Valid 
  @Schema(name = "expansions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expansions")
  public List<@Valid RangeExpansion> getExpansions() {
    return expansions;
  }

  public void setExpansions(List<@Valid RangeExpansion> expansions) {
    this.expansions = expansions;
  }

  public QueueEntryDetail priorityState(@Nullable QueuePriorityState priorityState) {
    this.priorityState = priorityState;
    return this;
  }

  /**
   * Get priorityState
   * @return priorityState
   */
  @Valid 
  @Schema(name = "priorityState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priorityState")
  public @Nullable QueuePriorityState getPriorityState() {
    return priorityState;
  }

  public void setPriorityState(@Nullable QueuePriorityState priorityState) {
    this.priorityState = priorityState;
  }

  public QueueEntryDetail history(List<@Valid QueueEntryDetailAllOfHistory> history) {
    this.history = history;
    return this;
  }

  public QueueEntryDetail addHistoryItem(QueueEntryDetailAllOfHistory historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<@Valid QueueEntryDetailAllOfHistory> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid QueueEntryDetailAllOfHistory> history) {
    this.history = history;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueEntryDetail queueEntryDetail = (QueueEntryDetail) o;
    return Objects.equals(this.ticketId, queueEntryDetail.ticketId) &&
        Objects.equals(this.playerId, queueEntryDetail.playerId) &&
        Objects.equals(this.partyId, queueEntryDetail.partyId) &&
        Objects.equals(this.partySize, queueEntryDetail.partySize) &&
        Objects.equals(this.activityType, queueEntryDetail.activityType) &&
        Objects.equals(this.mode, queueEntryDetail.mode) &&
        Objects.equals(this.queuedAt, queueEntryDetail.queuedAt) &&
        Objects.equals(this.expiresAt, queueEntryDetail.expiresAt) &&
        Objects.equals(this.priority, queueEntryDetail.priority) &&
        Objects.equals(this.status, queueEntryDetail.status) &&
        Objects.equals(this.currentRatingRange, queueEntryDetail.currentRatingRange) &&
        Objects.equals(this.waitTimeEstimateSeconds, queueEntryDetail.waitTimeEstimateSeconds) &&
        Objects.equals(this.traceId, queueEntryDetail.traceId) &&
        Objects.equals(this.etaSeconds, queueEntryDetail.etaSeconds) &&
        Objects.equals(this.waitedSeconds, queueEntryDetail.waitedSeconds) &&
        Objects.equals(this.expansions, queueEntryDetail.expansions) &&
        Objects.equals(this.priorityState, queueEntryDetail.priorityState) &&
        Objects.equals(this.history, queueEntryDetail.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, playerId, partyId, partySize, activityType, mode, queuedAt, expiresAt, priority, status, currentRatingRange, waitTimeEstimateSeconds, traceId, etaSeconds, waitedSeconds, expansions, priorityState, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueEntryDetail {\n");
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
    sb.append("    etaSeconds: ").append(toIndentedString(etaSeconds)).append("\n");
    sb.append("    waitedSeconds: ").append(toIndentedString(waitedSeconds)).append("\n");
    sb.append("    expansions: ").append(toIndentedString(expansions)).append("\n");
    sb.append("    priorityState: ").append(toIndentedString(priorityState)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

