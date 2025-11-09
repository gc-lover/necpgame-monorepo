package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.QueueEntryDetail;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * QueueSnapshot
 */


public class QueueSnapshot {

  private UUID ticketId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime snapshotTakenAt;

  private @Nullable QueueEntryDetail queueState;

  @Valid
  private Map<String, Object> rawPayload = new HashMap<>();

  public QueueSnapshot() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueSnapshot(UUID ticketId, OffsetDateTime snapshotTakenAt) {
    this.ticketId = ticketId;
    this.snapshotTakenAt = snapshotTakenAt;
  }

  public QueueSnapshot ticketId(UUID ticketId) {
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

  public QueueSnapshot snapshotTakenAt(OffsetDateTime snapshotTakenAt) {
    this.snapshotTakenAt = snapshotTakenAt;
    return this;
  }

  /**
   * Get snapshotTakenAt
   * @return snapshotTakenAt
   */
  @NotNull @Valid 
  @Schema(name = "snapshotTakenAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("snapshotTakenAt")
  public OffsetDateTime getSnapshotTakenAt() {
    return snapshotTakenAt;
  }

  public void setSnapshotTakenAt(OffsetDateTime snapshotTakenAt) {
    this.snapshotTakenAt = snapshotTakenAt;
  }

  public QueueSnapshot queueState(@Nullable QueueEntryDetail queueState) {
    this.queueState = queueState;
    return this;
  }

  /**
   * Get queueState
   * @return queueState
   */
  @Valid 
  @Schema(name = "queueState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueState")
  public @Nullable QueueEntryDetail getQueueState() {
    return queueState;
  }

  public void setQueueState(@Nullable QueueEntryDetail queueState) {
    this.queueState = queueState;
  }

  public QueueSnapshot rawPayload(Map<String, Object> rawPayload) {
    this.rawPayload = rawPayload;
    return this;
  }

  public QueueSnapshot putRawPayloadItem(String key, Object rawPayloadItem) {
    if (this.rawPayload == null) {
      this.rawPayload = new HashMap<>();
    }
    this.rawPayload.put(key, rawPayloadItem);
    return this;
  }

  /**
   * Get rawPayload
   * @return rawPayload
   */
  
  @Schema(name = "rawPayload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rawPayload")
  public Map<String, Object> getRawPayload() {
    return rawPayload;
  }

  public void setRawPayload(Map<String, Object> rawPayload) {
    this.rawPayload = rawPayload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueSnapshot queueSnapshot = (QueueSnapshot) o;
    return Objects.equals(this.ticketId, queueSnapshot.ticketId) &&
        Objects.equals(this.snapshotTakenAt, queueSnapshot.snapshotTakenAt) &&
        Objects.equals(this.queueState, queueSnapshot.queueState) &&
        Objects.equals(this.rawPayload, queueSnapshot.rawPayload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, snapshotTakenAt, queueState, rawPayload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueSnapshot {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    snapshotTakenAt: ").append(toIndentedString(snapshotTakenAt)).append("\n");
    sb.append("    queueState: ").append(toIndentedString(queueState)).append("\n");
    sb.append("    rawPayload: ").append(toIndentedString(rawPayload)).append("\n");
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

