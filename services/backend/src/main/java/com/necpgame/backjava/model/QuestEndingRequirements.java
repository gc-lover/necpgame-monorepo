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
 * QuestEndingRequirements
 */

@JsonTypeName("QuestEnding_requirements")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestEndingRequirements {

  private @Nullable String branch;

  @Valid
  private List<String> choices = new ArrayList<>();

  @Valid
  private Map<String, Integer> reputationMin = new HashMap<>();

  public QuestEndingRequirements branch(@Nullable String branch) {
    this.branch = branch;
    return this;
  }

  /**
   * Get branch
   * @return branch
   */
  
  @Schema(name = "branch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branch")
  public @Nullable String getBranch() {
    return branch;
  }

  public void setBranch(@Nullable String branch) {
    this.branch = branch;
  }

  public QuestEndingRequirements choices(List<String> choices) {
    this.choices = choices;
    return this;
  }

  public QuestEndingRequirements addChoicesItem(String choicesItem) {
    if (this.choices == null) {
      this.choices = new ArrayList<>();
    }
    this.choices.add(choicesItem);
    return this;
  }

  /**
   * Get choices
   * @return choices
   */
  
  @Schema(name = "choices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choices")
  public List<String> getChoices() {
    return choices;
  }

  public void setChoices(List<String> choices) {
    this.choices = choices;
  }

  public QuestEndingRequirements reputationMin(Map<String, Integer> reputationMin) {
    this.reputationMin = reputationMin;
    return this;
  }

  public QuestEndingRequirements putReputationMinItem(String key, Integer reputationMinItem) {
    if (this.reputationMin == null) {
      this.reputationMin = new HashMap<>();
    }
    this.reputationMin.put(key, reputationMinItem);
    return this;
  }

  /**
   * Get reputationMin
   * @return reputationMin
   */
  
  @Schema(name = "reputation_min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_min")
  public Map<String, Integer> getReputationMin() {
    return reputationMin;
  }

  public void setReputationMin(Map<String, Integer> reputationMin) {
    this.reputationMin = reputationMin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestEndingRequirements questEndingRequirements = (QuestEndingRequirements) o;
    return Objects.equals(this.branch, questEndingRequirements.branch) &&
        Objects.equals(this.choices, questEndingRequirements.choices) &&
        Objects.equals(this.reputationMin, questEndingRequirements.reputationMin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(branch, choices, reputationMin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestEndingRequirements {\n");
    sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
    sb.append("    choices: ").append(toIndentedString(choices)).append("\n");
    sb.append("    reputationMin: ").append(toIndentedString(reputationMin)).append("\n");
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

