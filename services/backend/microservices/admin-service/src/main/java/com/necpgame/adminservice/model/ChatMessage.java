package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChatMessage
 */


public class ChatMessage {

  private @Nullable UUID messageId;

  private @Nullable String channel;

  private @Nullable UUID senderId;

  private @Nullable String senderName;

  private @Nullable String content;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable Boolean flagged;

  private JsonNullable<String> flagReason = JsonNullable.<String>undefined();

  public ChatMessage messageId(@Nullable UUID messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
   */
  @Valid 
  @Schema(name = "message_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message_id")
  public @Nullable UUID getMessageId() {
    return messageId;
  }

  public void setMessageId(@Nullable UUID messageId) {
    this.messageId = messageId;
  }

  public ChatMessage channel(@Nullable String channel) {
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

  public ChatMessage senderId(@Nullable UUID senderId) {
    this.senderId = senderId;
    return this;
  }

  /**
   * Get senderId
   * @return senderId
   */
  @Valid 
  @Schema(name = "sender_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sender_id")
  public @Nullable UUID getSenderId() {
    return senderId;
  }

  public void setSenderId(@Nullable UUID senderId) {
    this.senderId = senderId;
  }

  public ChatMessage senderName(@Nullable String senderName) {
    this.senderName = senderName;
    return this;
  }

  /**
   * Get senderName
   * @return senderName
   */
  
  @Schema(name = "sender_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sender_name")
  public @Nullable String getSenderName() {
    return senderName;
  }

  public void setSenderName(@Nullable String senderName) {
    this.senderName = senderName;
  }

  public ChatMessage content(@Nullable String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   */
  
  @Schema(name = "content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public @Nullable String getContent() {
    return content;
  }

  public void setContent(@Nullable String content) {
    this.content = content;
  }

  public ChatMessage timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ChatMessage flagged(@Nullable Boolean flagged) {
    this.flagged = flagged;
    return this;
  }

  /**
   * Get flagged
   * @return flagged
   */
  
  @Schema(name = "flagged", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flagged")
  public @Nullable Boolean getFlagged() {
    return flagged;
  }

  public void setFlagged(@Nullable Boolean flagged) {
    this.flagged = flagged;
  }

  public ChatMessage flagReason(String flagReason) {
    this.flagReason = JsonNullable.of(flagReason);
    return this;
  }

  /**
   * Get flagReason
   * @return flagReason
   */
  
  @Schema(name = "flag_reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flag_reason")
  public JsonNullable<String> getFlagReason() {
    return flagReason;
  }

  public void setFlagReason(JsonNullable<String> flagReason) {
    this.flagReason = flagReason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatMessage chatMessage = (ChatMessage) o;
    return Objects.equals(this.messageId, chatMessage.messageId) &&
        Objects.equals(this.channel, chatMessage.channel) &&
        Objects.equals(this.senderId, chatMessage.senderId) &&
        Objects.equals(this.senderName, chatMessage.senderName) &&
        Objects.equals(this.content, chatMessage.content) &&
        Objects.equals(this.timestamp, chatMessage.timestamp) &&
        Objects.equals(this.flagged, chatMessage.flagged) &&
        equalsNullable(this.flagReason, chatMessage.flagReason);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, channel, senderId, senderName, content, timestamp, flagged, hashCodeNullable(flagReason));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    senderId: ").append(toIndentedString(senderId)).append("\n");
    sb.append("    senderName: ").append(toIndentedString(senderName)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    flagged: ").append(toIndentedString(flagged)).append("\n");
    sb.append("    flagReason: ").append(toIndentedString(flagReason)).append("\n");
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

