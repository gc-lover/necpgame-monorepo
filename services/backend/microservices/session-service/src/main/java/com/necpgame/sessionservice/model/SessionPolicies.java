package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.sessionservice.model.ConcurrentSessionPolicy;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SessionPolicies
 */


public class SessionPolicies {

  private @Nullable Integer heartbeatIntervalSeconds;

  private @Nullable Integer heartbeatGraceSeconds;

  private @Nullable Integer afkThresholdSeconds;

  private @Nullable Integer afkDisconnectSeconds;

  private @Nullable ConcurrentSessionPolicy concurrentPolicy;

  public SessionPolicies heartbeatIntervalSeconds(@Nullable Integer heartbeatIntervalSeconds) {
    this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
    return this;
  }

  /**
   * Get heartbeatIntervalSeconds
   * @return heartbeatIntervalSeconds
   */
  
  @Schema(name = "heartbeatIntervalSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heartbeatIntervalSeconds")
  public @Nullable Integer getHeartbeatIntervalSeconds() {
    return heartbeatIntervalSeconds;
  }

  public void setHeartbeatIntervalSeconds(@Nullable Integer heartbeatIntervalSeconds) {
    this.heartbeatIntervalSeconds = heartbeatIntervalSeconds;
  }

  public SessionPolicies heartbeatGraceSeconds(@Nullable Integer heartbeatGraceSeconds) {
    this.heartbeatGraceSeconds = heartbeatGraceSeconds;
    return this;
  }

  /**
   * Get heartbeatGraceSeconds
   * @return heartbeatGraceSeconds
   */
  
  @Schema(name = "heartbeatGraceSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heartbeatGraceSeconds")
  public @Nullable Integer getHeartbeatGraceSeconds() {
    return heartbeatGraceSeconds;
  }

  public void setHeartbeatGraceSeconds(@Nullable Integer heartbeatGraceSeconds) {
    this.heartbeatGraceSeconds = heartbeatGraceSeconds;
  }

  public SessionPolicies afkThresholdSeconds(@Nullable Integer afkThresholdSeconds) {
    this.afkThresholdSeconds = afkThresholdSeconds;
    return this;
  }

  /**
   * Get afkThresholdSeconds
   * @return afkThresholdSeconds
   */
  
  @Schema(name = "afkThresholdSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("afkThresholdSeconds")
  public @Nullable Integer getAfkThresholdSeconds() {
    return afkThresholdSeconds;
  }

  public void setAfkThresholdSeconds(@Nullable Integer afkThresholdSeconds) {
    this.afkThresholdSeconds = afkThresholdSeconds;
  }

  public SessionPolicies afkDisconnectSeconds(@Nullable Integer afkDisconnectSeconds) {
    this.afkDisconnectSeconds = afkDisconnectSeconds;
    return this;
  }

  /**
   * Get afkDisconnectSeconds
   * @return afkDisconnectSeconds
   */
  
  @Schema(name = "afkDisconnectSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("afkDisconnectSeconds")
  public @Nullable Integer getAfkDisconnectSeconds() {
    return afkDisconnectSeconds;
  }

  public void setAfkDisconnectSeconds(@Nullable Integer afkDisconnectSeconds) {
    this.afkDisconnectSeconds = afkDisconnectSeconds;
  }

  public SessionPolicies concurrentPolicy(@Nullable ConcurrentSessionPolicy concurrentPolicy) {
    this.concurrentPolicy = concurrentPolicy;
    return this;
  }

  /**
   * Get concurrentPolicy
   * @return concurrentPolicy
   */
  @Valid 
  @Schema(name = "concurrentPolicy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("concurrentPolicy")
  public @Nullable ConcurrentSessionPolicy getConcurrentPolicy() {
    return concurrentPolicy;
  }

  public void setConcurrentPolicy(@Nullable ConcurrentSessionPolicy concurrentPolicy) {
    this.concurrentPolicy = concurrentPolicy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionPolicies sessionPolicies = (SessionPolicies) o;
    return Objects.equals(this.heartbeatIntervalSeconds, sessionPolicies.heartbeatIntervalSeconds) &&
        Objects.equals(this.heartbeatGraceSeconds, sessionPolicies.heartbeatGraceSeconds) &&
        Objects.equals(this.afkThresholdSeconds, sessionPolicies.afkThresholdSeconds) &&
        Objects.equals(this.afkDisconnectSeconds, sessionPolicies.afkDisconnectSeconds) &&
        Objects.equals(this.concurrentPolicy, sessionPolicies.concurrentPolicy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(heartbeatIntervalSeconds, heartbeatGraceSeconds, afkThresholdSeconds, afkDisconnectSeconds, concurrentPolicy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionPolicies {\n");
    sb.append("    heartbeatIntervalSeconds: ").append(toIndentedString(heartbeatIntervalSeconds)).append("\n");
    sb.append("    heartbeatGraceSeconds: ").append(toIndentedString(heartbeatGraceSeconds)).append("\n");
    sb.append("    afkThresholdSeconds: ").append(toIndentedString(afkThresholdSeconds)).append("\n");
    sb.append("    afkDisconnectSeconds: ").append(toIndentedString(afkDisconnectSeconds)).append("\n");
    sb.append("    concurrentPolicy: ").append(toIndentedString(concurrentPolicy)).append("\n");
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

