package com.necpgame.adminservice.model;

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
 * AlertAck
 */


public class AlertAck {

  private String alertId;

  private UUID ackBy;

  private @Nullable String reason;

  private Boolean resolved = false;

  public AlertAck() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AlertAck(String alertId, UUID ackBy) {
    this.alertId = alertId;
    this.ackBy = ackBy;
  }

  public AlertAck alertId(String alertId) {
    this.alertId = alertId;
    return this;
  }

  /**
   * Get alertId
   * @return alertId
   */
  @NotNull 
  @Schema(name = "alertId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("alertId")
  public String getAlertId() {
    return alertId;
  }

  public void setAlertId(String alertId) {
    this.alertId = alertId;
  }

  public AlertAck ackBy(UUID ackBy) {
    this.ackBy = ackBy;
    return this;
  }

  /**
   * Get ackBy
   * @return ackBy
   */
  @NotNull @Valid 
  @Schema(name = "ackBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ackBy")
  public UUID getAckBy() {
    return ackBy;
  }

  public void setAckBy(UUID ackBy) {
    this.ackBy = ackBy;
  }

  public AlertAck reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public AlertAck resolved(Boolean resolved) {
    this.resolved = resolved;
    return this;
  }

  /**
   * Get resolved
   * @return resolved
   */
  
  @Schema(name = "resolved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved")
  public Boolean getResolved() {
    return resolved;
  }

  public void setResolved(Boolean resolved) {
    this.resolved = resolved;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertAck alertAck = (AlertAck) o;
    return Objects.equals(this.alertId, alertAck.alertId) &&
        Objects.equals(this.ackBy, alertAck.ackBy) &&
        Objects.equals(this.reason, alertAck.reason) &&
        Objects.equals(this.resolved, alertAck.resolved);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, ackBy, reason, resolved);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertAck {\n");
    sb.append("    alertId: ").append(toIndentedString(alertId)).append("\n");
    sb.append("    ackBy: ").append(toIndentedString(ackBy)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    resolved: ").append(toIndentedString(resolved)).append("\n");
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

