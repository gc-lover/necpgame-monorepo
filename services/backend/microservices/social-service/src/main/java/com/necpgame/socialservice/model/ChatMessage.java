package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.MessageMetadata;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * ChatMessage
 */


public class ChatMessage {

  private UUID messageId;

  private UUID senderId;

  private @Nullable String displayName;

  private String content;

  private @Nullable String rawContent;

  @Valid
  private Map<String, String> translatedContent = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime sentAt;

  private @Nullable MessageMetadata metadata;

  public ChatMessage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatMessage(UUID messageId, UUID senderId, String content, OffsetDateTime sentAt) {
    this.messageId = messageId;
    this.senderId = senderId;
    this.content = content;
    this.sentAt = sentAt;
  }

  public ChatMessage messageId(UUID messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
   */
  @NotNull @Valid 
  @Schema(name = "messageId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("messageId")
  public UUID getMessageId() {
    return messageId;
  }

  public void setMessageId(UUID messageId) {
    this.messageId = messageId;
  }

  public ChatMessage senderId(UUID senderId) {
    this.senderId = senderId;
    return this;
  }

  /**
   * Get senderId
   * @return senderId
   */
  @NotNull @Valid 
  @Schema(name = "senderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("senderId")
  public UUID getSenderId() {
    return senderId;
  }

  public void setSenderId(UUID senderId) {
    this.senderId = senderId;
  }

  public ChatMessage displayName(@Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public @Nullable String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@Nullable String displayName) {
    this.displayName = displayName;
  }

  public ChatMessage content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Get content
   * @return content
   */
  @NotNull 
  @Schema(name = "content", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public ChatMessage rawContent(@Nullable String rawContent) {
    this.rawContent = rawContent;
    return this;
  }

  /**
   * Get rawContent
   * @return rawContent
   */
  
  @Schema(name = "rawContent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rawContent")
  public @Nullable String getRawContent() {
    return rawContent;
  }

  public void setRawContent(@Nullable String rawContent) {
    this.rawContent = rawContent;
  }

  public ChatMessage translatedContent(Map<String, String> translatedContent) {
    this.translatedContent = translatedContent;
    return this;
  }

  public ChatMessage putTranslatedContentItem(String key, String translatedContentItem) {
    if (this.translatedContent == null) {
      this.translatedContent = new HashMap<>();
    }
    this.translatedContent.put(key, translatedContentItem);
    return this;
  }

  /**
   * Get translatedContent
   * @return translatedContent
   */
  
  @Schema(name = "translatedContent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("translatedContent")
  public Map<String, String> getTranslatedContent() {
    return translatedContent;
  }

  public void setTranslatedContent(Map<String, String> translatedContent) {
    this.translatedContent = translatedContent;
  }

  public ChatMessage sentAt(OffsetDateTime sentAt) {
    this.sentAt = sentAt;
    return this;
  }

  /**
   * Get sentAt
   * @return sentAt
   */
  @NotNull @Valid 
  @Schema(name = "sentAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sentAt")
  public OffsetDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(OffsetDateTime sentAt) {
    this.sentAt = sentAt;
  }

  public ChatMessage metadata(@Nullable MessageMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  @Valid 
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public @Nullable MessageMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(@Nullable MessageMetadata metadata) {
    this.metadata = metadata;
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
        Objects.equals(this.senderId, chatMessage.senderId) &&
        Objects.equals(this.displayName, chatMessage.displayName) &&
        Objects.equals(this.content, chatMessage.content) &&
        Objects.equals(this.rawContent, chatMessage.rawContent) &&
        Objects.equals(this.translatedContent, chatMessage.translatedContent) &&
        Objects.equals(this.sentAt, chatMessage.sentAt) &&
        Objects.equals(this.metadata, chatMessage.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, senderId, displayName, content, rawContent, translatedContent, sentAt, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    senderId: ").append(toIndentedString(senderId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    rawContent: ").append(toIndentedString(rawContent)).append("\n");
    sb.append("    translatedContent: ").append(toIndentedString(translatedContent)).append("\n");
    sb.append("    sentAt: ").append(toIndentedString(sentAt)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

