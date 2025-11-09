package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ProductionResultOutputsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProductionResult
 */


public class ProductionResult {

  private @Nullable UUID jobId;

  private @Nullable Boolean success;

  @Valid
  private List<@Valid ProductionResultOutputsInner> outputs = new ArrayList<>();

  private @Nullable Integer experienceGained;

  @Valid
  private List<Object> byproducts = new ArrayList<>();

  public ProductionResult jobId(@Nullable UUID jobId) {
    this.jobId = jobId;
    return this;
  }

  /**
   * Get jobId
   * @return jobId
   */
  @Valid 
  @Schema(name = "job_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("job_id")
  public @Nullable UUID getJobId() {
    return jobId;
  }

  public void setJobId(@Nullable UUID jobId) {
    this.jobId = jobId;
  }

  public ProductionResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ProductionResult outputs(List<@Valid ProductionResultOutputsInner> outputs) {
    this.outputs = outputs;
    return this;
  }

  public ProductionResult addOutputsItem(ProductionResultOutputsInner outputsItem) {
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
  public List<@Valid ProductionResultOutputsInner> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<@Valid ProductionResultOutputsInner> outputs) {
    this.outputs = outputs;
  }

  public ProductionResult experienceGained(@Nullable Integer experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_gained")
  public @Nullable Integer getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(@Nullable Integer experienceGained) {
    this.experienceGained = experienceGained;
  }

  public ProductionResult byproducts(List<Object> byproducts) {
    this.byproducts = byproducts;
    return this;
  }

  public ProductionResult addByproductsItem(Object byproductsItem) {
    if (this.byproducts == null) {
      this.byproducts = new ArrayList<>();
    }
    this.byproducts.add(byproductsItem);
    return this;
  }

  /**
   * Побочные продукты
   * @return byproducts
   */
  
  @Schema(name = "byproducts", description = "Побочные продукты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("byproducts")
  public List<Object> getByproducts() {
    return byproducts;
  }

  public void setByproducts(List<Object> byproducts) {
    this.byproducts = byproducts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionResult productionResult = (ProductionResult) o;
    return Objects.equals(this.jobId, productionResult.jobId) &&
        Objects.equals(this.success, productionResult.success) &&
        Objects.equals(this.outputs, productionResult.outputs) &&
        Objects.equals(this.experienceGained, productionResult.experienceGained) &&
        Objects.equals(this.byproducts, productionResult.byproducts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, success, outputs, experienceGained, byproducts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionResult {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    outputs: ").append(toIndentedString(outputs)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
    sb.append("    byproducts: ").append(toIndentedString(byproducts)).append("\n");
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

