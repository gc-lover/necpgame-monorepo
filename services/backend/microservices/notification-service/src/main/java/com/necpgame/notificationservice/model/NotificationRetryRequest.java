package com.necpgame.notificationservice.model;

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
 * NotificationRetryRequest
 */


public class NotificationRetryRequest {

  private @Nullable String reason;

  private @Nullable Boolean force;

  public NotificationRetryRequest reason(@Nullable String reason) {
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

  public NotificationRetryRequest force(@Nullable Boolean force) {
    this.force = force;
    return this;
  }

  /**
   * Get force
   * @return force
   */
  
  @Schema(name = "force", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("force")
  public @Nullable Boolean getForce() {
    return force;
  }

  public void setForce(@Nullable Boolean force) {
    this.force = force;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationRetryRequest notificationRetryRequest = (NotificationRetryRequest) o;
    return Objects.equals(this.reason, notificationRetryRequest.reason) &&
        Objects.equals(this.force, notificationRetryRequest.force);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, force);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationRetryRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    force: ").append(toIndentedString(force)).append("\n");
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

