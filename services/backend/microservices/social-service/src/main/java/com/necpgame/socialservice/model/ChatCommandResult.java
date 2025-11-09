package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.CommandStatus;
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
 * ChatCommandResult
 */


public class ChatCommandResult {

  private CommandStatus status;

  private @Nullable String message;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  private @Nullable Integer cooldownSeconds;

  public ChatCommandResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChatCommandResult(CommandStatus status) {
    this.status = status;
  }

  public ChatCommandResult status(CommandStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public CommandStatus getStatus() {
    return status;
  }

  public void setStatus(CommandStatus status) {
    this.status = status;
  }

  public ChatCommandResult message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public ChatCommandResult payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public ChatCommandResult putPayloadItem(String key, Object payloadItem) {
    if (this.payload == null) {
      this.payload = new HashMap<>();
    }
    this.payload.put(key, payloadItem);
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payload")
  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  public ChatCommandResult cooldownSeconds(@Nullable Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
    return this;
  }

  /**
   * Get cooldownSeconds
   * minimum: 0
   * @return cooldownSeconds
   */
  @Min(value = 0) 
  @Schema(name = "cooldownSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownSeconds")
  public @Nullable Integer getCooldownSeconds() {
    return cooldownSeconds;
  }

  public void setCooldownSeconds(@Nullable Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatCommandResult chatCommandResult = (ChatCommandResult) o;
    return Objects.equals(this.status, chatCommandResult.status) &&
        Objects.equals(this.message, chatCommandResult.message) &&
        Objects.equals(this.payload, chatCommandResult.payload) &&
        Objects.equals(this.cooldownSeconds, chatCommandResult.cooldownSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, message, payload, cooldownSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatCommandResult {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    cooldownSeconds: ").append(toIndentedString(cooldownSeconds)).append("\n");
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

