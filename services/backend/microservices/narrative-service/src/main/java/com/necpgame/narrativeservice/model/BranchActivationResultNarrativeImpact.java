package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * BranchActivationResultNarrativeImpact
 */

@JsonTypeName("BranchActivationResult_narrative_impact")

public class BranchActivationResultNarrativeImpact {

  /**
   * Gets or Sets significance
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

  @Valid
  private List<String> affectedQuests = new ArrayList<>();

  @Valid
  private List<String> affectedRelationships = new ArrayList<>();

  public BranchActivationResultNarrativeImpact significance(@Nullable SignificanceEnum significance) {
    this.significance = significance;
    return this;
  }

  /**
   * Get significance
   * @return significance
   */
  
  @Schema(name = "significance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("significance")
  public @Nullable SignificanceEnum getSignificance() {
    return significance;
  }

  public void setSignificance(@Nullable SignificanceEnum significance) {
    this.significance = significance;
  }

  public BranchActivationResultNarrativeImpact affectedQuests(List<String> affectedQuests) {
    this.affectedQuests = affectedQuests;
    return this;
  }

  public BranchActivationResultNarrativeImpact addAffectedQuestsItem(String affectedQuestsItem) {
    if (this.affectedQuests == null) {
      this.affectedQuests = new ArrayList<>();
    }
    this.affectedQuests.add(affectedQuestsItem);
    return this;
  }

  /**
   * Get affectedQuests
   * @return affectedQuests
   */
  
  @Schema(name = "affected_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_quests")
  public List<String> getAffectedQuests() {
    return affectedQuests;
  }

  public void setAffectedQuests(List<String> affectedQuests) {
    this.affectedQuests = affectedQuests;
  }

  public BranchActivationResultNarrativeImpact affectedRelationships(List<String> affectedRelationships) {
    this.affectedRelationships = affectedRelationships;
    return this;
  }

  public BranchActivationResultNarrativeImpact addAffectedRelationshipsItem(String affectedRelationshipsItem) {
    if (this.affectedRelationships == null) {
      this.affectedRelationships = new ArrayList<>();
    }
    this.affectedRelationships.add(affectedRelationshipsItem);
    return this;
  }

  /**
   * Get affectedRelationships
   * @return affectedRelationships
   */
  
  @Schema(name = "affected_relationships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_relationships")
  public List<String> getAffectedRelationships() {
    return affectedRelationships;
  }

  public void setAffectedRelationships(List<String> affectedRelationships) {
    this.affectedRelationships = affectedRelationships;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BranchActivationResultNarrativeImpact branchActivationResultNarrativeImpact = (BranchActivationResultNarrativeImpact) o;
    return Objects.equals(this.significance, branchActivationResultNarrativeImpact.significance) &&
        Objects.equals(this.affectedQuests, branchActivationResultNarrativeImpact.affectedQuests) &&
        Objects.equals(this.affectedRelationships, branchActivationResultNarrativeImpact.affectedRelationships);
  }

  @Override
  public int hashCode() {
    return Objects.hash(significance, affectedQuests, affectedRelationships);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BranchActivationResultNarrativeImpact {\n");
    sb.append("    significance: ").append(toIndentedString(significance)).append("\n");
    sb.append("    affectedQuests: ").append(toIndentedString(affectedQuests)).append("\n");
    sb.append("    affectedRelationships: ").append(toIndentedString(affectedRelationships)).append("\n");
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

