package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ReadyCheckCommand
 */


public class ReadyCheckCommand {

  private UUID initiatorId;

  private Integer expiresInSeconds;

  private @Nullable String message;

  public ReadyCheckCommand() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReadyCheckCommand(UUID initiatorId, Integer expiresInSeconds) {
    this.initiatorId = initiatorId;
    this.expiresInSeconds = expiresInSeconds;
  }

  public ReadyCheckCommand initiatorId(UUID initiatorId) {
    this.initiatorId = initiatorId;
    return this;
  }

  /**
   * Get initiatorId
   * @return initiatorId
   */
  @NotNull @Valid 
  @Schema(name = "initiatorId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("initiatorId")
  public UUID getInitiatorId() {
    return initiatorId;
  }

  public void setInitiatorId(UUID initiatorId) {
    this.initiatorId = initiatorId;
  }

  public ReadyCheckCommand expiresInSeconds(Integer expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
    return this;
  }

  /**
   * Get expiresInSeconds
   * minimum: 5
   * maximum: 45
   * @return expiresInSeconds
   */
  @NotNull @Min(value = 5) @Max(value = 45) 
  @Schema(name = "expiresInSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expiresInSeconds")
  public Integer getExpiresInSeconds() {
    return expiresInSeconds;
  }

  public void setExpiresInSeconds(Integer expiresInSeconds) {
    this.expiresInSeconds = expiresInSeconds;
  }

  public ReadyCheckCommand message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @Size(max = 200) 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadyCheckCommand readyCheckCommand = (ReadyCheckCommand) o;
    return Objects.equals(this.initiatorId, readyCheckCommand.initiatorId) &&
        Objects.equals(this.expiresInSeconds, readyCheckCommand.expiresInSeconds) &&
        Objects.equals(this.message, readyCheckCommand.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(initiatorId, expiresInSeconds, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckCommand {\n");
    sb.append("    initiatorId: ").append(toIndentedString(initiatorId)).append("\n");
    sb.append("    expiresInSeconds: ").append(toIndentedString(expiresInSeconds)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

