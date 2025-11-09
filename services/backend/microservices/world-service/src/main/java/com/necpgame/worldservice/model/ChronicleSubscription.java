package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChronicleSubscriptionRequestFilters;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ChronicleSubscription
 */


public class ChronicleSubscription {

  private UUID subscriberId;

  /**
   * Gets or Sets subscriberType
   */
  public enum SubscriberTypeEnum {
    PLAYER("player"),
    
    GUILD("guild"),
    
    FACTION("faction");

    private final String value;

    SubscriberTypeEnum(String value) {
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
    public static SubscriberTypeEnum fromValue(String value) {
      for (SubscriberTypeEnum b : SubscriberTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SubscriberTypeEnum subscriberType;

  private ChronicleSubscriptionRequestFilters filters;

  /**
   * Gets or Sets deliveryChannels
   */
  public enum DeliveryChannelsEnum {
    WEBSOCKET("websocket"),
    
    NOTIFICATION("notification"),
    
    EMAIL("email");

    private final String value;

    DeliveryChannelsEnum(String value) {
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
    public static DeliveryChannelsEnum fromValue(String value) {
      for (DeliveryChannelsEnum b : DeliveryChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<DeliveryChannelsEnum> deliveryChannels = new ArrayList<>(Arrays.asList(DeliveryChannelsEnum.WEBSOCKET, DeliveryChannelsEnum.NOTIFICATION));

  private Integer throttleSeconds = 60;

  private UUID subscriptionId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    PAUSED("paused"),
    
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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public ChronicleSubscription() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChronicleSubscription(UUID subscriberId, SubscriberTypeEnum subscriberType, ChronicleSubscriptionRequestFilters filters, UUID subscriptionId, StatusEnum status, OffsetDateTime createdAt) {
    this.subscriberId = subscriberId;
    this.subscriberType = subscriberType;
    this.filters = filters;
    this.subscriptionId = subscriptionId;
    this.status = status;
    this.createdAt = createdAt;
  }

  public ChronicleSubscription subscriberId(UUID subscriberId) {
    this.subscriberId = subscriberId;
    return this;
  }

  /**
   * Get subscriberId
   * @return subscriberId
   */
  @NotNull @Valid 
  @Schema(name = "subscriberId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subscriberId")
  public UUID getSubscriberId() {
    return subscriberId;
  }

  public void setSubscriberId(UUID subscriberId) {
    this.subscriberId = subscriberId;
  }

  public ChronicleSubscription subscriberType(SubscriberTypeEnum subscriberType) {
    this.subscriberType = subscriberType;
    return this;
  }

  /**
   * Get subscriberType
   * @return subscriberType
   */
  @NotNull 
  @Schema(name = "subscriberType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subscriberType")
  public SubscriberTypeEnum getSubscriberType() {
    return subscriberType;
  }

  public void setSubscriberType(SubscriberTypeEnum subscriberType) {
    this.subscriberType = subscriberType;
  }

  public ChronicleSubscription filters(ChronicleSubscriptionRequestFilters filters) {
    this.filters = filters;
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  @NotNull @Valid 
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("filters")
  public ChronicleSubscriptionRequestFilters getFilters() {
    return filters;
  }

  public void setFilters(ChronicleSubscriptionRequestFilters filters) {
    this.filters = filters;
  }

  public ChronicleSubscription deliveryChannels(List<DeliveryChannelsEnum> deliveryChannels) {
    this.deliveryChannels = deliveryChannels;
    return this;
  }

  public ChronicleSubscription addDeliveryChannelsItem(DeliveryChannelsEnum deliveryChannelsItem) {
    if (this.deliveryChannels == null) {
      this.deliveryChannels = new ArrayList<>(Arrays.asList(DeliveryChannelsEnum.WEBSOCKET, DeliveryChannelsEnum.NOTIFICATION));
    }
    this.deliveryChannels.add(deliveryChannelsItem);
    return this;
  }

  /**
   * Get deliveryChannels
   * @return deliveryChannels
   */
  
  @Schema(name = "deliveryChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveryChannels")
  public List<DeliveryChannelsEnum> getDeliveryChannels() {
    return deliveryChannels;
  }

  public void setDeliveryChannels(List<DeliveryChannelsEnum> deliveryChannels) {
    this.deliveryChannels = deliveryChannels;
  }

  public ChronicleSubscription throttleSeconds(Integer throttleSeconds) {
    this.throttleSeconds = throttleSeconds;
    return this;
  }

  /**
   * Get throttleSeconds
   * minimum: 0
   * maximum: 3600
   * @return throttleSeconds
   */
  @Min(value = 0) @Max(value = 3600) 
  @Schema(name = "throttleSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("throttleSeconds")
  public Integer getThrottleSeconds() {
    return throttleSeconds;
  }

  public void setThrottleSeconds(Integer throttleSeconds) {
    this.throttleSeconds = throttleSeconds;
  }

  public ChronicleSubscription subscriptionId(UUID subscriptionId) {
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

  public ChronicleSubscription status(StatusEnum status) {
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

  public ChronicleSubscription createdAt(OffsetDateTime createdAt) {
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

  public ChronicleSubscription updatedAt(@Nullable OffsetDateTime updatedAt) {
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
    ChronicleSubscription chronicleSubscription = (ChronicleSubscription) o;
    return Objects.equals(this.subscriberId, chronicleSubscription.subscriberId) &&
        Objects.equals(this.subscriberType, chronicleSubscription.subscriberType) &&
        Objects.equals(this.filters, chronicleSubscription.filters) &&
        Objects.equals(this.deliveryChannels, chronicleSubscription.deliveryChannels) &&
        Objects.equals(this.throttleSeconds, chronicleSubscription.throttleSeconds) &&
        Objects.equals(this.subscriptionId, chronicleSubscription.subscriptionId) &&
        Objects.equals(this.status, chronicleSubscription.status) &&
        Objects.equals(this.createdAt, chronicleSubscription.createdAt) &&
        Objects.equals(this.updatedAt, chronicleSubscription.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriberId, subscriberType, filters, deliveryChannels, throttleSeconds, subscriptionId, status, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleSubscription {\n");
    sb.append("    subscriberId: ").append(toIndentedString(subscriberId)).append("\n");
    sb.append("    subscriberType: ").append(toIndentedString(subscriberType)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    deliveryChannels: ").append(toIndentedString(deliveryChannels)).append("\n");
    sb.append("    throttleSeconds: ").append(toIndentedString(throttleSeconds)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

