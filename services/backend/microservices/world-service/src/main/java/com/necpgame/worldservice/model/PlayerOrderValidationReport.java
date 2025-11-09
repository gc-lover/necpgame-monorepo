package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ValidationChecklist;
import com.necpgame.worldservice.model.ValidationStatus;
import java.time.OffsetDateTime;
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
 * PlayerOrderValidationReport
 */


public class PlayerOrderValidationReport {

  private ValidationStatus status;

  private ValidationChecklist checklist;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  public PlayerOrderValidationReport() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderValidationReport(ValidationStatus status, ValidationChecklist checklist) {
    this.status = status;
    this.checklist = checklist;
  }

  public PlayerOrderValidationReport status(ValidationStatus status) {
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

  public PlayerOrderValidationReport checklist(ValidationChecklist checklist) {
    this.checklist = checklist;
    return this;
  }

  /**
   * Get checklist
   * @return checklist
   */
  @NotNull @Valid 
  @Schema(name = "checklist", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("checklist")
  public ValidationChecklist getChecklist() {
    return checklist;
  }

  public void setChecklist(ValidationChecklist checklist) {
    this.checklist = checklist;
  }

  public PlayerOrderValidationReport generatedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
   */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public @Nullable OffsetDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderValidationReport playerOrderValidationReport = (PlayerOrderValidationReport) o;
    return Objects.equals(this.status, playerOrderValidationReport.status) &&
        Objects.equals(this.checklist, playerOrderValidationReport.checklist) &&
        Objects.equals(this.generatedAt, playerOrderValidationReport.generatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, checklist, generatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderValidationReport {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    checklist: ").append(toIndentedString(checklist)).append("\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
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

