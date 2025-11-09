package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SubscribeToStateEvents200Response
 */

@JsonTypeName("subscribeToStateEvents_200_response")

public class SubscribeToStateEvents200Response {

  private @Nullable UUID subscriptionId;

  private @Nullable String websocketUrl;

  public SubscribeToStateEvents200Response subscriptionId(@Nullable UUID subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * Get subscriptionId
   * @return subscriptionId
   */
  @Valid 
  @Schema(name = "subscription_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subscription_id")
  public @Nullable UUID getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(@Nullable UUID subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public SubscribeToStateEvents200Response websocketUrl(@Nullable String websocketUrl) {
    this.websocketUrl = websocketUrl;
    return this;
  }

  /**
   * Get websocketUrl
   * @return websocketUrl
   */
  
  @Schema(name = "websocket_url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("websocket_url")
  public @Nullable String getWebsocketUrl() {
    return websocketUrl;
  }

  public void setWebsocketUrl(@Nullable String websocketUrl) {
    this.websocketUrl = websocketUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscribeToStateEvents200Response subscribeToStateEvents200Response = (SubscribeToStateEvents200Response) o;
    return Objects.equals(this.subscriptionId, subscribeToStateEvents200Response.subscriptionId) &&
        Objects.equals(this.websocketUrl, subscribeToStateEvents200Response.websocketUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, websocketUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscribeToStateEvents200Response {\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    websocketUrl: ").append(toIndentedString(websocketUrl)).append("\n");
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

