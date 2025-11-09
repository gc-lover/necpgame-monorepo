package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partyservice.model.PartyQueueStatus;
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
 * PartyQueueStatusEvent
 */


public class PartyQueueStatusEvent {

  private @Nullable String partyId;

  private @Nullable PartyQueueStatus queueStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  public PartyQueueStatusEvent partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public PartyQueueStatusEvent queueStatus(@Nullable PartyQueueStatus queueStatus) {
    this.queueStatus = queueStatus;
    return this;
  }

  /**
   * Get queueStatus
   * @return queueStatus
   */
  @Valid 
  @Schema(name = "queueStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueStatus")
  public @Nullable PartyQueueStatus getQueueStatus() {
    return queueStatus;
  }

  public void setQueueStatus(@Nullable PartyQueueStatus queueStatus) {
    this.queueStatus = queueStatus;
  }

  public PartyQueueStatusEvent timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyQueueStatusEvent partyQueueStatusEvent = (PartyQueueStatusEvent) o;
    return Objects.equals(this.partyId, partyQueueStatusEvent.partyId) &&
        Objects.equals(this.queueStatus, partyQueueStatusEvent.queueStatus) &&
        Objects.equals(this.timestamp, partyQueueStatusEvent.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, queueStatus, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyQueueStatusEvent {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    queueStatus: ").append(toIndentedString(queueStatus)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

