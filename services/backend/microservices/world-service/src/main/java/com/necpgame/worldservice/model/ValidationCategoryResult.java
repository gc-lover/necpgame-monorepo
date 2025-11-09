package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ValidationIssue;
import com.necpgame.worldservice.model.ValidationStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ValidationCategoryResult
 */


public class ValidationCategoryResult {

  private ValidationStatus status;

  @Valid
  private List<@Valid ValidationIssue> issues = new ArrayList<>();

  @Valid
  private List<@Valid ValidationIssue> warnings = new ArrayList<>();

  public ValidationCategoryResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidationCategoryResult(ValidationStatus status) {
    this.status = status;
  }

  public ValidationCategoryResult status(ValidationStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public ValidationStatus getStatus() {
    return status;
  }

  public void setStatus(ValidationStatus status) {
    this.status = status;
  }

  public ValidationCategoryResult issues(List<@Valid ValidationIssue> issues) {
    this.issues = issues;
    return this;
  }

  public ValidationCategoryResult addIssuesItem(ValidationIssue issuesItem) {
    if (this.issues == null) {
      this.issues = new ArrayList<>();
    }
    this.issues.add(issuesItem);
    return this;
  }

  /**
   * Get issues
   * @return issues
   */
  @Valid 
  @Schema(name = "issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issues")
  public List<@Valid ValidationIssue> getIssues() {
    return issues;
  }

  public void setIssues(List<@Valid ValidationIssue> issues) {
    this.issues = issues;
  }

  public ValidationCategoryResult warnings(List<@Valid ValidationIssue> warnings) {
    this.warnings = warnings;
    return this;
  }

  public ValidationCategoryResult addWarningsItem(ValidationIssue warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  @Valid 
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid ValidationIssue> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid ValidationIssue> warnings) {
    this.warnings = warnings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationCategoryResult validationCategoryResult = (ValidationCategoryResult) o;
    return Objects.equals(this.status, validationCategoryResult.status) &&
        Objects.equals(this.issues, validationCategoryResult.issues) &&
        Objects.equals(this.warnings, validationCategoryResult.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, issues, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationCategoryResult {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    issues: ").append(toIndentedString(issues)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

