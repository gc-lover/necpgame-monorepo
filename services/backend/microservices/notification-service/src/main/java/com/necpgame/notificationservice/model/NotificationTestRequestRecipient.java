package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationTestRequestRecipient
 */

@JsonTypeName("NotificationTestRequest_recipient")

public class NotificationTestRequestRecipient {

  private @Nullable String playerId;

  private @Nullable String email;

  private @Nullable String deviceToken;

  public NotificationTestRequestRecipient playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public NotificationTestRequestRecipient email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public NotificationTestRequestRecipient deviceToken(@Nullable String deviceToken) {
    this.deviceToken = deviceToken;
    return this;
  }

  /**
   * Get deviceToken
   * @return deviceToken
   */
  
  @Schema(name = "deviceToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deviceToken")
  public @Nullable String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(@Nullable String deviceToken) {
    this.deviceToken = deviceToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationTestRequestRecipient notificationTestRequestRecipient = (NotificationTestRequestRecipient) o;
    return Objects.equals(this.playerId, notificationTestRequestRecipient.playerId) &&
        Objects.equals(this.email, notificationTestRequestRecipient.email) &&
        Objects.equals(this.deviceToken, notificationTestRequestRecipient.deviceToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, email, deviceToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTestRequestRecipient {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    deviceToken: ").append(toIndentedString(deviceToken)).append("\n");
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

