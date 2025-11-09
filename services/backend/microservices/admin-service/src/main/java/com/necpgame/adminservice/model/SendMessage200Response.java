package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * SendMessage200Response
 */

@JsonTypeName("sendMessage_200_response")

public class SendMessage200Response {

  private @Nullable String messageId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime sentAt;

  private @Nullable String channel;

  public SendMessage200Response messageId(@Nullable String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
   */
  
  @Schema(name = "message_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message_id")
  public @Nullable String getMessageId() {
    return messageId;
  }

  public void setMessageId(@Nullable String messageId) {
    this.messageId = messageId;
  }

  public SendMessage200Response sentAt(@Nullable OffsetDateTime sentAt) {
    this.sentAt = sentAt;
    return this;
  }

  /**
   * Get sentAt
   * @return sentAt
   */
  @Valid 
  @Schema(name = "sent_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sent_at")
  public @Nullable OffsetDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(@Nullable OffsetDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public SendMessage200Response channel(@Nullable String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channel")
  public @Nullable String getChannel() {
    return channel;
  }

  public void setChannel(@Nullable String channel) {
    this.channel = channel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendMessage200Response sendMessage200Response = (SendMessage200Response) o;
    return Objects.equals(this.messageId, sendMessage200Response.messageId) &&
        Objects.equals(this.sentAt, sendMessage200Response.sentAt) &&
        Objects.equals(this.channel, sendMessage200Response.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, sentAt, channel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendMessage200Response {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    sentAt: ").append(toIndentedString(sentAt)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
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

