package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Vector3;
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
 * LagCompensationRequest
 */


public class LagCompensationRequest {

  private String eventId;

  private String participantId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime clientTimestamp;

  private @Nullable Vector3 reportedPosition;

  private @Nullable Integer latencyMs;

  public LagCompensationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LagCompensationRequest(String eventId, String participantId) {
    this.eventId = eventId;
    this.participantId = participantId;
  }

  public LagCompensationRequest eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public LagCompensationRequest participantId(String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  @NotNull 
  @Schema(name = "participantId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("participantId")
  public String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(String participantId) {
    this.participantId = participantId;
  }

  public LagCompensationRequest clientTimestamp(@Nullable OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
    return this;
  }

  /**
   * Get clientTimestamp
   * @return clientTimestamp
   */
  @Valid 
  @Schema(name = "clientTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clientTimestamp")
  public @Nullable OffsetDateTime getClientTimestamp() {
    return clientTimestamp;
  }

  public void setClientTimestamp(@Nullable OffsetDateTime clientTimestamp) {
    this.clientTimestamp = clientTimestamp;
  }

  public LagCompensationRequest reportedPosition(@Nullable Vector3 reportedPosition) {
    this.reportedPosition = reportedPosition;
    return this;
  }

  /**
   * Get reportedPosition
   * @return reportedPosition
   */
  @Valid 
  @Schema(name = "reportedPosition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reportedPosition")
  public @Nullable Vector3 getReportedPosition() {
    return reportedPosition;
  }

  public void setReportedPosition(@Nullable Vector3 reportedPosition) {
    this.reportedPosition = reportedPosition;
  }

  public LagCompensationRequest latencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
    return this;
  }

  /**
   * Get latencyMs
   * @return latencyMs
   */
  
  @Schema(name = "latencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyMs")
  public @Nullable Integer getLatencyMs() {
    return latencyMs;
  }

  public void setLatencyMs(@Nullable Integer latencyMs) {
    this.latencyMs = latencyMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LagCompensationRequest lagCompensationRequest = (LagCompensationRequest) o;
    return Objects.equals(this.eventId, lagCompensationRequest.eventId) &&
        Objects.equals(this.participantId, lagCompensationRequest.participantId) &&
        Objects.equals(this.clientTimestamp, lagCompensationRequest.clientTimestamp) &&
        Objects.equals(this.reportedPosition, lagCompensationRequest.reportedPosition) &&
        Objects.equals(this.latencyMs, lagCompensationRequest.latencyMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, participantId, clientTimestamp, reportedPosition, latencyMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LagCompensationRequest {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    clientTimestamp: ").append(toIndentedString(clientTimestamp)).append("\n");
    sb.append("    reportedPosition: ").append(toIndentedString(reportedPosition)).append("\n");
    sb.append("    latencyMs: ").append(toIndentedString(latencyMs)).append("\n");
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

