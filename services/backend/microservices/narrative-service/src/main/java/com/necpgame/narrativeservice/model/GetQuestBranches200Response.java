package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.QuestBranch;
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

public class GetQuestBranches200Response {

  private @Nullable String questId;

  @Valid
  private List<@Valid QuestBranch> branches = new ArrayList<>();

  @Valid
  private List<String> activeBranches = new ArrayList<>();

  public GetQuestBranches200Response questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

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

  public GetQuestBranches200Response activeBranches(List<String> activeBranches) {
    this.activeBranches = activeBranches;
    return this;
  }

  public GetQuestBranches200Response addActiveBranchesItem(String activeBranchesItem) {
    if (this.activeBranches == null) {
      this.activeBranches = new ArrayList<>();
    }
    this.activeBranches.add(activeBranchesItem);
    return this;
  }

  /**
   * Currently active branches
   * @return activeBranches
   */
  
  @Schema(name = "active_branches", description = "Currently active branches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_branches")
  public List<String> getActiveBranches() {
    return activeBranches;
  }

  public void setActiveBranches(List<String> activeBranches) {
    this.activeBranches = activeBranches;
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
    return Objects.equals(this.questId, getQuestBranches200Response.questId) &&
        Objects.equals(this.branches, getQuestBranches200Response.branches) &&
        Objects.equals(this.activeBranches, getQuestBranches200Response.activeBranches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, branches, activeBranches);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestBranches200Response {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    activeBranches: ").append(toIndentedString(activeBranches)).append("\n");
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

