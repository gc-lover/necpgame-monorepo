package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.QueueNotification;
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
 * QueueStatus
 */


public class QueueStatus {

  private UUID ticketId;

  private Integer etaSeconds;

  private Integer waitedSeconds;

  private @Nullable Integer priorityBoost;

  private @Nullable Integer currentRatingRange;

  @Valid
  private List<@Valid RangeExpansion> expansions = new ArrayList<>();

  @Valid
  private List<@Valid QueueNotification> notifications = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime heartbeatDueAt;

  public QueueStatus() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueStatus(UUID ticketId, Integer etaSeconds, Integer waitedSeconds) {
    this.ticketId = ticketId;
    this.etaSeconds = etaSeconds;
    this.waitedSeconds = waitedSeconds;
  }

  public QueueStatus ticketId(UUID ticketId) {
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

  public QueueStatus etaSeconds(Integer etaSeconds) {
    this.etaSeconds = etaSeconds;
    return this;
  }

  /**
   * Get etaSeconds
   * minimum: 0
   * @return etaSeconds
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "etaSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("etaSeconds")
  public Integer getEtaSeconds() {
    return etaSeconds;
  }

  public void setEtaSeconds(Integer etaSeconds) {
    this.etaSeconds = etaSeconds;
  }

  public QueueStatus waitedSeconds(Integer waitedSeconds) {
    this.waitedSeconds = waitedSeconds;
    return this;
  }

  /**
   * Get waitedSeconds
   * minimum: 0
   * @return waitedSeconds
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "waitedSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("waitedSeconds")
  public Integer getWaitedSeconds() {
    return waitedSeconds;
  }

  public void setWaitedSeconds(Integer waitedSeconds) {
    this.waitedSeconds = waitedSeconds;
  }

  public QueueStatus priorityBoost(@Nullable Integer priorityBoost) {
    this.priorityBoost = priorityBoost;
    return this;
  }

  /**
   * Get priorityBoost
   * @return priorityBoost
   */
  
  @Schema(name = "priorityBoost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priorityBoost")
  public @Nullable Integer getPriorityBoost() {
    return priorityBoost;
  }

  public void setPriorityBoost(@Nullable Integer priorityBoost) {
    this.priorityBoost = priorityBoost;
  }

  public QueueStatus currentRatingRange(@Nullable Integer currentRatingRange) {
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

  public QueueStatus expansions(List<@Valid RangeExpansion> expansions) {
    this.expansions = expansions;
    return this;
  }

  public QueueStatus addExpansionsItem(RangeExpansion expansionsItem) {
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

  public QueueStatus notifications(List<@Valid QueueNotification> notifications) {
    this.notifications = notifications;
    return this;
  }

  public QueueStatus addNotificationsItem(QueueNotification notificationsItem) {
    if (this.notifications == null) {
      this.notifications = new ArrayList<>();
    }
    this.notifications.add(notificationsItem);
    return this;
  }

  /**
   * Get notifications
   * @return notifications
   */
  @Valid 
  @Schema(name = "notifications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifications")
  public List<@Valid QueueNotification> getNotifications() {
    return notifications;
  }

  public void setNotifications(List<@Valid QueueNotification> notifications) {
    this.notifications = notifications;
  }

  public QueueStatus heartbeatDueAt(@Nullable OffsetDateTime heartbeatDueAt) {
    this.heartbeatDueAt = heartbeatDueAt;
    return this;
  }

  /**
   * Get heartbeatDueAt
   * @return heartbeatDueAt
   */
  @Valid 
  @Schema(name = "heartbeatDueAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heartbeatDueAt")
  public @Nullable OffsetDateTime getHeartbeatDueAt() {
    return heartbeatDueAt;
  }

  public void setHeartbeatDueAt(@Nullable OffsetDateTime heartbeatDueAt) {
    this.heartbeatDueAt = heartbeatDueAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueStatus queueStatus = (QueueStatus) o;
    return Objects.equals(this.ticketId, queueStatus.ticketId) &&
        Objects.equals(this.etaSeconds, queueStatus.etaSeconds) &&
        Objects.equals(this.waitedSeconds, queueStatus.waitedSeconds) &&
        Objects.equals(this.priorityBoost, queueStatus.priorityBoost) &&
        Objects.equals(this.currentRatingRange, queueStatus.currentRatingRange) &&
        Objects.equals(this.expansions, queueStatus.expansions) &&
        Objects.equals(this.notifications, queueStatus.notifications) &&
        Objects.equals(this.heartbeatDueAt, queueStatus.heartbeatDueAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, etaSeconds, waitedSeconds, priorityBoost, currentRatingRange, expansions, notifications, heartbeatDueAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueStatus {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    etaSeconds: ").append(toIndentedString(etaSeconds)).append("\n");
    sb.append("    waitedSeconds: ").append(toIndentedString(waitedSeconds)).append("\n");
    sb.append("    priorityBoost: ").append(toIndentedString(priorityBoost)).append("\n");
    sb.append("    currentRatingRange: ").append(toIndentedString(currentRatingRange)).append("\n");
    sb.append("    expansions: ").append(toIndentedString(expansions)).append("\n");
    sb.append("    notifications: ").append(toIndentedString(notifications)).append("\n");
    sb.append("    heartbeatDueAt: ").append(toIndentedString(heartbeatDueAt)).append("\n");
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

