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
 * NotificationBatchResponse
 */


public class NotificationBatchResponse {

  private @Nullable String batchId;

  private @Nullable Integer acceptedRecipients;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduledAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    QUEUED("QUEUED"),
    
    PROCESSING("PROCESSING"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED");

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

  private @Nullable StatusEnum status;

  public NotificationBatchResponse batchId(@Nullable String batchId) {
    this.batchId = batchId;
    return this;
  }

  /**
   * Get batchId
   * @return batchId
   */
  
  @Schema(name = "batchId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("batchId")
  public @Nullable String getBatchId() {
    return batchId;
  }

  public void setBatchId(@Nullable String batchId) {
    this.batchId = batchId;
  }

  public NotificationBatchResponse acceptedRecipients(@Nullable Integer acceptedRecipients) {
    this.acceptedRecipients = acceptedRecipients;
    return this;
  }

  /**
   * Get acceptedRecipients
   * @return acceptedRecipients
   */
  
  @Schema(name = "acceptedRecipients", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acceptedRecipients")
  public @Nullable Integer getAcceptedRecipients() {
    return acceptedRecipients;
  }

  public void setAcceptedRecipients(@Nullable Integer acceptedRecipients) {
    this.acceptedRecipients = acceptedRecipients;
  }

  public NotificationBatchResponse scheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledAt")
  public @Nullable OffsetDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  public NotificationBatchResponse status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationBatchResponse notificationBatchResponse = (NotificationBatchResponse) o;
    return Objects.equals(this.batchId, notificationBatchResponse.batchId) &&
        Objects.equals(this.acceptedRecipients, notificationBatchResponse.acceptedRecipients) &&
        Objects.equals(this.scheduledAt, notificationBatchResponse.scheduledAt) &&
        Objects.equals(this.status, notificationBatchResponse.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(batchId, acceptedRecipients, scheduledAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationBatchResponse {\n");
    sb.append("    batchId: ").append(toIndentedString(batchId)).append("\n");
    sb.append("    acceptedRecipients: ").append(toIndentedString(acceptedRecipients)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

