package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CancellationRequest
 */


public class CancellationRequest {

  private String reason;

  @Valid
  private List<String> notifyChannels = new ArrayList<>();

  private @Nullable String incidentTicketId;

  public CancellationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CancellationRequest(String reason) {
    this.reason = reason;
  }

  public CancellationRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull @Size(max = 280) 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public CancellationRequest notifyChannels(List<String> notifyChannels) {
    this.notifyChannels = notifyChannels;
    return this;
  }

  public CancellationRequest addNotifyChannelsItem(String notifyChannelsItem) {
    if (this.notifyChannels == null) {
      this.notifyChannels = new ArrayList<>();
    }
    this.notifyChannels.add(notifyChannelsItem);
    return this;
  }

  /**
   * Get notifyChannels
   * @return notifyChannels
   */
  
  @Schema(name = "notifyChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyChannels")
  public List<String> getNotifyChannels() {
    return notifyChannels;
  }

  public void setNotifyChannels(List<String> notifyChannels) {
    this.notifyChannels = notifyChannels;
  }

  public CancellationRequest incidentTicketId(@Nullable String incidentTicketId) {
    this.incidentTicketId = incidentTicketId;
    return this;
  }

  /**
   * Get incidentTicketId
   * @return incidentTicketId
   */
  
  @Schema(name = "incidentTicketId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incidentTicketId")
  public @Nullable String getIncidentTicketId() {
    return incidentTicketId;
  }

  public void setIncidentTicketId(@Nullable String incidentTicketId) {
    this.incidentTicketId = incidentTicketId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancellationRequest cancellationRequest = (CancellationRequest) o;
    return Objects.equals(this.reason, cancellationRequest.reason) &&
        Objects.equals(this.notifyChannels, cancellationRequest.notifyChannels) &&
        Objects.equals(this.incidentTicketId, cancellationRequest.incidentTicketId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, notifyChannels, incidentTicketId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancellationRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    notifyChannels: ").append(toIndentedString(notifyChannels)).append("\n");
    sb.append("    incidentTicketId: ").append(toIndentedString(incidentTicketId)).append("\n");
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

