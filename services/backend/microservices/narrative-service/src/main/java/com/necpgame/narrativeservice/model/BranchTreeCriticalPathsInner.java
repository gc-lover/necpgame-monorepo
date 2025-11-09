package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * BranchTreeCriticalPathsInner
 */

@JsonTypeName("BranchTree_critical_paths_inner")

public class BranchTreeCriticalPathsInner {

  private @Nullable String pathId;

  @Valid
  private List<String> branches = new ArrayList<>();

  public BranchTreeCriticalPathsInner pathId(@Nullable String pathId) {
    this.pathId = pathId;
    return this;
  }

  /**
   * Get pathId
   * @return pathId
   */
  
  @Schema(name = "path_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("path_id")
  public @Nullable String getPathId() {
    return pathId;
  }

  public void setPathId(@Nullable String pathId) {
    this.pathId = pathId;
  }

  public BranchTreeCriticalPathsInner branches(List<String> branches) {
    this.branches = branches;
    return this;
  }

  public BranchTreeCriticalPathsInner addBranchesItem(String branchesItem) {
    if (this.branches == null) {
      this.branches = new ArrayList<>();
    }
    this.branches.add(branchesItem);
    return this;
  }

  /**
   * Get branches
   * @return branches
   */
  
  @Schema(name = "branches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches")
  public List<String> getBranches() {
    return branches;
  }

  public void setBranches(List<String> branches) {
    this.branches = branches;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchTreeCriticalPathsInner branchTreeCriticalPathsInner = (BranchTreeCriticalPathsInner) o;
    return Objects.equals(this.pathId, branchTreeCriticalPathsInner.pathId) &&
        Objects.equals(this.branches, branchTreeCriticalPathsInner.branches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pathId, branches);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchTreeCriticalPathsInner {\n");
    sb.append("    pathId: ").append(toIndentedString(pathId)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
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

