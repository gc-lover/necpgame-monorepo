package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.ChatMessage;
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
 * GetChatLogs200Response
 */

@JsonTypeName("getChatLogs_200_response")

public class GetChatLogs200Response {

  @Valid
  private List<@Valid ChatMessage> messages = new ArrayList<>();

  public GetChatLogs200Response messages(List<@Valid ChatMessage> messages) {
    this.messages = messages;
    return this;
  }

  public GetChatLogs200Response addMessagesItem(ChatMessage messagesItem) {
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
  @Valid 
  @Schema(name = "messages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messages")
  public List<@Valid ChatMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<@Valid ChatMessage> messages) {
    this.messages = messages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetChatLogs200Response getChatLogs200Response = (GetChatLogs200Response) o;
    return Objects.equals(this.messages, getChatLogs200Response.messages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetChatLogs200Response {\n");
    sb.append("    messages: ").append(toIndentedString(messages)).append("\n");
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

