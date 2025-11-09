package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * NotificationAckRequest
 */


public class NotificationAckRequest {

  private String notificationId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    READ("READ"),
    
    DISMISSED("DISMISSED");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime acknowledgedAt;

  public NotificationAckRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationAckRequest(String notificationId, StatusEnum status) {
    this.notificationId = notificationId;
    this.status = status;
  }

  public NotificationAckRequest notificationId(String notificationId) {
    this.notificationId = notificationId;
    return this;
  }

  /**
   * Get notificationId
   * @return notificationId
   */
  @NotNull 
  @Schema(name = "notificationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("notificationId")
  public String getNotificationId() {
    return notificationId;
  }

  public void setNotificationId(String notificationId) {
    this.notificationId = notificationId;
  }

  public NotificationAckRequest status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public NotificationAckRequest acknowledgedAt(@Nullable OffsetDateTime acknowledgedAt) {
    this.acknowledgedAt = acknowledgedAt;
    return this;
  }

  /**
   * Get acknowledgedAt
   * @return acknowledgedAt
   */
  @Valid 
  @Schema(name = "acknowledgedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acknowledgedAt")
  public @Nullable OffsetDateTime getAcknowledgedAt() {
    return acknowledgedAt;
  }

  public void setAcknowledgedAt(@Nullable OffsetDateTime acknowledgedAt) {
    this.acknowledgedAt = acknowledgedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationAckRequest notificationAckRequest = (NotificationAckRequest) o;
    return Objects.equals(this.notificationId, notificationAckRequest.notificationId) &&
        Objects.equals(this.status, notificationAckRequest.status) &&
        Objects.equals(this.acknowledgedAt, notificationAckRequest.acknowledgedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificationId, status, acknowledgedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationAckRequest {\n");
    sb.append("    notificationId: ").append(toIndentedString(notificationId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    acknowledgedAt: ").append(toIndentedString(acknowledgedAt)).append("\n");
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

