package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.BranchTreeBranchRelationshipsInner;
import com.necpgame.narrativeservice.model.BranchTreeCriticalPathsInner;
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
 * BranchTree
 */


public class BranchTree {

  private @Nullable String questId;

  private @Nullable String questName;

  @Valid
  private List<@Valid QuestBranch> branches = new ArrayList<>();

  @Valid
  private List<@Valid BranchTreeBranchRelationshipsInner> branchRelationships = new ArrayList<>();

  @Valid
  private List<@Valid BranchTreeCriticalPathsInner> criticalPaths = new ArrayList<>();

  public BranchTree questId(@Nullable String questId) {
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

  public BranchTree questName(@Nullable String questName) {
    this.questName = questName;
    return this;
  }

  /**
   * Get questName
   * @return questName
   */
  
  @Schema(name = "quest_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_name")
  public @Nullable String getQuestName() {
    return questName;
  }

  public void setQuestName(@Nullable String questName) {
    this.questName = questName;
  }

  public BranchTree branches(List<@Valid QuestBranch> branches) {
    this.branches = branches;
    return this;
  }

  public BranchTree addBranchesItem(QuestBranch branchesItem) {
    if (this.branches == null) {
      this.branches = new ArrayList<>();
    }
    this.branches.add(branchesItem);
    return this;
  }

  /**
   * Все branches в дереве
   * @return branches
   */
  @Valid 
  @Schema(name = "branches", description = "Все branches в дереве", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branches")
  public List<@Valid QuestBranch> getBranches() {
    return branches;
  }

  public void setBranches(List<@Valid QuestBranch> branches) {
    this.branches = branches;
  }

  public BranchTree branchRelationships(List<@Valid BranchTreeBranchRelationshipsInner> branchRelationships) {
    this.branchRelationships = branchRelationships;
    return this;
  }

  public BranchTree addBranchRelationshipsItem(BranchTreeBranchRelationshipsInner branchRelationshipsItem) {
    if (this.branchRelationships == null) {
      this.branchRelationships = new ArrayList<>();
    }
    this.branchRelationships.add(branchRelationshipsItem);
    return this;
  }

  /**
   * Связи между branches
   * @return branchRelationships
   */
  @Valid 
  @Schema(name = "branch_relationships", description = "Связи между branches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branch_relationships")
  public List<@Valid BranchTreeBranchRelationshipsInner> getBranchRelationships() {
    return branchRelationships;
  }

  public void setBranchRelationships(List<@Valid BranchTreeBranchRelationshipsInner> branchRelationships) {
    this.branchRelationships = branchRelationships;
  }

  public BranchTree criticalPaths(List<@Valid BranchTreeCriticalPathsInner> criticalPaths) {
    this.criticalPaths = criticalPaths;
    return this;
  }

  public BranchTree addCriticalPathsItem(BranchTreeCriticalPathsInner criticalPathsItem) {
    if (this.criticalPaths == null) {
      this.criticalPaths = new ArrayList<>();
    }
    this.criticalPaths.add(criticalPathsItem);
    return this;
  }

  /**
   * Critical narrative paths
   * @return criticalPaths
   */
  @Valid 
  @Schema(name = "critical_paths", description = "Critical narrative paths", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_paths")
  public List<@Valid BranchTreeCriticalPathsInner> getCriticalPaths() {
    return criticalPaths;
  }

  public void setCriticalPaths(List<@Valid BranchTreeCriticalPathsInner> criticalPaths) {
    this.criticalPaths = criticalPaths;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchTree branchTree = (BranchTree) o;
    return Objects.equals(this.questId, branchTree.questId) &&
        Objects.equals(this.questName, branchTree.questName) &&
        Objects.equals(this.branches, branchTree.branches) &&
        Objects.equals(this.branchRelationships, branchTree.branchRelationships) &&
        Objects.equals(this.criticalPaths, branchTree.criticalPaths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, questName, branches, branchRelationships, criticalPaths);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchTree {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    questName: ").append(toIndentedString(questName)).append("\n");
    sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
    sb.append("    branchRelationships: ").append(toIndentedString(branchRelationships)).append("\n");
    sb.append("    criticalPaths: ").append(toIndentedString(criticalPaths)).append("\n");
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

