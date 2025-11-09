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
 * NotificationSendResponse
 */


public class NotificationSendResponse {

  private @Nullable String notificationId;

  private @Nullable String deliveryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime queuedAt;

  public NotificationSendResponse notificationId(@Nullable String notificationId) {
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

  public NotificationSendResponse deliveryId(@Nullable String deliveryId) {
    this.deliveryId = deliveryId;
    return this;
  }

  /**
   * Get deliveryId
   * @return deliveryId
   */
  
  @Schema(name = "deliveryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveryId")
  public @Nullable String getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(@Nullable String deliveryId) {
    this.deliveryId = deliveryId;
  }

  public NotificationSendResponse queuedAt(@Nullable OffsetDateTime queuedAt) {
    this.queuedAt = queuedAt;
    return this;
  }

  /**
   * Get queuedAt
   * @return queuedAt
   */
  @Valid 
  @Schema(name = "queuedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queuedAt")
  public @Nullable OffsetDateTime getQueuedAt() {
    return queuedAt;
  }

  public void setQueuedAt(@Nullable OffsetDateTime queuedAt) {
    this.queuedAt = queuedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendResponse notificationSendResponse = (NotificationSendResponse) o;
    return Objects.equals(this.notificationId, notificationSendResponse.notificationId) &&
        Objects.equals(this.deliveryId, notificationSendResponse.deliveryId) &&
        Objects.equals(this.queuedAt, notificationSendResponse.queuedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificationId, deliveryId, queuedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendResponse {\n");
    sb.append("    notificationId: ").append(toIndentedString(notificationId)).append("\n");
    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    queuedAt: ").append(toIndentedString(queuedAt)).append("\n");
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

