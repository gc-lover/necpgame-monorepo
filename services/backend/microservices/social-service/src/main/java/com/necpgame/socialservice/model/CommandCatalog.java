package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.CommandCatalogEntry;
import java.net.URI;
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
 * CommandCatalog
 */


public class CommandCatalog {

  @Valid
  private List<@Valid CommandCatalogEntry> commands = new ArrayList<>();

  private @Nullable URI helpUrl;

  public CommandCatalog() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CommandCatalog(List<@Valid CommandCatalogEntry> commands) {
    this.commands = commands;
  }

  public CommandCatalog commands(List<@Valid CommandCatalogEntry> commands) {
    this.commands = commands;
    return this;
  }

  public CommandCatalog addCommandsItem(CommandCatalogEntry commandsItem) {
    if (this.commands == null) {
      this.commands = new ArrayList<>();
    }
    this.commands.add(commandsItem);
    return this;
  }

  /**
   * Get commands
   * @return commands
   */
  @NotNull @Valid 
  @Schema(name = "commands", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("commands")
  public List<@Valid CommandCatalogEntry> getCommands() {
    return commands;
  }

  public void setCommands(List<@Valid CommandCatalogEntry> commands) {
    this.commands = commands;
  }

  public CommandCatalog helpUrl(@Nullable URI helpUrl) {
    this.helpUrl = helpUrl;
    return this;
  }

  /**
   * Get helpUrl
   * @return helpUrl
   */
  @Valid 
  @Schema(name = "helpUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("helpUrl")
  public @Nullable URI getHelpUrl() {
    return helpUrl;
  }

  public void setHelpUrl(@Nullable URI helpUrl) {
    this.helpUrl = helpUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommandCatalog commandCatalog = (CommandCatalog) o;
    return Objects.equals(this.commands, commandCatalog.commands) &&
        Objects.equals(this.helpUrl, commandCatalog.helpUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(commands, helpUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommandCatalog {\n");
    sb.append("    commands: ").append(toIndentedString(commands)).append("\n");
    sb.append("    helpUrl: ").append(toIndentedString(helpUrl)).append("\n");
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

