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
 * QuestDetailsAllOfObjectives
 */

@JsonTypeName("QuestDetails_allOf_objectives")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestDetailsAllOfObjectives {

  private @Nullable String objectiveId;

  private @Nullable String description;

  private @Nullable Boolean optional;

  public QuestDetailsAllOfObjectives objectiveId(@Nullable String objectiveId) {
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

  public QuestDetailsAllOfObjectives description(@Nullable String description) {
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

  public QuestDetailsAllOfObjectives optional(@Nullable Boolean optional) {
    this.optional = optional;
    return this;
  }

  /**
   * Get optional
   * @return optional
   */
  
  @Schema(name = "optional", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("optional")
  public @Nullable Boolean getOptional() {
    return optional;
  }

  public void setOptional(@Nullable Boolean optional) {
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
    QuestDetailsAllOfObjectives questDetailsAllOfObjectives = (QuestDetailsAllOfObjectives) o;
    return Objects.equals(this.objectiveId, questDetailsAllOfObjectives.objectiveId) &&
        Objects.equals(this.description, questDetailsAllOfObjectives.description) &&
        Objects.equals(this.optional, questDetailsAllOfObjectives.optional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectiveId, description, optional);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestDetailsAllOfObjectives {\n");
    sb.append("    objectiveId: ").append(toIndentedString(objectiveId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

