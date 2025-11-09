package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * CloseSession200Response
 */

@JsonTypeName("closeSession_200_response")

public class CloseSession200Response {

  private @Nullable String sessionId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime closedAt;

  private @Nullable BigDecimal duration;

  public CloseSession200Response sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public CloseSession200Response closedAt(@Nullable OffsetDateTime closedAt) {
    this.closedAt = closedAt;
    return this;
  }

  /**
   * Get closedAt
   * @return closedAt
   */
  @Valid 
  @Schema(name = "closed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("closed_at")
  public @Nullable OffsetDateTime getClosedAt() {
    return closedAt;
  }

  public void setClosedAt(@Nullable OffsetDateTime closedAt) {
    this.closedAt = closedAt;
  }

  public CloseSession200Response duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность сессии (секунды)
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", description = "Длительность сессии (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CloseSession200Response closeSession200Response = (CloseSession200Response) o;
    return Objects.equals(this.sessionId, closeSession200Response.sessionId) &&
        Objects.equals(this.closedAt, closeSession200Response.closedAt) &&
        Objects.equals(this.duration, closeSession200Response.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, closedAt, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseSession200Response {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    closedAt: ").append(toIndentedString(closedAt)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

