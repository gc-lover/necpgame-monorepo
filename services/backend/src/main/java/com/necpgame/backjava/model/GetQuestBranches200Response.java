package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.QuestBranch;
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
 * GetQuestBranches200Response
 */

@JsonTypeName("getQuestBranches_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetQuestBranches200Response {

  @Valid
  private List<@Valid QuestBranch> branches = new ArrayList<>();

  private @Nullable String currentBranch;

  public GetQuestBranches200Response branches(List<@Valid QuestBranch> branches) {
    this.branches = branches;
    return this;
  }

  public GetQuestBranches200Response addBranchesItem(QuestBranch branchesItem) {
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
  @Valid 
  @Schema(name = "branches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches")
  public List<@Valid QuestBranch> getBranches() {
    return branches;
  }

  public void setBranches(List<@Valid QuestBranch> branches) {
    this.branches = branches;
  }

  public GetQuestBranches200Response currentBranch(@Nullable String currentBranch) {
    this.currentBranch = currentBranch;
    return this;
  }

  /**
   * Текущая ветка игрока
   * @return currentBranch
   */
  
  @Schema(name = "current_branch", description = "Текущая ветка игрока", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_branch")
  public @Nullable String getCurrentBranch() {
    return currentBranch;
  }

  public void setCurrentBranch(@Nullable String currentBranch) {
    this.currentBranch = currentBranch;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetQuestBranches200Response getQuestBranches200Response = (GetQuestBranches200Response) o;
    return Objects.equals(this.branches, getQuestBranches200Response.branches) &&
        Objects.equals(this.currentBranch, getQuestBranches200Response.currentBranch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branches, currentBranch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestBranches200Response {\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    currentBranch: ").append(toIndentedString(currentBranch)).append("\n");
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

