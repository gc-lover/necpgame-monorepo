package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChronicleSubscriptionRequestFilters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ChronicleSubscriptionRequest
 */


public class ChronicleSubscriptionRequest {

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

  public ChronicleSubscriptionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChronicleSubscriptionRequest(UUID subscriberId, SubscriberTypeEnum subscriberType, ChronicleSubscriptionRequestFilters filters) {
    this.subscriberId = subscriberId;
    this.subscriberType = subscriberType;
    this.filters = filters;
  }

  public ChronicleSubscriptionRequest subscriberId(UUID subscriberId) {
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

  public ChronicleSubscriptionRequest subscriberType(SubscriberTypeEnum subscriberType) {
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

  public ChronicleSubscriptionRequest filters(ChronicleSubscriptionRequestFilters filters) {
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

  public ChronicleSubscriptionRequest deliveryChannels(List<DeliveryChannelsEnum> deliveryChannels) {
    this.deliveryChannels = deliveryChannels;
    return this;
  }

  public ChronicleSubscriptionRequest addDeliveryChannelsItem(DeliveryChannelsEnum deliveryChannelsItem) {
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

  public ChronicleSubscriptionRequest throttleSeconds(Integer throttleSeconds) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChronicleSubscriptionRequest chronicleSubscriptionRequest = (ChronicleSubscriptionRequest) o;
    return Objects.equals(this.subscriberId, chronicleSubscriptionRequest.subscriberId) &&
        Objects.equals(this.subscriberType, chronicleSubscriptionRequest.subscriberType) &&
        Objects.equals(this.filters, chronicleSubscriptionRequest.filters) &&
        Objects.equals(this.deliveryChannels, chronicleSubscriptionRequest.deliveryChannels) &&
        Objects.equals(this.throttleSeconds, chronicleSubscriptionRequest.throttleSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriberId, subscriberType, filters, deliveryChannels, throttleSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleSubscriptionRequest {\n");
    sb.append("    subscriberId: ").append(toIndentedString(subscriberId)).append("\n");
    sb.append("    subscriberType: ").append(toIndentedString(subscriberType)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
    sb.append("    deliveryChannels: ").append(toIndentedString(deliveryChannels)).append("\n");
    sb.append("    throttleSeconds: ").append(toIndentedString(throttleSeconds)).append("\n");
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

