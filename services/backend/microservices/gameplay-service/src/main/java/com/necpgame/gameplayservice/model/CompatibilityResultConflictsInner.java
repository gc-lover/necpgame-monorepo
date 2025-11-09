package com.necpgame.gameplayservice.model;

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
 * CompatibilityResultConflictsInner
 */

@JsonTypeName("CompatibilityResult_conflicts_inner")

public class CompatibilityResultConflictsInner {

  private @Nullable String implant1;

  private @Nullable String implant2;

  private @Nullable String conflictType;

  private @Nullable String description;

  public CompatibilityResultConflictsInner implant1(@Nullable String implant1) {
    this.implant1 = implant1;
    return this;
  }

  /**
   * Get implant1
   * @return implant1
   */
  
  @Schema(name = "implant_1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_1")
  public @Nullable String getImplant1() {
    return implant1;
  }

  public void setImplant1(@Nullable String implant1) {
    this.implant1 = implant1;
  }

  public CompatibilityResultConflictsInner implant2(@Nullable String implant2) {
    this.implant2 = implant2;
    return this;
  }

  /**
   * Get implant2
   * @return implant2
   */
  
  @Schema(name = "implant_2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_2")
  public @Nullable String getImplant2() {
    return implant2;
  }

  public void setImplant2(@Nullable String implant2) {
    this.implant2 = implant2;
  }

  public CompatibilityResultConflictsInner conflictType(@Nullable String conflictType) {
    this.conflictType = conflictType;
    return this;
  }

  /**
   * Get conflictType
   * @return conflictType
   */
  
  @Schema(name = "conflict_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflict_type")
  public @Nullable String getConflictType() {
    return conflictType;
  }

  public void setConflictType(@Nullable String conflictType) {
    this.conflictType = conflictType;
  }

  public CompatibilityResultConflictsInner description(@Nullable String description) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompatibilityResultConflictsInner compatibilityResultConflictsInner = (CompatibilityResultConflictsInner) o;
    return Objects.equals(this.implant1, compatibilityResultConflictsInner.implant1) &&
        Objects.equals(this.implant2, compatibilityResultConflictsInner.implant2) &&
        Objects.equals(this.conflictType, compatibilityResultConflictsInner.conflictType) &&
        Objects.equals(this.description, compatibilityResultConflictsInner.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implant1, implant2, conflictType, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityResultConflictsInner {\n");
    sb.append("    implant1: ").append(toIndentedString(implant1)).append("\n");
    sb.append("    implant2: ").append(toIndentedString(implant2)).append("\n");
    sb.append("    conflictType: ").append(toIndentedString(conflictType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

