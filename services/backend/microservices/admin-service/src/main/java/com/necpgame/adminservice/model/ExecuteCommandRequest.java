package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ExecuteCommandRequest
 */

@JsonTypeName("executeCommand_request")

public class ExecuteCommandRequest {

  private String characterId;

  private String command;

  @Valid
  private List<String> arguments = new ArrayList<>();

  public ExecuteCommandRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExecuteCommandRequest(String characterId, String command) {
    this.characterId = characterId;
    this.command = command;
  }

  public ExecuteCommandRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public ExecuteCommandRequest command(String command) {
    this.command = command;
    return this;
  }

  /**
   * Get command
   * @return command
   */
  @NotNull 
  @Schema(name = "command", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("command")
  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public ExecuteCommandRequest arguments(List<String> arguments) {
    this.arguments = arguments;
    return this;
  }

  public ExecuteCommandRequest addArgumentsItem(String argumentsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteCommandRequest executeCommandRequest = (ExecuteCommandRequest) o;
    return Objects.equals(this.characterId, executeCommandRequest.characterId) &&
        Objects.equals(this.command, executeCommandRequest.command) &&
        Objects.equals(this.arguments, executeCommandRequest.arguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, command, arguments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteCommandRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    command: ").append(toIndentedString(command)).append("\n");
    sb.append("    arguments: ").append(toIndentedString(arguments)).append("\n");
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

