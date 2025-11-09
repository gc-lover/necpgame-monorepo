package com.necpgame.gameplayservice.model;

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
 * SelectClass200Response
 */

@JsonTypeName("selectClass_200_response")

public class SelectClass200Response {

  private @Nullable Boolean success;

  private @Nullable Object appliedBonuses;

  @Valid
  private List<String> unlockedSkillTrees = new ArrayList<>();

  @Valid
  private List<String> unlockedAbilities = new ArrayList<>();

  public SelectClass200Response success(@Nullable Boolean success) {
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

  public SelectClass200Response appliedBonuses(@Nullable Object appliedBonuses) {
    this.appliedBonuses = appliedBonuses;
    return this;
  }

  /**
   * Get appliedBonuses
   * @return appliedBonuses
   */
  
  @Schema(name = "applied_bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applied_bonuses")
  public @Nullable Object getAppliedBonuses() {
    return appliedBonuses;
  }

  public void setAppliedBonuses(@Nullable Object appliedBonuses) {
    this.appliedBonuses = appliedBonuses;
  }

  public SelectClass200Response unlockedSkillTrees(List<String> unlockedSkillTrees) {
    this.unlockedSkillTrees = unlockedSkillTrees;
    return this;
  }

  public SelectClass200Response addUnlockedSkillTreesItem(String unlockedSkillTreesItem) {
    if (this.unlockedSkillTrees == null) {
      this.unlockedSkillTrees = new ArrayList<>();
    }
    this.unlockedSkillTrees.add(unlockedSkillTreesItem);
    return this;
  }

  /**
   * Get unlockedSkillTrees
   * @return unlockedSkillTrees
   */
  
  @Schema(name = "unlocked_skill_trees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_skill_trees")
  public List<String> getUnlockedSkillTrees() {
    return unlockedSkillTrees;
  }

  public void setUnlockedSkillTrees(List<String> unlockedSkillTrees) {
    this.unlockedSkillTrees = unlockedSkillTrees;
  }

  public SelectClass200Response unlockedAbilities(List<String> unlockedAbilities) {
    this.unlockedAbilities = unlockedAbilities;
    return this;
  }

  public SelectClass200Response addUnlockedAbilitiesItem(String unlockedAbilitiesItem) {
    if (this.unlockedAbilities == null) {
      this.unlockedAbilities = new ArrayList<>();
    }
    this.unlockedAbilities.add(unlockedAbilitiesItem);
    return this;
  }

  /**
   * Get unlockedAbilities
   * @return unlockedAbilities
   */
  
  @Schema(name = "unlocked_abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_abilities")
  public List<String> getUnlockedAbilities() {
    return unlockedAbilities;
  }

  public void setUnlockedAbilities(List<String> unlockedAbilities) {
    this.unlockedAbilities = unlockedAbilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SelectClass200Response selectClass200Response = (SelectClass200Response) o;
    return Objects.equals(this.success, selectClass200Response.success) &&
        Objects.equals(this.appliedBonuses, selectClass200Response.appliedBonuses) &&
        Objects.equals(this.unlockedSkillTrees, selectClass200Response.unlockedSkillTrees) &&
        Objects.equals(this.unlockedAbilities, selectClass200Response.unlockedAbilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, appliedBonuses, unlockedSkillTrees, unlockedAbilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SelectClass200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    appliedBonuses: ").append(toIndentedString(appliedBonuses)).append("\n");
    sb.append("    unlockedSkillTrees: ").append(toIndentedString(unlockedSkillTrees)).append("\n");
    sb.append("    unlockedAbilities: ").append(toIndentedString(unlockedAbilities)).append("\n");
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

