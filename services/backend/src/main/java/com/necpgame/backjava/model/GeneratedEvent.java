package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.WorldEvent;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GeneratedEvent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GeneratedEvent {

  private @Nullable WorldEvent event;

  private @Nullable Integer generationRoll;

  private @Nullable String generationTable;

  public GeneratedEvent event(@Nullable WorldEvent event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable WorldEvent getEvent() {
    return event;
  }

  public void setEvent(@Nullable WorldEvent event) {
    this.event = event;
  }

  public GeneratedEvent generationRoll(@Nullable Integer generationRoll) {
    this.generationRoll = generationRoll;
    return this;
  }

  /**
   * Результат броска d100
   * minimum: 1
   * maximum: 100
   * @return generationRoll
   */
  @Min(value = 1) @Max(value = 100) 
  @Schema(name = "generation_roll", description = "Результат броска d100", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generation_roll")
  public @Nullable Integer getGenerationRoll() {
    return generationRoll;
  }

  public void setGenerationRoll(@Nullable Integer generationRoll) {
    this.generationRoll = generationRoll;
  }

  public GeneratedEvent generationTable(@Nullable String generationTable) {
    this.generationTable = generationTable;
    return this;
  }

  /**
   * Какая таблица использована
   * @return generationTable
   */
  
  @Schema(name = "generation_table", description = "Какая таблица использована", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generation_table")
  public @Nullable String getGenerationTable() {
    return generationTable;
  }

  public void setGenerationTable(@Nullable String generationTable) {
    this.generationTable = generationTable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeneratedEvent generatedEvent = (GeneratedEvent) o;
    return Objects.equals(this.event, generatedEvent.event) &&
        Objects.equals(this.generationRoll, generatedEvent.generationRoll) &&
        Objects.equals(this.generationTable, generatedEvent.generationTable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event, generationRoll, generationTable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeneratedEvent {\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    generationRoll: ").append(toIndentedString(generationRoll)).append("\n");
    sb.append("    generationTable: ").append(toIndentedString(generationTable)).append("\n");
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

