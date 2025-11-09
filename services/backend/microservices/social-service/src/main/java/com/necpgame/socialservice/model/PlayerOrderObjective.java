package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderObjective
 */


public class PlayerOrderObjective {

  private String title;

  private @Nullable String description;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    PRIMARY("primary"),
    
    SECONDARY("secondary"),
    
    OPTIONAL("optional");

    private final String value;

    PriorityEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority;

  private @Nullable String successCriteria;

  public PlayerOrderObjective() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderObjective(String title, PriorityEnum priority) {
    this.title = title;
    this.priority = priority;
  }

  public PlayerOrderObjective title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Цель или задача в рамках заказа.
   * @return title
   */
  @NotNull 
  @Schema(name = "title", description = "Цель или задача в рамках заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PlayerOrderObjective description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Детали по выполнению задачи.
   * @return description
   */
  
  @Schema(name = "description", description = "Детали по выполнению задачи.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public PlayerOrderObjective priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @NotNull 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public PlayerOrderObjective successCriteria(@Nullable String successCriteria) {
    this.successCriteria = successCriteria;
    return this;
  }

  /**
   * Условие завершения задачи.
   * @return successCriteria
   */
  
  @Schema(name = "successCriteria", description = "Условие завершения задачи.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("successCriteria")
  public @Nullable String getSuccessCriteria() {
    return successCriteria;
  }

  public void setSuccessCriteria(@Nullable String successCriteria) {
    this.successCriteria = successCriteria;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderObjective playerOrderObjective = (PlayerOrderObjective) o;
    return Objects.equals(this.title, playerOrderObjective.title) &&
        Objects.equals(this.description, playerOrderObjective.description) &&
        Objects.equals(this.priority, playerOrderObjective.priority) &&
        Objects.equals(this.successCriteria, playerOrderObjective.successCriteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, description, priority, successCriteria);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderObjective {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    successCriteria: ").append(toIndentedString(successCriteria)).append("\n");
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

