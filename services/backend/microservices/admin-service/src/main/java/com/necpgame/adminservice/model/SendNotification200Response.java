package com.necpgame.adminservice.model;

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
 * SendNotification200Response
 */

@JsonTypeName("sendNotification_200_response")

public class SendNotification200Response {

  private @Nullable String notificationId;

  public SendNotification200Response notificationId(@Nullable String notificationId) {
    this.notificationId = notificationId;
    return this;
  }

  /**
   * Get notificationId
   * @return notificationId
   */
  
  @Schema(name = "notification_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notification_id")
  public @Nullable String getNotificationId() {
    return notificationId;
  }

  public void setNotificationId(@Nullable String notificationId) {
    this.notificationId = notificationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendNotification200Response sendNotification200Response = (SendNotification200Response) o;
    return Objects.equals(this.notificationId, sendNotification200Response.notificationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendNotification200Response {\n");
    sb.append("    notificationId: ").append(toIndentedString(notificationId)).append("\n");
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

