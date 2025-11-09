package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.notificationservice.model.NotificationSendRequestOverrides;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationSendRequest
 */


public class NotificationSendRequest {

  private String recipientId;

  /**
   * Gets or Sets channel
   */
  public enum ChannelEnum {
    IN_GAME("IN_GAME"),
    
    PUSH("PUSH"),
    
    EMAIL("EMAIL"),
    
    SMS("SMS");

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

  private String templateId;

  private @Nullable String locale;

  @Valid
  private Map<String, Object> data = new HashMap<>();

  private @Nullable String idempotencyKey;

  private @Nullable String dedupeToken;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("LOW"),
    
    NORMAL("NORMAL"),
    
    HIGH("HIGH"),
    
    CRITICAL("CRITICAL");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PriorityEnum priority;

  private @Nullable NotificationSendRequestOverrides overrides;

  public NotificationSendRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationSendRequest(String recipientId, ChannelEnum channel, String templateId) {
    this.recipientId = recipientId;
    this.channel = channel;
    this.templateId = templateId;
  }

  public NotificationSendRequest recipientId(String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

  /**
   * Get recipientId
   * @return recipientId
   */
  @NotNull 
  @Schema(name = "recipientId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recipientId")
  public String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(String recipientId) {
    this.recipientId = recipientId;
  }

  public NotificationSendRequest channel(ChannelEnum channel) {
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

  public NotificationSendRequest templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  @NotNull 
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public NotificationSendRequest locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public NotificationSendRequest data(Map<String, Object> data) {
    this.data = data;
    return this;
  }

  public NotificationSendRequest putDataItem(String key, Object dataItem) {
    if (this.data == null) {
      this.data = new HashMap<>();
    }
    this.data.put(key, dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  
  @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public NotificationSendRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  public NotificationSendRequest dedupeToken(@Nullable String dedupeToken) {
    this.dedupeToken = dedupeToken;
    return this;
  }

  /**
   * Get dedupeToken
   * @return dedupeToken
   */
  
  @Schema(name = "dedupeToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dedupeToken")
  public @Nullable String getDedupeToken() {
    return dedupeToken;
  }

  public void setDedupeToken(@Nullable String dedupeToken) {
    this.dedupeToken = dedupeToken;
  }

  public NotificationSendRequest priority(@Nullable PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(@Nullable PriorityEnum priority) {
    this.priority = priority;
  }

  public NotificationSendRequest overrides(@Nullable NotificationSendRequestOverrides overrides) {
    this.overrides = overrides;
    return this;
  }

  /**
   * Get overrides
   * @return overrides
   */
  @Valid 
  @Schema(name = "overrides", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrides")
  public @Nullable NotificationSendRequestOverrides getOverrides() {
    return overrides;
  }

  public void setOverrides(@Nullable NotificationSendRequestOverrides overrides) {
    this.overrides = overrides;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationSendRequest notificationSendRequest = (NotificationSendRequest) o;
    return Objects.equals(this.recipientId, notificationSendRequest.recipientId) &&
        Objects.equals(this.channel, notificationSendRequest.channel) &&
        Objects.equals(this.templateId, notificationSendRequest.templateId) &&
        Objects.equals(this.locale, notificationSendRequest.locale) &&
        Objects.equals(this.data, notificationSendRequest.data) &&
        Objects.equals(this.idempotencyKey, notificationSendRequest.idempotencyKey) &&
        Objects.equals(this.dedupeToken, notificationSendRequest.dedupeToken) &&
        Objects.equals(this.priority, notificationSendRequest.priority) &&
        Objects.equals(this.overrides, notificationSendRequest.overrides);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipientId, channel, templateId, locale, data, idempotencyKey, dedupeToken, priority, overrides);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationSendRequest {\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
    sb.append("    dedupeToken: ").append(toIndentedString(dedupeToken)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    overrides: ").append(toIndentedString(overrides)).append("\n");
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

