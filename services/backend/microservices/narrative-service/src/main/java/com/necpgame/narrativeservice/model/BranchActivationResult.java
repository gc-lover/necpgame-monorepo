package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.BranchActivationResultNarrativeImpact;
import com.necpgame.narrativeservice.model.Consequence;
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
 * BranchActivationResult
 */


public class BranchActivationResult {

  private @Nullable Boolean success;

  private @Nullable String branchId;

  @Valid
  private List<@Valid Consequence> consequencesApplied = new ArrayList<>();

  @Valid
  private List<String> unlockedContent = new ArrayList<>();

  @Valid
  private List<String> lockedContent = new ArrayList<>();

  private @Nullable BranchActivationResultNarrativeImpact narrativeImpact;

  public BranchActivationResult success(@Nullable Boolean success) {
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

  public BranchActivationResult branchId(@Nullable String branchId) {
    this.branchId = branchId;
    return this;
  }

  /**
   * Get branchId
   * @return branchId
   */
  
  @Schema(name = "branch_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branch_id")
  public @Nullable String getBranchId() {
    return branchId;
  }

  public void setBranchId(@Nullable String branchId) {
    this.branchId = branchId;
  }

  public BranchActivationResult consequencesApplied(List<@Valid Consequence> consequencesApplied) {
    this.consequencesApplied = consequencesApplied;
    return this;
  }

  public BranchActivationResult addConsequencesAppliedItem(Consequence consequencesAppliedItem) {
    if (this.consequencesApplied == null) {
      this.consequencesApplied = new ArrayList<>();
    }
    this.consequencesApplied.add(consequencesAppliedItem);
    return this;
  }

  /**
   * Get consequencesApplied
   * @return consequencesApplied
   */
  @Valid 
  @Schema(name = "consequences_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences_applied")
  public List<@Valid Consequence> getConsequencesApplied() {
    return consequencesApplied;
  }

  public void setConsequencesApplied(List<@Valid Consequence> consequencesApplied) {
    this.consequencesApplied = consequencesApplied;
  }

  public BranchActivationResult unlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
    return this;
  }

  public BranchActivationResult addUnlockedContentItem(String unlockedContentItem) {
    if (this.unlockedContent == null) {
      this.unlockedContent = new ArrayList<>();
    }
    this.unlockedContent.add(unlockedContentItem);
    return this;
  }

  /**
   * New quests/content unlocked
   * @return unlockedContent
   */
  
  @Schema(name = "unlocked_content", description = "New quests/content unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_content")
  public List<String> getUnlockedContent() {
    return unlockedContent;
  }

  public void setUnlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
  }

  public BranchActivationResult lockedContent(List<String> lockedContent) {
    this.lockedContent = lockedContent;
    return this;
  }

  public BranchActivationResult addLockedContentItem(String lockedContentItem) {
    if (this.lockedContent == null) {
      this.lockedContent = new ArrayList<>();
    }
    this.lockedContent.add(lockedContentItem);
    return this;
  }

  /**
   * Content locked by this choice
   * @return lockedContent
   */
  
  @Schema(name = "locked_content", description = "Content locked by this choice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locked_content")
  public List<String> getLockedContent() {
    return lockedContent;
  }

  public void setLockedContent(List<String> lockedContent) {
    this.lockedContent = lockedContent;
  }

  public BranchActivationResult narrativeImpact(@Nullable BranchActivationResultNarrativeImpact narrativeImpact) {
    this.narrativeImpact = narrativeImpact;
    return this;
  }

  /**
   * Get narrativeImpact
   * @return narrativeImpact
   */
  @Valid 
  @Schema(name = "narrative_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("narrative_impact")
  public @Nullable BranchActivationResultNarrativeImpact getNarrativeImpact() {
    return narrativeImpact;
  }

  public void setNarrativeImpact(@Nullable BranchActivationResultNarrativeImpact narrativeImpact) {
    this.narrativeImpact = narrativeImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchActivationResult branchActivationResult = (BranchActivationResult) o;
    return Objects.equals(this.success, branchActivationResult.success) &&
        Objects.equals(this.branchId, branchActivationResult.branchId) &&
        Objects.equals(this.consequencesApplied, branchActivationResult.consequencesApplied) &&
        Objects.equals(this.unlockedContent, branchActivationResult.unlockedContent) &&
        Objects.equals(this.lockedContent, branchActivationResult.lockedContent) &&
        Objects.equals(this.narrativeImpact, branchActivationResult.narrativeImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, branchId, consequencesApplied, unlockedContent, lockedContent, narrativeImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchActivationResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    consequencesApplied: ").append(toIndentedString(consequencesApplied)).append("\n");
    sb.append("    unlockedContent: ").append(toIndentedString(unlockedContent)).append("\n");
    sb.append("    lockedContent: ").append(toIndentedString(lockedContent)).append("\n");
    sb.append("    narrativeImpact: ").append(toIndentedString(narrativeImpact)).append("\n");
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

