package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.QuestBranchConsequences;
import com.necpgame.backjava.model.QuestBranchRequirements;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestBranch {

  private @Nullable String branchId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable QuestBranchRequirements requirements;

  private @Nullable QuestBranchConsequences consequences;

  @Valid
  private List<String> leadsToEndings = new ArrayList<>();

  public QuestBranch branchId(@Nullable String branchId) {
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

  public QuestBranch name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Peaceful Resolution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public QuestBranch description(@Nullable String description) {
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

  public QuestBranch requirements(@Nullable QuestBranchRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable QuestBranchRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable QuestBranchRequirements requirements) {
    this.requirements = requirements;
  }

  public QuestBranch consequences(@Nullable QuestBranchConsequences consequences) {
    this.consequences = consequences;
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public @Nullable QuestBranchConsequences getConsequences() {
    return consequences;
  }

  public void setConsequences(@Nullable QuestBranchConsequences consequences) {
    this.consequences = consequences;
  }

  public QuestBranch leadsToEndings(List<String> leadsToEndings) {
    this.leadsToEndings = leadsToEndings;
    return this;
  }

  public QuestBranch addLeadsToEndingsItem(String leadsToEndingsItem) {
    if (this.leadsToEndings == null) {
      this.leadsToEndings = new ArrayList<>();
    }
    this.leadsToEndings.add(leadsToEndingsItem);
    return this;
  }

  /**
   * Какие концовки доступны из этой ветки
   * @return leadsToEndings
   */
  
  @Schema(name = "leads_to_endings", description = "Какие концовки доступны из этой ветки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leads_to_endings")
  public List<String> getLeadsToEndings() {
    return leadsToEndings;
  }

  public void setLeadsToEndings(List<String> leadsToEndings) {
    this.leadsToEndings = leadsToEndings;
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
        Objects.equals(this.name, questBranch.name) &&
        Objects.equals(this.description, questBranch.description) &&
        Objects.equals(this.requirements, questBranch.requirements) &&
        Objects.equals(this.consequences, questBranch.consequences) &&
        Objects.equals(this.leadsToEndings, questBranch.leadsToEndings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branchId, name, description, requirements, consequences, leadsToEndings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestBranch {\n");
    sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    leadsToEndings: ").append(toIndentedString(leadsToEndings)).append("\n");
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

