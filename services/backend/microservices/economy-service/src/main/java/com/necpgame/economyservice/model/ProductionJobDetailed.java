package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.ProductionFacility;
import com.necpgame.economyservice.model.ProductionStage;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProductionJobDetailed
 */


public class ProductionJobDetailed {

  private @Nullable UUID jobId;

  private @Nullable UUID characterId;

  private @Nullable String chainId;

  private @Nullable Integer stageNumber;

  private @Nullable Integer quantity;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED"),
    
    CANCELLED("CANCELLED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedCompletion;

  private @Nullable BigDecimal progressPercentage;

  private @Nullable ProductionStage stage;

  @Valid
  private List<Object> inputsConsumed = new ArrayList<>();

  private @Nullable ProductionFacility facility;

  public ProductionJobDetailed jobId(@Nullable UUID jobId) {
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

  public ProductionJobDetailed characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public ProductionJobDetailed chainId(@Nullable String chainId) {
    this.chainId = chainId;
    return this;
  }

  /**
   * Get chainId
   * @return chainId
   */
  
  @Schema(name = "chain_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chain_id")
  public @Nullable String getChainId() {
    return chainId;
  }

  public void setChainId(@Nullable String chainId) {
    this.chainId = chainId;
  }

  public ProductionJobDetailed stageNumber(@Nullable Integer stageNumber) {
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

  public ProductionJobDetailed quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public ProductionJobDetailed status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ProductionJobDetailed startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public ProductionJobDetailed estimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
    return this;
  }

  /**
   * Get estimatedCompletion
   * @return estimatedCompletion
   */
  @Valid 
  @Schema(name = "estimated_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_completion")
  public @Nullable OffsetDateTime getEstimatedCompletion() {
    return estimatedCompletion;
  }

  public void setEstimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
  }

  public ProductionJobDetailed progressPercentage(@Nullable BigDecimal progressPercentage) {
    this.progressPercentage = progressPercentage;
    return this;
  }

  /**
   * Get progressPercentage
   * @return progressPercentage
   */
  @Valid 
  @Schema(name = "progress_percentage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress_percentage")
  public @Nullable BigDecimal getProgressPercentage() {
    return progressPercentage;
  }

  public void setProgressPercentage(@Nullable BigDecimal progressPercentage) {
    this.progressPercentage = progressPercentage;
  }

  public ProductionJobDetailed stage(@Nullable ProductionStage stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  @Valid 
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable ProductionStage getStage() {
    return stage;
  }

  public void setStage(@Nullable ProductionStage stage) {
    this.stage = stage;
  }

  public ProductionJobDetailed inputsConsumed(List<Object> inputsConsumed) {
    this.inputsConsumed = inputsConsumed;
    return this;
  }

  public ProductionJobDetailed addInputsConsumedItem(Object inputsConsumedItem) {
    if (this.inputsConsumed == null) {
      this.inputsConsumed = new ArrayList<>();
    }
    this.inputsConsumed.add(inputsConsumedItem);
    return this;
  }

  /**
   * Get inputsConsumed
   * @return inputsConsumed
   */
  
  @Schema(name = "inputs_consumed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inputs_consumed")
  public List<Object> getInputsConsumed() {
    return inputsConsumed;
  }

  public void setInputsConsumed(List<Object> inputsConsumed) {
    this.inputsConsumed = inputsConsumed;
  }

  public ProductionJobDetailed facility(@Nullable ProductionFacility facility) {
    this.facility = facility;
    return this;
  }

  /**
   * Get facility
   * @return facility
   */
  @Valid 
  @Schema(name = "facility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facility")
  public @Nullable ProductionFacility getFacility() {
    return facility;
  }

  public void setFacility(@Nullable ProductionFacility facility) {
    this.facility = facility;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionJobDetailed productionJobDetailed = (ProductionJobDetailed) o;
    return Objects.equals(this.jobId, productionJobDetailed.jobId) &&
        Objects.equals(this.characterId, productionJobDetailed.characterId) &&
        Objects.equals(this.chainId, productionJobDetailed.chainId) &&
        Objects.equals(this.stageNumber, productionJobDetailed.stageNumber) &&
        Objects.equals(this.quantity, productionJobDetailed.quantity) &&
        Objects.equals(this.status, productionJobDetailed.status) &&
        Objects.equals(this.startedAt, productionJobDetailed.startedAt) &&
        Objects.equals(this.estimatedCompletion, productionJobDetailed.estimatedCompletion) &&
        Objects.equals(this.progressPercentage, productionJobDetailed.progressPercentage) &&
        Objects.equals(this.stage, productionJobDetailed.stage) &&
        Objects.equals(this.inputsConsumed, productionJobDetailed.inputsConsumed) &&
        Objects.equals(this.facility, productionJobDetailed.facility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(jobId, characterId, chainId, stageNumber, quantity, status, startedAt, estimatedCompletion, progressPercentage, stage, inputsConsumed, facility);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionJobDetailed {\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    chainId: ").append(toIndentedString(chainId)).append("\n");
    sb.append("    stageNumber: ").append(toIndentedString(stageNumber)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    estimatedCompletion: ").append(toIndentedString(estimatedCompletion)).append("\n");
    sb.append("    progressPercentage: ").append(toIndentedString(progressPercentage)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    inputsConsumed: ").append(toIndentedString(inputsConsumed)).append("\n");
    sb.append("    facility: ").append(toIndentedString(facility)).append("\n");
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

