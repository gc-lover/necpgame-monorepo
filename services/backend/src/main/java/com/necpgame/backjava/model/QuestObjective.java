package com.necpgame.backjava.model;

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
 * QuestObjective
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:05.709666800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class QuestObjective {

  private String id;

  private String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    LOCATION("location"),
    
    KILL("kill"),
    
    COLLECT("collect"),
    
    TALK("talk"),
    
    INTERACT("interact");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private Integer currentProgress;

  private Integer targetProgress;

  private Boolean completed;

  private Boolean optional = false;

  public QuestObjective() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QuestObjective(String id, String description, TypeEnum type, Integer currentProgress, Integer targetProgress, Boolean completed) {
    this.id = id;
    this.description = description;
    this.type = type;
    this.currentProgress = currentProgress;
    this.targetProgress = targetProgress;
    this.completed = completed;
  }

  public QuestObjective id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "obj_find_npc", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public QuestObjective description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "РќР°Р№С‚Рё С‚РѕСЂРіРѕРІС†Р°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public QuestObjective type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public QuestObjective currentProgress(Integer currentProgress) {
    this.currentProgress = currentProgress;
    return this;
  }

  /**
   * Get currentProgress
   * @return currentProgress
   */
  @NotNull 
  @Schema(name = "currentProgress", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentProgress")
  public Integer getCurrentProgress() {
    return currentProgress;
  }

  public void setCurrentProgress(Integer currentProgress) {
    this.currentProgress = currentProgress;
  }

  public QuestObjective targetProgress(Integer targetProgress) {
    this.targetProgress = targetProgress;
    return this;
  }

  /**
   * Get targetProgress
   * @return targetProgress
   */
  @NotNull 
  @Schema(name = "targetProgress", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetProgress")
  public Integer getTargetProgress() {
    return targetProgress;
  }

  public void setTargetProgress(Integer targetProgress) {
    this.targetProgress = targetProgress;
  }

  public QuestObjective completed(Boolean completed) {
    this.completed = completed;
    return this;
  }

  /**
   * Get completed
   * @return completed
   */
  @NotNull 
  @Schema(name = "completed", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("completed")
  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public QuestObjective optional(Boolean optional) {
    this.optional = optional;
    return this;
  }

  /**
   * Get optional
   * @return optional
   */
  
  @Schema(name = "optional", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("optional")
  public Boolean getOptional() {
    return optional;
  }

  public void setOptional(Boolean optional) {
    this.optional = optional;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestObjective questObjective = (QuestObjective) o;
    return Objects.equals(this.id, questObjective.id) &&
        Objects.equals(this.description, questObjective.description) &&
        Objects.equals(this.type, questObjective.type) &&
        Objects.equals(this.currentProgress, questObjective.currentProgress) &&
        Objects.equals(this.targetProgress, questObjective.targetProgress) &&
        Objects.equals(this.completed, questObjective.completed) &&
        Objects.equals(this.optional, questObjective.optional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description, type, currentProgress, targetProgress, completed, optional);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestObjective {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    currentProgress: ").append(toIndentedString(currentProgress)).append("\n");
    sb.append("    targetProgress: ").append(toIndentedString(targetProgress)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
    sb.append("    optional: ").append(toIndentedString(optional)).append("\n");
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


