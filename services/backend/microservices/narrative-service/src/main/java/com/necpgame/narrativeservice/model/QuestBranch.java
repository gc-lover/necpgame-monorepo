package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.BranchCondition;
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
 * QuestBranch
 */


public class QuestBranch {

  private String branchId;

  private String branchName;

  private @Nullable String description;

  /**
   * Gets or Sets branchType
   */
  public enum BranchTypeEnum {
    MAIN("main"),
    
    SIDE("side"),
    
    PARALLEL("parallel"),
    
    EXCLUSIVE("exclusive");

    private final String value;

    BranchTypeEnum(String value) {
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
    public static BranchTypeEnum fromValue(String value) {
      for (BranchTypeEnum b : BranchTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BranchTypeEnum branchType;

  @Valid
  private List<@Valid BranchCondition> conditions = new ArrayList<>();

  @Valid
  private List<@Valid Consequence> consequences = new ArrayList<>();

  @Valid
  private List<String> nextBranches = new ArrayList<>();

  @Valid
  private List<String> mutuallyExclusiveWith = new ArrayList<>();

  /**
   * Значимость branch для narrative
   */
  public enum SignificanceEnum {
    MINOR("minor"),
    
    MODERATE("moderate"),
    
    MAJOR("major"),
    
    CRITICAL("critical");

    private final String value;

    SignificanceEnum(String value) {
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
    public static SignificanceEnum fromValue(String value) {
      for (SignificanceEnum b : SignificanceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SignificanceEnum significance;

  public QuestBranch() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QuestBranch(String branchId, String branchName) {
    this.branchId = branchId;
    this.branchName = branchName;
  }

  public QuestBranch branchId(String branchId) {
    this.branchId = branchId;
    return this;
  }

  /**
   * Get branchId
   * @return branchId
   */
  @NotNull 
  @Schema(name = "branch_id", example = "corporate_path", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("branch_id")
  public String getBranchId() {
    return branchId;
  }

  public void setBranchId(String branchId) {
    this.branchId = branchId;
  }

  public QuestBranch branchName(String branchName) {
    this.branchName = branchName;
    return this;
  }

  /**
   * Get branchName
   * @return branchName
   */
  @NotNull 
  @Schema(name = "branch_name", example = "Corporate Alliance Path", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("branch_name")
  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public QuestBranch description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", example = "Align with corporate forces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public QuestBranch branchType(@Nullable BranchTypeEnum branchType) {
    this.branchType = branchType;
    return this;
  }

  /**
   * Get branchType
   * @return branchType
   */
  
  @Schema(name = "branch_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branch_type")
  public @Nullable BranchTypeEnum getBranchType() {
    return branchType;
  }

  public void setBranchType(@Nullable BranchTypeEnum branchType) {
    this.branchType = branchType;
  }

  public QuestBranch conditions(List<@Valid BranchCondition> conditions) {
    this.conditions = conditions;
    return this;
  }

  public QuestBranch addConditionsItem(BranchCondition conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * Условия для активации branch
   * @return conditions
   */
  @Valid 
  @Schema(name = "conditions", description = "Условия для активации branch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conditions")
  public List<@Valid BranchCondition> getConditions() {
    return conditions;
  }

  public void setConditions(List<@Valid BranchCondition> conditions) {
    this.conditions = conditions;
  }

  public QuestBranch consequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
    return this;
  }

  public QuestBranch addConsequencesItem(Consequence consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Последствия активации branch
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", description = "Последствия активации branch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<@Valid Consequence> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
  }

  public QuestBranch nextBranches(List<String> nextBranches) {
    this.nextBranches = nextBranches;
    return this;
  }

  public QuestBranch addNextBranchesItem(String nextBranchesItem) {
    if (this.nextBranches == null) {
      this.nextBranches = new ArrayList<>();
    }
    this.nextBranches.add(nextBranchesItem);
    return this;
  }

  /**
   * Branches доступные после этого
   * @return nextBranches
   */
  
  @Schema(name = "next_branches", description = "Branches доступные после этого", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_branches")
  public List<String> getNextBranches() {
    return nextBranches;
  }

  public void setNextBranches(List<String> nextBranches) {
    this.nextBranches = nextBranches;
  }

  public QuestBranch mutuallyExclusiveWith(List<String> mutuallyExclusiveWith) {
    this.mutuallyExclusiveWith = mutuallyExclusiveWith;
    return this;
  }

  public QuestBranch addMutuallyExclusiveWithItem(String mutuallyExclusiveWithItem) {
    if (this.mutuallyExclusiveWith == null) {
      this.mutuallyExclusiveWith = new ArrayList<>();
    }
    this.mutuallyExclusiveWith.add(mutuallyExclusiveWithItem);
    return this;
  }

  /**
   * Branches несовместимые с этим
   * @return mutuallyExclusiveWith
   */
  
  @Schema(name = "mutually_exclusive_with", description = "Branches несовместимые с этим", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mutually_exclusive_with")
  public List<String> getMutuallyExclusiveWith() {
    return mutuallyExclusiveWith;
  }

  public void setMutuallyExclusiveWith(List<String> mutuallyExclusiveWith) {
    this.mutuallyExclusiveWith = mutuallyExclusiveWith;
  }

  public QuestBranch significance(@Nullable SignificanceEnum significance) {
    this.significance = significance;
    return this;
  }

  /**
   * Значимость branch для narrative
   * @return significance
   */
  
  @Schema(name = "significance", description = "Значимость branch для narrative", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("significance")
  public @Nullable SignificanceEnum getSignificance() {
    return significance;
  }

  public void setSignificance(@Nullable SignificanceEnum significance) {
    this.significance = significance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestBranch questBranch = (QuestBranch) o;
    return Objects.equals(this.branchId, questBranch.branchId) &&
        Objects.equals(this.branchName, questBranch.branchName) &&
        Objects.equals(this.description, questBranch.description) &&
        Objects.equals(this.branchType, questBranch.branchType) &&
        Objects.equals(this.conditions, questBranch.conditions) &&
        Objects.equals(this.consequences, questBranch.consequences) &&
        Objects.equals(this.nextBranches, questBranch.nextBranches) &&
        Objects.equals(this.mutuallyExclusiveWith, questBranch.mutuallyExclusiveWith) &&
        Objects.equals(this.significance, questBranch.significance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branchId, branchName, description, branchType, conditions, consequences, nextBranches, mutuallyExclusiveWith, significance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestBranch {\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    branchName: ").append(toIndentedString(branchName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    branchType: ").append(toIndentedString(branchType)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    nextBranches: ").append(toIndentedString(nextBranches)).append("\n");
    sb.append("    mutuallyExclusiveWith: ").append(toIndentedString(mutuallyExclusiveWith)).append("\n");
    sb.append("    significance: ").append(toIndentedString(significance)).append("\n");
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

