package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ChatCommandRequestContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChatCommandRequest
 */


public class ChatCommandRequest {

  private String command;

  @Valid
  private List<String> arguments = new ArrayList<>();

  private @Nullable String channelId;

  private @Nullable UUID targetPlayerId;

  private @Nullable ChatCommandRequestContext context;

  public ChatCommandRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatCommandRequest(String command) {
    this.command = command;
  }

  public ChatCommandRequest command(String command) {
    this.command = command;
    return this;
  }

  /**
   * Get command
   * @return command
   */
  @NotNull @Pattern(regexp = "^/[a-zA-Z]+") 
  @Schema(name = "command", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("command")
  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public ChatCommandRequest arguments(List<String> arguments) {
    this.arguments = arguments;
    return this;
  }

  public ChatCommandRequest addArgumentsItem(String argumentsItem) {
    if (this.arguments == null) {
      this.arguments = new ArrayList<>();
    }
    this.arguments.add(argumentsItem);
    return this;
  }

  /**
   * Get arguments
   * @return arguments
   */
  
  @Schema(name = "arguments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arguments")
  public List<String> getArguments() {
    return arguments;
  }

  public void setArguments(List<String> arguments) {
    this.arguments = arguments;
  }

  public ChatCommandRequest channelId(@Nullable String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelId")
  public @Nullable String getChannelId() {
    return channelId;
  }

  public void setChannelId(@Nullable String channelId) {
    this.channelId = channelId;
  }

  public ChatCommandRequest targetPlayerId(@Nullable UUID targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
    return this;
  }

  /**
   * Get targetPlayerId
   * @return targetPlayerId
   */
  @Valid 
  @Schema(name = "targetPlayerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetPlayerId")
  public @Nullable UUID getTargetPlayerId() {
    return targetPlayerId;
  }

  public void setTargetPlayerId(@Nullable UUID targetPlayerId) {
    this.targetPlayerId = targetPlayerId;
  }

  public ChatCommandRequest context(@Nullable ChatCommandRequestContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable ChatCommandRequestContext getContext() {
    return context;
  }

  public void setContext(@Nullable ChatCommandRequestContext context) {
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatCommandRequest chatCommandRequest = (ChatCommandRequest) o;
    return Objects.equals(this.command, chatCommandRequest.command) &&
        Objects.equals(this.arguments, chatCommandRequest.arguments) &&
        Objects.equals(this.channelId, chatCommandRequest.channelId) &&
        Objects.equals(this.targetPlayerId, chatCommandRequest.targetPlayerId) &&
        Objects.equals(this.context, chatCommandRequest.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(command, arguments, channelId, targetPlayerId, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatCommandRequest {\n");
    sb.append("    command: ").append(toIndentedString(command)).append("\n");
    sb.append("    arguments: ").append(toIndentedString(arguments)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    targetPlayerId: ").append(toIndentedString(targetPlayerId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

