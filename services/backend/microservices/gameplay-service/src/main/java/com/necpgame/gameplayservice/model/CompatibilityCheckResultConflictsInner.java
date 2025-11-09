package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompatibilityCheckResultConflictsInner
 */

@JsonTypeName("CompatibilityCheckResult_conflicts_inner")

public class CompatibilityCheckResultConflictsInner {

  private @Nullable String implantId1;

  private @Nullable String implantId2;

  /**
   * Gets or Sets conflictReason
   */
  public enum ConflictReasonEnum {
    SLOT_CONFLICT("slot_conflict"),
    
    INCOMPATIBLE_TYPES("incompatible_types"),
    
    ENERGY_OVERLOAD("energy_overload"),
    
    HUMANITY_LIMIT("humanity_limit");

    private final String value;

    ConflictReasonEnum(String value) {
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
    public static ConflictReasonEnum fromValue(String value) {
      for (ConflictReasonEnum b : ConflictReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConflictReasonEnum conflictReason;

  private @Nullable String conflictDescription;

  public CompatibilityCheckResultConflictsInner implantId1(@Nullable String implantId1) {
    this.implantId1 = implantId1;
    return this;
  }

  /**
   * Get implantId1
   * @return implantId1
   */
  
  @Schema(name = "implant_id_1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id_1")
  public @Nullable String getImplantId1() {
    return implantId1;
  }

  public void setImplantId1(@Nullable String implantId1) {
    this.implantId1 = implantId1;
  }

  public CompatibilityCheckResultConflictsInner implantId2(@Nullable String implantId2) {
    this.implantId2 = implantId2;
    return this;
  }

  /**
   * Get implantId2
   * @return implantId2
   */
  
  @Schema(name = "implant_id_2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id_2")
  public @Nullable String getImplantId2() {
    return implantId2;
  }

  public void setImplantId2(@Nullable String implantId2) {
    this.implantId2 = implantId2;
  }

  public CompatibilityCheckResultConflictsInner conflictReason(@Nullable ConflictReasonEnum conflictReason) {
    this.conflictReason = conflictReason;
    return this;
  }

  /**
   * Get conflictReason
   * @return conflictReason
   */
  
  @Schema(name = "conflict_reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflict_reason")
  public @Nullable ConflictReasonEnum getConflictReason() {
    return conflictReason;
  }

  public void setConflictReason(@Nullable ConflictReasonEnum conflictReason) {
    this.conflictReason = conflictReason;
  }

  public CompatibilityCheckResultConflictsInner conflictDescription(@Nullable String conflictDescription) {
    this.conflictDescription = conflictDescription;
    return this;
  }

  /**
   * Get conflictDescription
   * @return conflictDescription
   */
  
  @Schema(name = "conflict_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflict_description")
  public @Nullable String getConflictDescription() {
    return conflictDescription;
  }

  public void setConflictDescription(@Nullable String conflictDescription) {
    this.conflictDescription = conflictDescription;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompatibilityCheckResultConflictsInner compatibilityCheckResultConflictsInner = (CompatibilityCheckResultConflictsInner) o;
    return Objects.equals(this.implantId1, compatibilityCheckResultConflictsInner.implantId1) &&
        Objects.equals(this.implantId2, compatibilityCheckResultConflictsInner.implantId2) &&
        Objects.equals(this.conflictReason, compatibilityCheckResultConflictsInner.conflictReason) &&
        Objects.equals(this.conflictDescription, compatibilityCheckResultConflictsInner.conflictDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId1, implantId2, conflictReason, conflictDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityCheckResultConflictsInner {\n");
    sb.append("    implantId1: ").append(toIndentedString(implantId1)).append("\n");
    sb.append("    implantId2: ").append(toIndentedString(implantId2)).append("\n");
    sb.append("    conflictReason: ").append(toIndentedString(conflictReason)).append("\n");
    sb.append("    conflictDescription: ").append(toIndentedString(conflictDescription)).append("\n");
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

