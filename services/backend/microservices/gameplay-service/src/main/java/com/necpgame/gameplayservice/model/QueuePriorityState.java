package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.QueuePriorityStateBoostSourcesInner;
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
 * QueuePriorityState
 */


public class QueuePriorityState {

  private UUID ticketId;

  private Integer priority;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime boostedUntil;

  @Valid
  private List<@Valid QueuePriorityStateBoostSourcesInner> boostSources = new ArrayList<>();

  public QueuePriorityState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueuePriorityState(UUID ticketId, Integer priority) {
    this.ticketId = ticketId;
    this.priority = priority;
  }

  public QueuePriorityState ticketId(UUID ticketId) {
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

  public QueuePriorityState priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @NotNull 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public QueuePriorityState boostedUntil(@Nullable OffsetDateTime boostedUntil) {
    this.boostedUntil = boostedUntil;
    return this;
  }

  /**
   * Get boostedUntil
   * @return boostedUntil
   */
  @Valid 
  @Schema(name = "boostedUntil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostedUntil")
  public @Nullable OffsetDateTime getBoostedUntil() {
    return boostedUntil;
  }

  public void setBoostedUntil(@Nullable OffsetDateTime boostedUntil) {
    this.boostedUntil = boostedUntil;
  }

  public QueuePriorityState boostSources(List<@Valid QueuePriorityStateBoostSourcesInner> boostSources) {
    this.boostSources = boostSources;
    return this;
  }

  public QueuePriorityState addBoostSourcesItem(QueuePriorityStateBoostSourcesInner boostSourcesItem) {
    if (this.boostSources == null) {
      this.boostSources = new ArrayList<>();
    }
    this.boostSources.add(boostSourcesItem);
    return this;
  }

  /**
   * Get boostSources
   * @return boostSources
   */
  @Valid 
  @Schema(name = "boostSources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostSources")
  public List<@Valid QueuePriorityStateBoostSourcesInner> getBoostSources() {
    return boostSources;
  }

  public void setBoostSources(List<@Valid QueuePriorityStateBoostSourcesInner> boostSources) {
    this.boostSources = boostSources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueuePriorityState queuePriorityState = (QueuePriorityState) o;
    return Objects.equals(this.ticketId, queuePriorityState.ticketId) &&
        Objects.equals(this.priority, queuePriorityState.priority) &&
        Objects.equals(this.boostedUntil, queuePriorityState.boostedUntil) &&
        Objects.equals(this.boostSources, queuePriorityState.boostSources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, priority, boostedUntil, boostSources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueuePriorityState {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    boostedUntil: ").append(toIndentedString(boostedUntil)).append("\n");
    sb.append("    boostSources: ").append(toIndentedString(boostSources)).append("\n");
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

