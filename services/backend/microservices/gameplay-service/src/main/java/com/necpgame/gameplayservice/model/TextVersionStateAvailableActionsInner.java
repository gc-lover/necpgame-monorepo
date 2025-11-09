package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TextVersionStateAvailableActionsInner
 */

@JsonTypeName("TextVersionState_available_actions_inner")

public class TextVersionStateAvailableActionsInner {

  private @Nullable String action;

  private @Nullable String description;

  private @Nullable String command;

  public TextVersionStateAvailableActionsInner action(@Nullable String action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable String getAction() {
    return action;
  }

  public void setAction(@Nullable String action) {
    this.action = action;
  }

  public TextVersionStateAvailableActionsInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public TextVersionStateAvailableActionsInner command(@Nullable String command) {
    this.command = command;
    return this;
  }

  /**
   * Get command
   * @return command
   */
  
  @Schema(name = "command", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("command")
  public @Nullable String getCommand() {
    return command;
  }

  public void setCommand(@Nullable String command) {
    this.command = command;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextVersionStateAvailableActionsInner textVersionStateAvailableActionsInner = (TextVersionStateAvailableActionsInner) o;
    return Objects.equals(this.action, textVersionStateAvailableActionsInner.action) &&
        Objects.equals(this.description, textVersionStateAvailableActionsInner.description) &&
        Objects.equals(this.command, textVersionStateAvailableActionsInner.command);
  }

  @Override
  public int hashCode() {
    return Objects.hash(action, description, command);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextVersionStateAvailableActionsInner {\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    command: ").append(toIndentedString(command)).append("\n");
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

