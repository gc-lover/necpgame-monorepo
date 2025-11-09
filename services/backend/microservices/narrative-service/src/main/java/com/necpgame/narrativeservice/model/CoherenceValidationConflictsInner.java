package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CoherenceValidationConflictsInner
 */

@JsonTypeName("CoherenceValidation_conflicts_inner")

public class CoherenceValidationConflictsInner {

  /**
   * Gets or Sets conflictType
   */
  public enum ConflictTypeEnum {
    MUTUALLY_EXCLUSIVE("mutually_exclusive"),
    
    MISSING_PREREQUISITE("missing_prerequisite"),
    
    STATE_MISMATCH("state_mismatch");

    private final String value;

    ConflictTypeEnum(String value) {
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
    public static ConflictTypeEnum fromValue(String value) {
      for (ConflictTypeEnum b : ConflictTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConflictTypeEnum conflictType;

  private @Nullable String description;

  @Valid
  private List<String> affectedBranches = new ArrayList<>();

  public CoherenceValidationConflictsInner conflictType(@Nullable ConflictTypeEnum conflictType) {
    this.conflictType = conflictType;
    return this;
  }

  /**
   * Get conflictType
   * @return conflictType
   */
  
  @Schema(name = "conflict_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflict_type")
  public @Nullable ConflictTypeEnum getConflictType() {
    return conflictType;
  }

  public void setConflictType(@Nullable ConflictTypeEnum conflictType) {
    this.conflictType = conflictType;
  }

  public CoherenceValidationConflictsInner description(@Nullable String description) {
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

  public CoherenceValidationConflictsInner affectedBranches(List<String> affectedBranches) {
    this.affectedBranches = affectedBranches;
    return this;
  }

  public CoherenceValidationConflictsInner addAffectedBranchesItem(String affectedBranchesItem) {
    if (this.affectedBranches == null) {
      this.affectedBranches = new ArrayList<>();
    }
    this.affectedBranches.add(affectedBranchesItem);
    return this;
  }

  /**
   * Get affectedBranches
   * @return affectedBranches
   */
  
  @Schema(name = "affected_branches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_branches")
  public List<String> getAffectedBranches() {
    return affectedBranches;
  }

  public void setAffectedBranches(List<String> affectedBranches) {
    this.affectedBranches = affectedBranches;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoherenceValidationConflictsInner coherenceValidationConflictsInner = (CoherenceValidationConflictsInner) o;
    return Objects.equals(this.conflictType, coherenceValidationConflictsInner.conflictType) &&
        Objects.equals(this.description, coherenceValidationConflictsInner.description) &&
        Objects.equals(this.affectedBranches, coherenceValidationConflictsInner.affectedBranches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conflictType, description, affectedBranches);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoherenceValidationConflictsInner {\n");
    sb.append("    conflictType: ").append(toIndentedString(conflictType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    affectedBranches: ").append(toIndentedString(affectedBranches)).append("\n");
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

