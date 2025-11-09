package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.StageObjectiveProgress;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * StageObjective
 */


public class StageObjective {

  private String objectiveId;

  private String type;

  private String target;

  private @Nullable Integer quantity;

  private Boolean optional = false;

  private @Nullable StageObjectiveProgress progress;

  public StageObjective() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StageObjective(String objectiveId, String type, String target) {
    this.objectiveId = objectiveId;
    this.type = type;
    this.target = target;
  }

  public StageObjective objectiveId(String objectiveId) {
    this.objectiveId = objectiveId;
    return this;
  }

  /**
   * Get objectiveId
   * @return objectiveId
   */
  @NotNull 
  @Schema(name = "objectiveId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectiveId")
  public String getObjectiveId() {
    return objectiveId;
  }

  public void setObjectiveId(String objectiveId) {
    this.objectiveId = objectiveId;
  }

  public StageObjective type(String type) {
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
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public StageObjective target(String target) {
    this.target = target;
    return this;
  }

  /**
   * Get target
   * @return target
   */
  @NotNull 
  @Schema(name = "target", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target")
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public StageObjective quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * @return quantity
   */
  @Min(value = 1) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public StageObjective optional(Boolean optional) {
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

  public StageObjective progress(@Nullable StageObjectiveProgress progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * @return progress
   */
  @Valid 
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable StageObjectiveProgress getProgress() {
    return progress;
  }

  public void setProgress(@Nullable StageObjectiveProgress progress) {
    this.progress = progress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StageObjective stageObjective = (StageObjective) o;
    return Objects.equals(this.objectiveId, stageObjective.objectiveId) &&
        Objects.equals(this.type, stageObjective.type) &&
        Objects.equals(this.target, stageObjective.target) &&
        Objects.equals(this.quantity, stageObjective.quantity) &&
        Objects.equals(this.optional, stageObjective.optional) &&
        Objects.equals(this.progress, stageObjective.progress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectiveId, type, target, quantity, optional, progress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StageObjective {\n");
    sb.append("    objectiveId: ").append(toIndentedString(objectiveId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    optional: ").append(toIndentedString(optional)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
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

