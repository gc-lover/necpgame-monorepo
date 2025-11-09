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
 * ValidationHistoryItem
 */


public class ValidationHistoryItem {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime checkedAt;

  private ValidationStatus status;

  private @Nullable ValidationChecklist checklist;

  public ValidationHistoryItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidationHistoryItem(OffsetDateTime checkedAt, ValidationStatus status) {
    this.checkedAt = checkedAt;
    this.status = status;
  }

  public ValidationHistoryItem checkedAt(OffsetDateTime checkedAt) {
    this.checkedAt = checkedAt;
    return this;
  }

  /**
   * Get checkedAt
   * @return checkedAt
   */
  @NotNull @Valid 
  @Schema(name = "checkedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("checkedAt")
  public OffsetDateTime getCheckedAt() {
    return checkedAt;
  }

  public void setCheckedAt(OffsetDateTime checkedAt) {
    this.checkedAt = checkedAt;
  }

  public ValidationHistoryItem status(ValidationStatus status) {
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

  public ValidationHistoryItem checklist(@Nullable ValidationChecklist checklist) {
    this.checklist = checklist;
    return this;
  }

  /**
   * Get checklist
   * @return checklist
   */
  @Valid 
  @Schema(name = "checklist", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("checklist")
  public @Nullable ValidationChecklist getChecklist() {
    return checklist;
  }

  public void setChecklist(@Nullable ValidationChecklist checklist) {
    this.checklist = checklist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationHistoryItem validationHistoryItem = (ValidationHistoryItem) o;
    return Objects.equals(this.checkedAt, validationHistoryItem.checkedAt) &&
        Objects.equals(this.status, validationHistoryItem.status) &&
        Objects.equals(this.checklist, validationHistoryItem.checklist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkedAt, status, checklist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationHistoryItem {\n");
    sb.append("    checkedAt: ").append(toIndentedString(checkedAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    checklist: ").append(toIndentedString(checklist)).append("\n");
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

