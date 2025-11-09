package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CommandCatalogEntry
 */


public class CommandCatalogEntry {

  private String command;

  private String description;

  private @Nullable String scope;

  @Valid
  private List<String> arguments = new ArrayList<>();

  @Valid
  private List<String> examples = new ArrayList<>();

  public CommandCatalogEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CommandCatalogEntry(String command, String description) {
    this.command = command;
    this.description = description;
  }

  public CommandCatalogEntry command(String command) {
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

  public CommandCatalogEntry description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CommandCatalogEntry scope(@Nullable String scope) {
    this.scope = scope;
    return this;
  }

  /**
   * Get scope
   * @return scope
   */
  
  @Schema(name = "scope", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scope")
  public @Nullable String getScope() {
    return scope;
  }

  public void setScope(@Nullable String scope) {
    this.scope = scope;
  }

  public CommandCatalogEntry arguments(List<String> arguments) {
    this.arguments = arguments;
    return this;
  }

  public CommandCatalogEntry addArgumentsItem(String argumentsItem) {
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

  public CommandCatalogEntry examples(List<String> examples) {
    this.examples = examples;
    return this;
  }

  public CommandCatalogEntry addExamplesItem(String examplesItem) {
    if (this.examples == null) {
      this.examples = new ArrayList<>();
    }
    this.examples.add(examplesItem);
    return this;
  }

  /**
   * Get examples
   * @return examples
   */
  
  @Schema(name = "examples", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("examples")
  public List<String> getExamples() {
    return examples;
  }

  public void setExamples(List<String> examples) {
    this.examples = examples;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommandCatalogEntry commandCatalogEntry = (CommandCatalogEntry) o;
    return Objects.equals(this.command, commandCatalogEntry.command) &&
        Objects.equals(this.description, commandCatalogEntry.description) &&
        Objects.equals(this.scope, commandCatalogEntry.scope) &&
        Objects.equals(this.arguments, commandCatalogEntry.arguments) &&
        Objects.equals(this.examples, commandCatalogEntry.examples);
  }

  @Override
  public int hashCode() {
    return Objects.hash(command, description, scope, arguments, examples);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommandCatalogEntry {\n");
    sb.append("    command: ").append(toIndentedString(command)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    arguments: ").append(toIndentedString(arguments)).append("\n");
    sb.append("    examples: ").append(toIndentedString(examples)).append("\n");
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

