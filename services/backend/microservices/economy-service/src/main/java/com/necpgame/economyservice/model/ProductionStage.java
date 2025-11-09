package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ProductionStageInputsInner;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProductionStage
 */


public class ProductionStage {

  private @Nullable Integer stageNumber;

  private @Nullable String name;

  @Valid
  private List<@Valid ProductionStageInputsInner> inputs = new ArrayList<>();

  @Valid
  private List<@Valid ProductionStageInputsInner> outputs = new ArrayList<>();

  private @Nullable BigDecimal timeHours;

  private JsonNullable<String> facilityRequired = JsonNullable.<String>undefined();

  private JsonNullable<String> skillRequired = JsonNullable.<String>undefined();

  private @Nullable Integer minSkillLevel;

  public ProductionStage stageNumber(@Nullable Integer stageNumber) {
    this.stageNumber = stageNumber;
    return this;
  }

  /**
   * Get stageNumber
   * @return stageNumber
   */
  
  @Schema(name = "stage_number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage_number")
  public @Nullable Integer getStageNumber() {
    return stageNumber;
  }

  public void setStageNumber(@Nullable Integer stageNumber) {
    this.stageNumber = stageNumber;
  }

  public ProductionStage name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Smelt Ore to Ingots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public ProductionStage inputs(List<@Valid ProductionStageInputsInner> inputs) {
    this.inputs = inputs;
    return this;
  }

  public ProductionStage addInputsItem(ProductionStageInputsInner inputsItem) {
    if (this.inputs == null) {
      this.inputs = new ArrayList<>();
    }
    this.inputs.add(inputsItem);
    return this;
  }

  /**
   * Get inputs
   * @return inputs
   */
  @Valid 
  @Schema(name = "inputs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inputs")
  public List<@Valid ProductionStageInputsInner> getInputs() {
    return inputs;
  }

  public void setInputs(List<@Valid ProductionStageInputsInner> inputs) {
    this.inputs = inputs;
  }

  public ProductionStage outputs(List<@Valid ProductionStageInputsInner> outputs) {
    this.outputs = outputs;
    return this;
  }

  public ProductionStage addOutputsItem(ProductionStageInputsInner outputsItem) {
    if (this.outputs == null) {
      this.outputs = new ArrayList<>();
    }
    this.outputs.add(outputsItem);
    return this;
  }

  /**
   * Get outputs
   * @return outputs
   */
  @Valid 
  @Schema(name = "outputs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("outputs")
  public List<@Valid ProductionStageInputsInner> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<@Valid ProductionStageInputsInner> outputs) {
    this.outputs = outputs;
  }

  public ProductionStage timeHours(@Nullable BigDecimal timeHours) {
    this.timeHours = timeHours;
    return this;
  }

  /**
   * Get timeHours
   * @return timeHours
   */
  @Valid 
  @Schema(name = "time_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_hours")
  public @Nullable BigDecimal getTimeHours() {
    return timeHours;
  }

  public void setTimeHours(@Nullable BigDecimal timeHours) {
    this.timeHours = timeHours;
  }

  public ProductionStage facilityRequired(String facilityRequired) {
    this.facilityRequired = JsonNullable.of(facilityRequired);
    return this;
  }

  /**
   * Get facilityRequired
   * @return facilityRequired
   */
  
  @Schema(name = "facility_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facility_required")
  public JsonNullable<String> getFacilityRequired() {
    return facilityRequired;
  }

  public void setFacilityRequired(JsonNullable<String> facilityRequired) {
    this.facilityRequired = facilityRequired;
  }

  public ProductionStage skillRequired(String skillRequired) {
    this.skillRequired = JsonNullable.of(skillRequired);
    return this;
  }

  /**
   * Get skillRequired
   * @return skillRequired
   */
  
  @Schema(name = "skill_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_required")
  public JsonNullable<String> getSkillRequired() {
    return skillRequired;
  }

  public void setSkillRequired(JsonNullable<String> skillRequired) {
    this.skillRequired = skillRequired;
  }

  public ProductionStage minSkillLevel(@Nullable Integer minSkillLevel) {
    this.minSkillLevel = minSkillLevel;
    return this;
  }

  /**
   * Get minSkillLevel
   * @return minSkillLevel
   */
  
  @Schema(name = "min_skill_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_skill_level")
  public @Nullable Integer getMinSkillLevel() {
    return minSkillLevel;
  }

  public void setMinSkillLevel(@Nullable Integer minSkillLevel) {
    this.minSkillLevel = minSkillLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionStage productionStage = (ProductionStage) o;
    return Objects.equals(this.stageNumber, productionStage.stageNumber) &&
        Objects.equals(this.name, productionStage.name) &&
        Objects.equals(this.inputs, productionStage.inputs) &&
        Objects.equals(this.outputs, productionStage.outputs) &&
        Objects.equals(this.timeHours, productionStage.timeHours) &&
        equalsNullable(this.facilityRequired, productionStage.facilityRequired) &&
        equalsNullable(this.skillRequired, productionStage.skillRequired) &&
        Objects.equals(this.minSkillLevel, productionStage.minSkillLevel);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(stageNumber, name, inputs, outputs, timeHours, hashCodeNullable(facilityRequired), hashCodeNullable(skillRequired), minSkillLevel);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionStage {\n");
    sb.append("    stageNumber: ").append(toIndentedString(stageNumber)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    inputs: ").append(toIndentedString(inputs)).append("\n");
    sb.append("    outputs: ").append(toIndentedString(outputs)).append("\n");
    sb.append("    timeHours: ").append(toIndentedString(timeHours)).append("\n");
    sb.append("    facilityRequired: ").append(toIndentedString(facilityRequired)).append("\n");
    sb.append("    skillRequired: ").append(toIndentedString(skillRequired)).append("\n");
    sb.append("    minSkillLevel: ").append(toIndentedString(minSkillLevel)).append("\n");
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

