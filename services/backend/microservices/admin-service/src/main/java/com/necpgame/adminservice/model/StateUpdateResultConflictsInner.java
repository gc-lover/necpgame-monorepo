package com.necpgame.adminservice.model;

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
 * StateUpdateResultConflictsInner
 */

@JsonTypeName("StateUpdateResult_conflicts_inner")

public class StateUpdateResultConflictsInner {

  private @Nullable String field;

  private @Nullable Integer expectedVersion;

  private @Nullable Integer actualVersion;

  public StateUpdateResultConflictsInner field(@Nullable String field) {
    this.field = field;
    return this;
  }

  /**
   * Get field
   * @return field
   */
  
  @Schema(name = "field", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public @Nullable String getField() {
    return field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public StateUpdateResultConflictsInner expectedVersion(@Nullable Integer expectedVersion) {
    this.expectedVersion = expectedVersion;
    return this;
  }

  /**
   * Get expectedVersion
   * @return expectedVersion
   */
  
  @Schema(name = "expected_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_version")
  public @Nullable Integer getExpectedVersion() {
    return expectedVersion;
  }

  public void setExpectedVersion(@Nullable Integer expectedVersion) {
    this.expectedVersion = expectedVersion;
  }

  public StateUpdateResultConflictsInner actualVersion(@Nullable Integer actualVersion) {
    this.actualVersion = actualVersion;
    return this;
  }

  /**
   * Get actualVersion
   * @return actualVersion
   */
  
  @Schema(name = "actual_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actual_version")
  public @Nullable Integer getActualVersion() {
    return actualVersion;
  }

  public void setActualVersion(@Nullable Integer actualVersion) {
    this.actualVersion = actualVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateUpdateResultConflictsInner stateUpdateResultConflictsInner = (StateUpdateResultConflictsInner) o;
    return Objects.equals(this.field, stateUpdateResultConflictsInner.field) &&
        Objects.equals(this.expectedVersion, stateUpdateResultConflictsInner.expectedVersion) &&
        Objects.equals(this.actualVersion, stateUpdateResultConflictsInner.actualVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, expectedVersion, actualVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateUpdateResultConflictsInner {\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    expectedVersion: ").append(toIndentedString(expectedVersion)).append("\n");
    sb.append("    actualVersion: ").append(toIndentedString(actualVersion)).append("\n");
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

