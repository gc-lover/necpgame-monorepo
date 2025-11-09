package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.RiskAlertThreshold;
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
 * RiskAlertSubscription
 */


public class RiskAlertSubscription {

  private UUID subscriptionId;

  private UUID subscriberId;

  /**
   * Gets or Sets channels
   */
  public enum ChannelsEnum {
    EMAIL("email"),
    
    SMS("sms"),
    
    PUSH("push"),
    
    WEBHOOK("webhook");

    private final String value;

    ChannelsEnum(String value) {
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
    public static ChannelsEnum fromValue(String value) {
      for (ChannelsEnum b : ChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<ChannelsEnum> channels = new ArrayList<>();

  @Valid
  private List<@Valid RiskAlertThreshold> thresholds = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public RiskAlertSubscription() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskAlertSubscription(UUID subscriptionId, UUID subscriberId, List<@Valid RiskAlertThreshold> thresholds) {
    this.subscriptionId = subscriptionId;
    this.subscriberId = subscriberId;
    this.thresholds = thresholds;
  }

  public RiskAlertSubscription subscriptionId(UUID subscriptionId) {
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

  public RiskAlertSubscription subscriberId(UUID subscriberId) {
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

  public RiskAlertSubscription channels(List<ChannelsEnum> channels) {
    this.channels = channels;
    return this;
  }

  public RiskAlertSubscription addChannelsItem(ChannelsEnum channelsItem) {
    if (this.channels == null) {
      this.channels = new ArrayList<>();
    }
    this.channels.add(channelsItem);
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channels")
  public List<ChannelsEnum> getChannels() {
    return channels;
  }

  public void setChannels(List<ChannelsEnum> channels) {
    this.channels = channels;
  }

  public RiskAlertSubscription thresholds(List<@Valid RiskAlertThreshold> thresholds) {
    this.thresholds = thresholds;
    return this;
  }

  public RiskAlertSubscription addThresholdsItem(RiskAlertThreshold thresholdsItem) {
    if (this.thresholds == null) {
      this.thresholds = new ArrayList<>();
    }
    this.thresholds.add(thresholdsItem);
    return this;
  }

  /**
   * Get thresholds
   * @return thresholds
   */
  @NotNull @Valid 
  @Schema(name = "thresholds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("thresholds")
  public List<@Valid RiskAlertThreshold> getThresholds() {
    return thresholds;
  }

  public void setThresholds(List<@Valid RiskAlertThreshold> thresholds) {
    this.thresholds = thresholds;
  }

  public RiskAlertSubscription createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskAlertSubscription riskAlertSubscription = (RiskAlertSubscription) o;
    return Objects.equals(this.subscriptionId, riskAlertSubscription.subscriptionId) &&
        Objects.equals(this.subscriberId, riskAlertSubscription.subscriberId) &&
        Objects.equals(this.channels, riskAlertSubscription.channels) &&
        Objects.equals(this.thresholds, riskAlertSubscription.thresholds) &&
        Objects.equals(this.createdAt, riskAlertSubscription.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, subscriberId, channels, thresholds, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskAlertSubscription {\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    subscriberId: ").append(toIndentedString(subscriberId)).append("\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    thresholds: ").append(toIndentedString(thresholds)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

