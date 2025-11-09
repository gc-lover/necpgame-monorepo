package com.necpgame.backjava.model;

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
 * ActiveQuestProgressObjectivesInner
 */

@JsonTypeName("ActiveQuestProgress_objectives_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ActiveQuestProgressObjectivesInner {

  private @Nullable String objectiveId;

  private @Nullable String description;

  private @Nullable Boolean completed;

  public ActiveQuestProgressObjectivesInner objectiveId(@Nullable String objectiveId) {
    this.objectiveId = objectiveId;
    return this;
  }

  /**
   * Get objectiveId
   * @return objectiveId
   */
  
  @Schema(name = "objective_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objective_id")
  public @Nullable String getObjectiveId() {
    return objectiveId;
  }

  public void setObjectiveId(@Nullable String objectiveId) {
    this.objectiveId = objectiveId;
  }

  public ActiveQuestProgressObjectivesInner description(@Nullable String description) {
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

  public ActiveQuestProgressObjectivesInner completed(@Nullable Boolean completed) {
    this.completed = completed;
    return this;
  }

  /**
   * Get completed
   * @return completed
   */
  
  @Schema(name = "completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed")
  public @Nullable Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(@Nullable Boolean completed) {
    this.completed = completed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveQuestProgressObjectivesInner activeQuestProgressObjectivesInner = (ActiveQuestProgressObjectivesInner) o;
    return Objects.equals(this.objectiveId, activeQuestProgressObjectivesInner.objectiveId) &&
        Objects.equals(this.description, activeQuestProgressObjectivesInner.description) &&
        Objects.equals(this.completed, activeQuestProgressObjectivesInner.completed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectiveId, description, completed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveQuestProgressObjectivesInner {\n");
    sb.append("    objectiveId: ").append(toIndentedString(objectiveId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    completed: ").append(toIndentedString(completed)).append("\n");
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

