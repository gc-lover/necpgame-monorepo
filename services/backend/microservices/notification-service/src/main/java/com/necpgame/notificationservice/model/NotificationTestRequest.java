package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.notificationservice.model.NotificationTestRequestRecipient;
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
 * NotificationTestRequest
 */


public class NotificationTestRequest {

  private String templateId;

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

  private @Nullable NotificationTestRequestRecipient recipient;

  @Valid
  private Map<String, Object> data = new HashMap<>();

  public NotificationTestRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationTestRequest(String templateId, ChannelEnum channel) {
    this.templateId = templateId;
    this.channel = channel;
  }

  public NotificationTestRequest templateId(String templateId) {
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

  public NotificationTestRequest channel(ChannelEnum channel) {
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

  public NotificationTestRequest recipient(@Nullable NotificationTestRequestRecipient recipient) {
    this.recipient = recipient;
    return this;
  }

  /**
   * Get recipient
   * @return recipient
   */
  @Valid 
  @Schema(name = "recipient", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipient")
  public @Nullable NotificationTestRequestRecipient getRecipient() {
    return recipient;
  }

  public void setRecipient(@Nullable NotificationTestRequestRecipient recipient) {
    this.recipient = recipient;
  }

  public NotificationTestRequest data(Map<String, Object> data) {
    this.data = data;
    return this;
  }

  public NotificationTestRequest putDataItem(String key, Object dataItem) {
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
    NotificationTestRequest notificationTestRequest = (NotificationTestRequest) o;
    return Objects.equals(this.templateId, notificationTestRequest.templateId) &&
        Objects.equals(this.channel, notificationTestRequest.channel) &&
        Objects.equals(this.recipient, notificationTestRequest.recipient) &&
        Objects.equals(this.data, notificationTestRequest.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, channel, recipient, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTestRequest {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
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

