package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestBranchConsequences
 */

@JsonTypeName("QuestBranch_consequences")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestBranchConsequences {

  @Valid
  private Map<String, Integer> reputationChanges = new HashMap<>();

  @Valid
  private Map<String, Integer> factionRelations = new HashMap<>();

  @Valid
  private List<String> unlocks = new ArrayList<>();

  public QuestBranchConsequences reputationChanges(Map<String, Integer> reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  public QuestBranchConsequences putReputationChangesItem(String key, Integer reputationChangesItem) {
    if (this.reputationChanges == null) {
      this.reputationChanges = new HashMap<>();
    }
    this.reputationChanges.put(key, reputationChangesItem);
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  
  @Schema(name = "reputation_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_changes")
  public Map<String, Integer> getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(Map<String, Integer> reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  public QuestBranchConsequences factionRelations(Map<String, Integer> factionRelations) {
    this.factionRelations = factionRelations;
    return this;
  }

  public QuestBranchConsequences putFactionRelationsItem(String key, Integer factionRelationsItem) {
    if (this.factionRelations == null) {
      this.factionRelations = new HashMap<>();
    }
    this.factionRelations.put(key, factionRelationsItem);
    return this;
  }

  /**
   * Get factionRelations
   * @return factionRelations
   */
  
  @Schema(name = "faction_relations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_relations")
  public Map<String, Integer> getFactionRelations() {
    return factionRelations;
  }

  public void setFactionRelations(Map<String, Integer> factionRelations) {
    this.factionRelations = factionRelations;
  }

  public QuestBranchConsequences unlocks(List<String> unlocks) {
    this.unlocks = unlocks;
    return this;
  }

  public QuestBranchConsequences addUnlocksItem(String unlocksItem) {
    if (this.unlocks == null) {
      this.unlocks = new ArrayList<>();
    }
    this.unlocks.add(unlocksItem);
    return this;
  }

  /**
   * Get unlocks
   * @return unlocks
   */
  
  @Schema(name = "unlocks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocks")
  public List<String> getUnlocks() {
    return unlocks;
  }

  public void setUnlocks(List<String> unlocks) {
    this.unlocks = unlocks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestBranchConsequences questBranchConsequences = (QuestBranchConsequences) o;
    return Objects.equals(this.reputationChanges, questBranchConsequences.reputationChanges) &&
        Objects.equals(this.factionRelations, questBranchConsequences.factionRelations) &&
        Objects.equals(this.unlocks, questBranchConsequences.unlocks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationChanges, factionRelations, unlocks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestBranchConsequences {\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
    sb.append("    factionRelations: ").append(toIndentedString(factionRelations)).append("\n");
    sb.append("    unlocks: ").append(toIndentedString(unlocks)).append("\n");
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

