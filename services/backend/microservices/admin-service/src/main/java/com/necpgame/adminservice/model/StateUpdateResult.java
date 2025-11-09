package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.StateUpdateResultConflictsInner;
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
 * StateUpdateResult
 */


public class StateUpdateResult {

  private @Nullable Boolean success;

  private @Nullable Integer newVersion;

  @Valid
  private List<@Valid StateUpdateResultConflictsInner> conflicts = new ArrayList<>();

  public StateUpdateResult success(@Nullable Boolean success) {
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

  public StateUpdateResult newVersion(@Nullable Integer newVersion) {
    this.newVersion = newVersion;
    return this;
  }

  /**
   * Get newVersion
   * @return newVersion
   */
  
  @Schema(name = "new_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_version")
  public @Nullable Integer getNewVersion() {
    return newVersion;
  }

  public void setNewVersion(@Nullable Integer newVersion) {
    this.newVersion = newVersion;
  }

  public StateUpdateResult conflicts(List<@Valid StateUpdateResultConflictsInner> conflicts) {
    this.conflicts = conflicts;
    return this;
  }

  public StateUpdateResult addConflictsItem(StateUpdateResultConflictsInner conflictsItem) {
    if (this.conflicts == null) {
      this.conflicts = new ArrayList<>();
    }
    this.conflicts.add(conflictsItem);
    return this;
  }

  /**
   * Get conflicts
   * @return conflicts
   */
  @Valid 
  @Schema(name = "conflicts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conflicts")
  public List<@Valid StateUpdateResultConflictsInner> getConflicts() {
    return conflicts;
  }

  public void setConflicts(List<@Valid StateUpdateResultConflictsInner> conflicts) {
    this.conflicts = conflicts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateUpdateResult stateUpdateResult = (StateUpdateResult) o;
    return Objects.equals(this.success, stateUpdateResult.success) &&
        Objects.equals(this.newVersion, stateUpdateResult.newVersion) &&
        Objects.equals(this.conflicts, stateUpdateResult.conflicts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, newVersion, conflicts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateUpdateResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    newVersion: ").append(toIndentedString(newVersion)).append("\n");
    sb.append("    conflicts: ").append(toIndentedString(conflicts)).append("\n");
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

