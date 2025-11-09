package com.necpgame.narrativeservice.model;

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
 * QuestRequirements
 */

@JsonTypeName("Quest_requirements")

public class QuestRequirements {

  private @Nullable Integer minLevel;

  @Valid
  private List<String> requiredQuests = new ArrayList<>();

  @Valid
  private Map<String, Integer> minReputation = new HashMap<>();

  public QuestRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Get minLevel
   * @return minLevel
   */
  
  @Schema(name = "minLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public QuestRequirements requiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
    return this;
  }

  public QuestRequirements addRequiredQuestsItem(String requiredQuestsItem) {
    if (this.requiredQuests == null) {
      this.requiredQuests = new ArrayList<>();
    }
    this.requiredQuests.add(requiredQuestsItem);
    return this;
  }

  /**
   * Get requiredQuests
   * @return requiredQuests
   */
  
  @Schema(name = "requiredQuests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredQuests")
  public List<String> getRequiredQuests() {
    return requiredQuests;
  }

  public void setRequiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
  }

  public QuestRequirements minReputation(Map<String, Integer> minReputation) {
    this.minReputation = minReputation;
    return this;
  }

  public QuestRequirements putMinReputationItem(String key, Integer minReputationItem) {
    if (this.minReputation == null) {
      this.minReputation = new HashMap<>();
    }
    this.minReputation.put(key, minReputationItem);
    return this;
  }

  /**
   * Get minReputation
   * @return minReputation
   */
  
  @Schema(name = "minReputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minReputation")
  public Map<String, Integer> getMinReputation() {
    return minReputation;
  }

  public void setMinReputation(Map<String, Integer> minReputation) {
    this.minReputation = minReputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestRequirements questRequirements = (QuestRequirements) o;
    return Objects.equals(this.minLevel, questRequirements.minLevel) &&
        Objects.equals(this.requiredQuests, questRequirements.requiredQuests) &&
        Objects.equals(this.minReputation, questRequirements.minReputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, requiredQuests, minReputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    requiredQuests: ").append(toIndentedString(requiredQuests)).append("\n");
    sb.append("    minReputation: ").append(toIndentedString(minReputation)).append("\n");
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

