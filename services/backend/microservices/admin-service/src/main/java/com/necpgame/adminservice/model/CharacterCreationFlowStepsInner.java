package com.necpgame.adminservice.model;

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
 * CharacterCreationFlowStepsInner
 */

@JsonTypeName("CharacterCreationFlow_steps_inner")

public class CharacterCreationFlowStepsInner {

  private @Nullable String stepId;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable Integer order;

  public CharacterCreationFlowStepsInner stepId(@Nullable String stepId) {
    this.stepId = stepId;
    return this;
  }

  /**
   * Get stepId
   * @return stepId
   */
  
  @Schema(name = "step_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("step_id")
  public @Nullable String getStepId() {
    return stepId;
  }

  public void setStepId(@Nullable String stepId) {
    this.stepId = stepId;
  }

  public CharacterCreationFlowStepsInner title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public CharacterCreationFlowStepsInner description(@Nullable String description) {
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

  public CharacterCreationFlowStepsInner order(@Nullable Integer order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * @return order
   */
  
  @Schema(name = "order", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("order")
  public @Nullable Integer getOrder() {
    return order;
  }

  public void setOrder(@Nullable Integer order) {
    this.order = order;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterCreationFlowStepsInner characterCreationFlowStepsInner = (CharacterCreationFlowStepsInner) o;
    return Objects.equals(this.stepId, characterCreationFlowStepsInner.stepId) &&
        Objects.equals(this.title, characterCreationFlowStepsInner.title) &&
        Objects.equals(this.description, characterCreationFlowStepsInner.description) &&
        Objects.equals(this.order, characterCreationFlowStepsInner.order);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stepId, title, description, order);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterCreationFlowStepsInner {\n");
    sb.append("    stepId: ").append(toIndentedString(stepId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
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

