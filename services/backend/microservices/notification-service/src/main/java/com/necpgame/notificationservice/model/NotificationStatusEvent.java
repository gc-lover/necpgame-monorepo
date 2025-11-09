package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * NotificationStatusEvent
 */


public class NotificationStatusEvent {

  private @Nullable String notificationId;

  private @Nullable String status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public NotificationStatusEvent notificationId(@Nullable String notificationId) {
    this.notificationId = notificationId;
    return this;
  }

  /**
   * Get notificationId
   * @return notificationId
   */
  
  @Schema(name = "notificationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notificationId")
  public @Nullable String getNotificationId() {
    return notificationId;
  }

  public void setNotificationId(@Nullable String notificationId) {
    this.notificationId = notificationId;
  }

  public NotificationStatusEvent status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public NotificationStatusEvent updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationStatusEvent notificationStatusEvent = (NotificationStatusEvent) o;
    return Objects.equals(this.notificationId, notificationStatusEvent.notificationId) &&
        Objects.equals(this.status, notificationStatusEvent.status) &&
        Objects.equals(this.updatedAt, notificationStatusEvent.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificationId, status, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationStatusEvent {\n");
    sb.append("    notificationId: ").append(toIndentedString(notificationId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

