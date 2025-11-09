package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SessionAbortRequest
 */


public class SessionAbortRequest {

  private @Nullable String reason;

  private @Nullable String incidentId;

  private @Nullable Boolean preserveRewards;

  public SessionAbortRequest reason(@Nullable String reason) {
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

  public SessionAbortRequest incidentId(@Nullable String incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  
  @Schema(name = "incidentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incidentId")
  public @Nullable String getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(@Nullable String incidentId) {
    this.incidentId = incidentId;
  }

  public SessionAbortRequest preserveRewards(@Nullable Boolean preserveRewards) {
    this.preserveRewards = preserveRewards;
    return this;
  }

  /**
   * Get preserveRewards
   * @return preserveRewards
   */
  
  @Schema(name = "preserveRewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preserveRewards")
  public @Nullable Boolean getPreserveRewards() {
    return preserveRewards;
  }

  public void setPreserveRewards(@Nullable Boolean preserveRewards) {
    this.preserveRewards = preserveRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionAbortRequest sessionAbortRequest = (SessionAbortRequest) o;
    return Objects.equals(this.reason, sessionAbortRequest.reason) &&
        Objects.equals(this.incidentId, sessionAbortRequest.incidentId) &&
        Objects.equals(this.preserveRewards, sessionAbortRequest.preserveRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, incidentId, preserveRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionAbortRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    preserveRewards: ").append(toIndentedString(preserveRewards)).append("\n");
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

