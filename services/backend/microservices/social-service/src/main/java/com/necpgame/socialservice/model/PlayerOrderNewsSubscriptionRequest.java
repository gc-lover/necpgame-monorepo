package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderNewsTag;
import java.net.URI;
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
 * PlayerOrderNewsSubscriptionRequest
 */


public class PlayerOrderNewsSubscriptionRequest {

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

  @Valid
  private List<UUID> cityIds = new ArrayList<>();

  @Valid
  private List<@Valid PlayerOrderNewsTag> tags = new ArrayList<>();

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    CAUTION("caution"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<SeverityEnum> severity = new ArrayList<>();

  private @Nullable String language;

  private @Nullable String deliveryWindow;

  private @Nullable URI webhookUrl;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime muteUntil;

  public PlayerOrderNewsSubscriptionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsSubscriptionRequest(ChannelEnum channel) {
    this.channel = channel;
  }

  public PlayerOrderNewsSubscriptionRequest channel(ChannelEnum channel) {
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

  public PlayerOrderNewsSubscriptionRequest cityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
    return this;
  }

  public PlayerOrderNewsSubscriptionRequest addCityIdsItem(UUID cityIdsItem) {
    if (this.cityIds == null) {
      this.cityIds = new ArrayList<>();
    }
    this.cityIds.add(cityIdsItem);
    return this;
  }

  /**
   * Get cityIds
   * @return cityIds
   */
  @Valid 
  @Schema(name = "cityIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cityIds")
  public List<UUID> getCityIds() {
    return cityIds;
  }

  public void setCityIds(List<UUID> cityIds) {
    this.cityIds = cityIds;
  }

  public PlayerOrderNewsSubscriptionRequest tags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
    return this;
  }

  public PlayerOrderNewsSubscriptionRequest addTagsItem(PlayerOrderNewsTag tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  @Valid 
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<@Valid PlayerOrderNewsTag> getTags() {
    return tags;
  }

  public void setTags(List<@Valid PlayerOrderNewsTag> tags) {
    this.tags = tags;
  }

  public PlayerOrderNewsSubscriptionRequest severity(List<SeverityEnum> severity) {
    this.severity = severity;
    return this;
  }

  public PlayerOrderNewsSubscriptionRequest addSeverityItem(SeverityEnum severityItem) {
    if (this.severity == null) {
      this.severity = new ArrayList<>();
    }
    this.severity.add(severityItem);
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public List<SeverityEnum> getSeverity() {
    return severity;
  }

  public void setSeverity(List<SeverityEnum> severity) {
    this.severity = severity;
  }

  public PlayerOrderNewsSubscriptionRequest language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  public PlayerOrderNewsSubscriptionRequest deliveryWindow(@Nullable String deliveryWindow) {
    this.deliveryWindow = deliveryWindow;
    return this;
  }

  /**
   * Get deliveryWindow
   * @return deliveryWindow
   */
  @Pattern(regexp = "^([01]\\\\d|2[0-3]):[0-5]\\\\d-([01]\\\\d|2[0-3]):[0-5]\\\\d$") 
  @Schema(name = "deliveryWindow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveryWindow")
  public @Nullable String getDeliveryWindow() {
    return deliveryWindow;
  }

  public void setDeliveryWindow(@Nullable String deliveryWindow) {
    this.deliveryWindow = deliveryWindow;
  }

  public PlayerOrderNewsSubscriptionRequest webhookUrl(@Nullable URI webhookUrl) {
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

  public PlayerOrderNewsSubscriptionRequest muteUntil(@Nullable OffsetDateTime muteUntil) {
    this.muteUntil = muteUntil;
    return this;
  }

  /**
   * Get muteUntil
   * @return muteUntil
   */
  @Valid 
  @Schema(name = "muteUntil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("muteUntil")
  public @Nullable OffsetDateTime getMuteUntil() {
    return muteUntil;
  }

  public void setMuteUntil(@Nullable OffsetDateTime muteUntil) {
    this.muteUntil = muteUntil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsSubscriptionRequest playerOrderNewsSubscriptionRequest = (PlayerOrderNewsSubscriptionRequest) o;
    return Objects.equals(this.channel, playerOrderNewsSubscriptionRequest.channel) &&
        Objects.equals(this.cityIds, playerOrderNewsSubscriptionRequest.cityIds) &&
        Objects.equals(this.tags, playerOrderNewsSubscriptionRequest.tags) &&
        Objects.equals(this.severity, playerOrderNewsSubscriptionRequest.severity) &&
        Objects.equals(this.language, playerOrderNewsSubscriptionRequest.language) &&
        Objects.equals(this.deliveryWindow, playerOrderNewsSubscriptionRequest.deliveryWindow) &&
        Objects.equals(this.webhookUrl, playerOrderNewsSubscriptionRequest.webhookUrl) &&
        Objects.equals(this.muteUntil, playerOrderNewsSubscriptionRequest.muteUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channel, cityIds, tags, severity, language, deliveryWindow, webhookUrl, muteUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsSubscriptionRequest {\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    cityIds: ").append(toIndentedString(cityIds)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    deliveryWindow: ").append(toIndentedString(deliveryWindow)).append("\n");
    sb.append("    webhookUrl: ").append(toIndentedString(webhookUrl)).append("\n");
    sb.append("    muteUntil: ").append(toIndentedString(muteUntil)).append("\n");
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

