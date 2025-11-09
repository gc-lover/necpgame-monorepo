package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderNewsSubscriptionFilters;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * PlayerOrderNewsSubscription
 */


public class PlayerOrderNewsSubscription {

  private UUID subscriptionId;

  /**
   * Gets or Sets channel
   */
  public enum ChannelEnum {
    PUSH("push"),
    
    EMAIL("email"),
    
    WEBHOOK("webhook"),
    
    IN_GAME("in_game");

    private final String value;

    ChannelEnum(String value) {
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
    public static ChannelEnum fromValue(String value) {
      for (ChannelEnum b : ChannelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ChannelEnum channel;

  private @Nullable PlayerOrderNewsSubscriptionFilters filters;

  private @Nullable URI webhookUrl;

  private @Nullable String deliveryWindow;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    PAUSED("paused"),
    
    MUTED("muted"),
    
    CANCELLED("cancelled");

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

  public PlayerOrderNewsSubscription() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsSubscription(UUID subscriptionId, ChannelEnum channel, OffsetDateTime createdAt) {
    this.subscriptionId = subscriptionId;
    this.channel = channel;
    this.createdAt = createdAt;
  }

  public PlayerOrderNewsSubscription subscriptionId(UUID subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * Get subscriptionId
   * @return subscriptionId
   */
  @NotNull @Valid 
  @Schema(name = "subscriptionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subscriptionId")
  public UUID getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(UUID subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public PlayerOrderNewsSubscription channel(ChannelEnum channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  @NotNull 
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channel")
  public ChannelEnum getChannel() {
    return channel;
  }

  public void setChannel(ChannelEnum channel) {
    this.channel = channel;
  }

  public PlayerOrderNewsSubscription filters(@Nullable PlayerOrderNewsSubscriptionFilters filters) {
    this.filters = filters;
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  @Valid 
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public @Nullable PlayerOrderNewsSubscriptionFilters getFilters() {
    return filters;
  }

  public void setFilters(@Nullable PlayerOrderNewsSubscriptionFilters filters) {
    this.filters = filters;
  }

  public PlayerOrderNewsSubscription webhookUrl(@Nullable URI webhookUrl) {
    this.webhookUrl = webhookUrl;
    return this;
  }

  /**
   * Get webhookUrl
   * @return webhookUrl
   */
  @Valid 
  @Schema(name = "webhookUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("webhookUrl")
  public @Nullable URI getWebhookUrl() {
    return webhookUrl;
  }

  public void setWebhookUrl(@Nullable URI webhookUrl) {
    this.webhookUrl = webhookUrl;
  }

  public PlayerOrderNewsSubscription deliveryWindow(@Nullable String deliveryWindow) {
    this.deliveryWindow = deliveryWindow;
    return this;
  }

  /**
   * Get deliveryWindow
   * @return deliveryWindow
   */
  
  @Schema(name = "deliveryWindow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveryWindow")
  public @Nullable String getDeliveryWindow() {
    return deliveryWindow;
  }

  public void setDeliveryWindow(@Nullable String deliveryWindow) {
    this.deliveryWindow = deliveryWindow;
  }

  public PlayerOrderNewsSubscription createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PlayerOrderNewsSubscription updatedAt(@Nullable OffsetDateTime updatedAt) {
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

  public PlayerOrderNewsSubscription status(@Nullable StatusEnum status) {
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
    PlayerOrderNewsSubscription playerOrderNewsSubscription = (PlayerOrderNewsSubscription) o;
    return Objects.equals(this.subscriptionId, playerOrderNewsSubscription.subscriptionId) &&
        Objects.equals(this.channel, playerOrderNewsSubscription.channel) &&
        Objects.equals(this.filters, playerOrderNewsSubscription.filters) &&
        Objects.equals(this.webhookUrl, playerOrderNewsSubscription.webhookUrl) &&
        Objects.equals(this.deliveryWindow, playerOrderNewsSubscription.deliveryWindow) &&
        Objects.equals(this.createdAt, playerOrderNewsSubscription.createdAt) &&
        Objects.equals(this.updatedAt, playerOrderNewsSubscription.updatedAt) &&
        Objects.equals(this.status, playerOrderNewsSubscription.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, channel, filters, webhookUrl, deliveryWindow, createdAt, updatedAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsSubscription {\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    webhookUrl: ").append(toIndentedString(webhookUrl)).append("\n");
    sb.append("    deliveryWindow: ").append(toIndentedString(deliveryWindow)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

