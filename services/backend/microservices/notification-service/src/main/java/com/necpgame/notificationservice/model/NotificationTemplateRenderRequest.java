package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * NotificationTemplateRenderRequest
 */


public class NotificationTemplateRenderRequest {

  private String templateId;

  private String locale;

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

  private @Nullable ChannelEnum channel;

  @Valid
  private Map<String, Object> data = new HashMap<>();

  public NotificationTemplateRenderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationTemplateRenderRequest(String templateId, String locale) {
    this.templateId = templateId;
    this.locale = locale;
  }

  public NotificationTemplateRenderRequest templateId(String templateId) {
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

  public NotificationTemplateRenderRequest locale(String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  @NotNull 
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locale")
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public NotificationTemplateRenderRequest channel(@Nullable ChannelEnum channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel")
  public @Nullable ChannelEnum getChannel() {
    return channel;
  }

  public void setChannel(@Nullable ChannelEnum channel) {
    this.channel = channel;
  }

  public NotificationTemplateRenderRequest data(Map<String, Object> data) {
    this.data = data;
    return this;
  }

  public NotificationTemplateRenderRequest putDataItem(String key, Object dataItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationTemplateRenderRequest notificationTemplateRenderRequest = (NotificationTemplateRenderRequest) o;
    return Objects.equals(this.templateId, notificationTemplateRenderRequest.templateId) &&
        Objects.equals(this.locale, notificationTemplateRenderRequest.locale) &&
        Objects.equals(this.channel, notificationTemplateRenderRequest.channel) &&
        Objects.equals(this.data, notificationTemplateRenderRequest.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, locale, channel, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTemplateRenderRequest {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

