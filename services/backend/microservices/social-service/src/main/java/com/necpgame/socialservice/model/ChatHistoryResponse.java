package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChatMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChatHistoryResponse
 */


public class ChatHistoryResponse {

  private String channelId;

  private String channelType;

  @Valid
  private List<@Valid ChatMessage> messages = new ArrayList<>();

  private @Nullable Boolean hasMoreBefore;

  private @Nullable Boolean hasMoreAfter;

  private @Nullable String nextCursor;

  public ChatHistoryResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatHistoryResponse(String channelId, String channelType, List<@Valid ChatMessage> messages) {
    this.channelId = channelId;
    this.channelType = channelType;
    this.messages = messages;
  }

  public ChatHistoryResponse channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  @NotNull 
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelId")
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public ChatHistoryResponse channelType(String channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  @NotNull 
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelType")
  public String getChannelType() {
    return channelType;
  }

  public void setChannelType(String channelType) {
    this.channelType = channelType;
  }

  public ChatHistoryResponse messages(List<@Valid ChatMessage> messages) {
    this.messages = messages;
    return this;
  }

  public ChatHistoryResponse addMessagesItem(ChatMessage messagesItem) {
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    this.messages.add(messagesItem);
    return this;
  }

  /**
   * Get messages
   * @return messages
   */
  @NotNull @Valid 
  @Schema(name = "messages", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("messages")
  public List<@Valid ChatMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<@Valid ChatMessage> messages) {
    this.messages = messages;
  }

  public ChatHistoryResponse hasMoreBefore(@Nullable Boolean hasMoreBefore) {
    this.hasMoreBefore = hasMoreBefore;
    return this;
  }

  /**
   * Get hasMoreBefore
   * @return hasMoreBefore
   */
  
  @Schema(name = "hasMoreBefore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasMoreBefore")
  public @Nullable Boolean getHasMoreBefore() {
    return hasMoreBefore;
  }

  public void setHasMoreBefore(@Nullable Boolean hasMoreBefore) {
    this.hasMoreBefore = hasMoreBefore;
  }

  public ChatHistoryResponse hasMoreAfter(@Nullable Boolean hasMoreAfter) {
    this.hasMoreAfter = hasMoreAfter;
    return this;
  }

  /**
   * Get hasMoreAfter
   * @return hasMoreAfter
   */
  
  @Schema(name = "hasMoreAfter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasMoreAfter")
  public @Nullable Boolean getHasMoreAfter() {
    return hasMoreAfter;
  }

  public void setHasMoreAfter(@Nullable Boolean hasMoreAfter) {
    this.hasMoreAfter = hasMoreAfter;
  }

  public ChatHistoryResponse nextCursor(@Nullable String nextCursor) {
    this.nextCursor = nextCursor;
    return this;
  }

  /**
   * Get nextCursor
   * @return nextCursor
   */
  
  @Schema(name = "nextCursor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextCursor")
  public @Nullable String getNextCursor() {
    return nextCursor;
  }

  public void setNextCursor(@Nullable String nextCursor) {
    this.nextCursor = nextCursor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatHistoryResponse chatHistoryResponse = (ChatHistoryResponse) o;
    return Objects.equals(this.channelId, chatHistoryResponse.channelId) &&
        Objects.equals(this.channelType, chatHistoryResponse.channelType) &&
        Objects.equals(this.messages, chatHistoryResponse.messages) &&
        Objects.equals(this.hasMoreBefore, chatHistoryResponse.hasMoreBefore) &&
        Objects.equals(this.hasMoreAfter, chatHistoryResponse.hasMoreAfter) &&
        Objects.equals(this.nextCursor, chatHistoryResponse.nextCursor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelType, messages, hasMoreBefore, hasMoreAfter, nextCursor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatHistoryResponse {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    messages: ").append(toIndentedString(messages)).append("\n");
    sb.append("    hasMoreBefore: ").append(toIndentedString(hasMoreBefore)).append("\n");
    sb.append("    hasMoreAfter: ").append(toIndentedString(hasMoreAfter)).append("\n");
    sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
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

