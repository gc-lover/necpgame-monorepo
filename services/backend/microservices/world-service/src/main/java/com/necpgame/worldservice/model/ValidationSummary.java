package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ValidationStatus;
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
 * ValidationSummary
 */


public class ValidationSummary {

  private ValidationStatus overallStatus;

  private Integer blockingIssues;

  private Integer warningsCount;

  @Valid
  private List<String> recommendedActions = new ArrayList<>();

  private String policyVersion;

  private @Nullable UUID auditTraceId;

  public ValidationSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidationSummary(ValidationStatus overallStatus, Integer blockingIssues, Integer warningsCount, String policyVersion) {
    this.overallStatus = overallStatus;
    this.blockingIssues = blockingIssues;
    this.warningsCount = warningsCount;
    this.policyVersion = policyVersion;
  }

  public ValidationSummary overallStatus(ValidationStatus overallStatus) {
    this.overallStatus = overallStatus;
    return this;
  }

  /**
   * Get overallStatus
   * @return overallStatus
   */
  @NotNull @Valid 
  @Schema(name = "overallStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("overallStatus")
  public ValidationStatus getOverallStatus() {
    return overallStatus;
  }

  public void setOverallStatus(ValidationStatus overallStatus) {
    this.overallStatus = overallStatus;
  }

  public ValidationSummary blockingIssues(Integer blockingIssues) {
    this.blockingIssues = blockingIssues;
    return this;
  }

  /**
   * Get blockingIssues
   * minimum: 0
   * @return blockingIssues
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "blockingIssues", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("blockingIssues")
  public Integer getBlockingIssues() {
    return blockingIssues;
  }

  public void setBlockingIssues(Integer blockingIssues) {
    this.blockingIssues = blockingIssues;
  }

  public ValidationSummary warningsCount(Integer warningsCount) {
    this.warningsCount = warningsCount;
    return this;
  }

  /**
   * Get warningsCount
   * minimum: 0
   * @return warningsCount
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "warningsCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("warningsCount")
  public Integer getWarningsCount() {
    return warningsCount;
  }

  public void setWarningsCount(Integer warningsCount) {
    this.warningsCount = warningsCount;
  }

  public ValidationSummary recommendedActions(List<String> recommendedActions) {
    this.recommendedActions = recommendedActions;
    return this;
  }

  public ValidationSummary addRecommendedActionsItem(String recommendedActionsItem) {
    if (this.recommendedActions == null) {
      this.recommendedActions = new ArrayList<>();
    }
    this.recommendedActions.add(recommendedActionsItem);
    return this;
  }

  /**
   * Get recommendedActions
   * @return recommendedActions
   */
  
  @Schema(name = "recommendedActions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedActions")
  public List<String> getRecommendedActions() {
    return recommendedActions;
  }

  public void setRecommendedActions(List<String> recommendedActions) {
    this.recommendedActions = recommendedActions;
  }

  public ValidationSummary policyVersion(String policyVersion) {
    this.policyVersion = policyVersion;
    return this;
  }

  /**
   * Версия политик world-service, использованных при проверке.
   * @return policyVersion
   */
  @NotNull 
  @Schema(name = "policyVersion", description = "Версия политик world-service, использованных при проверке.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("policyVersion")
  public String getPolicyVersion() {
    return policyVersion;
  }

  public void setPolicyVersion(String policyVersion) {
    this.policyVersion = policyVersion;
  }

  public ValidationSummary auditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
    return this;
  }

  /**
   * Get auditTraceId
   * @return auditTraceId
   */
  @Valid 
  @Schema(name = "auditTraceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditTraceId")
  public @Nullable UUID getAuditTraceId() {
    return auditTraceId;
  }

  public void setAuditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationSummary validationSummary = (ValidationSummary) o;
    return Objects.equals(this.overallStatus, validationSummary.overallStatus) &&
        Objects.equals(this.blockingIssues, validationSummary.blockingIssues) &&
        Objects.equals(this.warningsCount, validationSummary.warningsCount) &&
        Objects.equals(this.recommendedActions, validationSummary.recommendedActions) &&
        Objects.equals(this.policyVersion, validationSummary.policyVersion) &&
        Objects.equals(this.auditTraceId, validationSummary.auditTraceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(overallStatus, blockingIssues, warningsCount, recommendedActions, policyVersion, auditTraceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationSummary {\n");
    sb.append("    overallStatus: ").append(toIndentedString(overallStatus)).append("\n");
    sb.append("    blockingIssues: ").append(toIndentedString(blockingIssues)).append("\n");
    sb.append("    warningsCount: ").append(toIndentedString(warningsCount)).append("\n");
    sb.append("    recommendedActions: ").append(toIndentedString(recommendedActions)).append("\n");
    sb.append("    policyVersion: ").append(toIndentedString(policyVersion)).append("\n");
    sb.append("    auditTraceId: ").append(toIndentedString(auditTraceId)).append("\n");
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

