package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.QuestRewards;
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
 * QuestEndingConsequences
 */

@JsonTypeName("QuestEnding_consequences")

public class QuestEndingConsequences {

  @Valid
  private Map<String, Integer> reputationImpact = new HashMap<>();

  @Valid
  private Map<String, Integer> factionRelations = new HashMap<>();

  @Valid
  private List<String> unlockedContent = new ArrayList<>();

  private @Nullable QuestRewards rewards;

  public QuestEndingConsequences reputationImpact(Map<String, Integer> reputationImpact) {
    this.reputationImpact = reputationImpact;
    return this;
  }

  public QuestEndingConsequences putReputationImpactItem(String key, Integer reputationImpactItem) {
    if (this.reputationImpact == null) {
      this.reputationImpact = new HashMap<>();
    }
    this.reputationImpact.put(key, reputationImpactItem);
    return this;
  }

  /**
   * Get reputationImpact
   * @return reputationImpact
   */
  
  @Schema(name = "reputation_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_impact")
  public Map<String, Integer> getReputationImpact() {
    return reputationImpact;
  }

  public void setReputationImpact(Map<String, Integer> reputationImpact) {
    this.reputationImpact = reputationImpact;
  }

  public QuestEndingConsequences factionRelations(Map<String, Integer> factionRelations) {
    this.factionRelations = factionRelations;
    return this;
  }

  public QuestEndingConsequences putFactionRelationsItem(String key, Integer factionRelationsItem) {
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

  public QuestEndingConsequences unlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
    return this;
  }

  public QuestEndingConsequences addUnlockedContentItem(String unlockedContentItem) {
    if (this.unlockedContent == null) {
      this.unlockedContent = new ArrayList<>();
    }
    this.unlockedContent.add(unlockedContentItem);
    return this;
  }

  /**
   * Get unlockedContent
   * @return unlockedContent
   */
  
  @Schema(name = "unlocked_content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_content")
  public List<String> getUnlockedContent() {
    return unlockedContent;
  }

  public void setUnlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
  }

  public QuestEndingConsequences rewards(@Nullable QuestRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable QuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable QuestRewards rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestEndingConsequences questEndingConsequences = (QuestEndingConsequences) o;
    return Objects.equals(this.reputationImpact, questEndingConsequences.reputationImpact) &&
        Objects.equals(this.factionRelations, questEndingConsequences.factionRelations) &&
        Objects.equals(this.unlockedContent, questEndingConsequences.unlockedContent) &&
        Objects.equals(this.rewards, questEndingConsequences.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationImpact, factionRelations, unlockedContent, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestEndingConsequences {\n");
    sb.append("    reputationImpact: ").append(toIndentedString(reputationImpact)).append("\n");
    sb.append("    factionRelations: ").append(toIndentedString(factionRelations)).append("\n");
    sb.append("    unlockedContent: ").append(toIndentedString(unlockedContent)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

