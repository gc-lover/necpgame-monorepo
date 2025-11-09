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
 * ShutdownPlanDrainStepsInner
 */

@JsonTypeName("ShutdownPlan_drainSteps_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ShutdownPlanDrainStepsInner {

  private Integer step;

  private String description;

  private @Nullable String targetService;

  public ShutdownPlanDrainStepsInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ShutdownPlanDrainStepsInner(Integer step, String description) {
    this.step = step;
    this.description = description;
  }

  public ShutdownPlanDrainStepsInner step(Integer step) {
    this.step = step;
    return this;
  }

  /**
   * Get step
   * @return step
   */
  @NotNull 
  @Schema(name = "step", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("step")
  public Integer getStep() {
    return step;
  }

  public void setStep(Integer step) {
    this.step = step;
  }

  public ShutdownPlanDrainStepsInner description(String description) {
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

  public ShutdownPlanDrainStepsInner targetService(@Nullable String targetService) {
    this.targetService = targetService;
    return this;
  }

  /**
   * Get targetService
   * @return targetService
   */
  
  @Schema(name = "targetService", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetService")
  public @Nullable String getTargetService() {
    return targetService;
  }

  public void setTargetService(@Nullable String targetService) {
    this.targetService = targetService;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShutdownPlanDrainStepsInner shutdownPlanDrainStepsInner = (ShutdownPlanDrainStepsInner) o;
    return Objects.equals(this.step, shutdownPlanDrainStepsInner.step) &&
        Objects.equals(this.description, shutdownPlanDrainStepsInner.description) &&
        Objects.equals(this.targetService, shutdownPlanDrainStepsInner.targetService);
  }

  @Override
  public int hashCode() {
    return Objects.hash(step, description, targetService);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShutdownPlanDrainStepsInner {\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    targetService: ").append(toIndentedString(targetService)).append("\n");
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

