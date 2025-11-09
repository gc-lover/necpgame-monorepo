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
 * DeliveryStatus
 */


public class DeliveryStatus {

  private @Nullable String deliveryId;

  private @Nullable String notificationId;

  private @Nullable String channel;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    QUEUED("QUEUED"),
    
    SENT("SENT"),
    
    DELIVERED("DELIVERED"),
    
    FAILED("FAILED"),
    
    RETRYING("RETRYING");

    private final String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StateEnum state;

  private @Nullable Integer attempts;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastAttemptAt;

  private @Nullable String error;

  public DeliveryStatus deliveryId(@Nullable String deliveryId) {
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

  public DeliveryStatus notificationId(@Nullable String notificationId) {
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

  public DeliveryStatus channel(@Nullable String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel")
  public @Nullable String getChannel() {
    return channel;
  }

  public void setChannel(@Nullable String channel) {
    this.channel = channel;
  }

  public DeliveryStatus state(@Nullable StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable StateEnum getState() {
    return state;
  }

  public void setState(@Nullable StateEnum state) {
    this.state = state;
  }

  public DeliveryStatus attempts(@Nullable Integer attempts) {
    this.attempts = attempts;
    return this;
  }

  /**
   * Get attempts
   * @return attempts
   */
  
  @Schema(name = "attempts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attempts")
  public @Nullable Integer getAttempts() {
    return attempts;
  }

  public void setAttempts(@Nullable Integer attempts) {
    this.attempts = attempts;
  }

  public DeliveryStatus lastAttemptAt(@Nullable OffsetDateTime lastAttemptAt) {
    this.lastAttemptAt = lastAttemptAt;
    return this;
  }

  /**
   * Get lastAttemptAt
   * @return lastAttemptAt
   */
  @Valid 
  @Schema(name = "lastAttemptAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastAttemptAt")
  public @Nullable OffsetDateTime getLastAttemptAt() {
    return lastAttemptAt;
  }

  public void setLastAttemptAt(@Nullable OffsetDateTime lastAttemptAt) {
    this.lastAttemptAt = lastAttemptAt;
  }

  public DeliveryStatus error(@Nullable String error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
   */
  
  @Schema(name = "error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public @Nullable String getError() {
    return error;
  }

  public void setError(@Nullable String error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliveryStatus deliveryStatus = (DeliveryStatus) o;
    return Objects.equals(this.deliveryId, deliveryStatus.deliveryId) &&
        Objects.equals(this.notificationId, deliveryStatus.notificationId) &&
        Objects.equals(this.channel, deliveryStatus.channel) &&
        Objects.equals(this.state, deliveryStatus.state) &&
        Objects.equals(this.attempts, deliveryStatus.attempts) &&
        Objects.equals(this.lastAttemptAt, deliveryStatus.lastAttemptAt) &&
        Objects.equals(this.error, deliveryStatus.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, notificationId, channel, state, attempts, lastAttemptAt, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliveryStatus {\n");
    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    notificationId: ").append(toIndentedString(notificationId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    attempts: ").append(toIndentedString(attempts)).append("\n");
    sb.append("    lastAttemptAt: ").append(toIndentedString(lastAttemptAt)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

