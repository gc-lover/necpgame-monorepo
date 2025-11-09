package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.Consequence;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerChoice
 */


public class PlayerChoice {

  private @Nullable String choiceId;

  private @Nullable String questId;

  private @Nullable String questName;

  private @Nullable String choiceText;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable String branchActivated;

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
  private List<@Valid Consequence> consequences = new ArrayList<>();

  public PlayerChoice choiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
    return this;
  }

  /**
   * Get choiceId
   * @return choiceId
   */
  
  @Schema(name = "choice_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_id")
  public @Nullable String getChoiceId() {
    return choiceId;
  }

  public void setChoiceId(@Nullable String choiceId) {
    this.choiceId = choiceId;
  }

  public PlayerChoice questId(@Nullable String questId) {
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

  public PlayerChoice questName(@Nullable String questName) {
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

  public PlayerChoice choiceText(@Nullable String choiceText) {
    this.choiceText = choiceText;
    return this;
  }

  /**
   * Get choiceText
   * @return choiceText
   */
  
  @Schema(name = "choice_text", example = "Side with Militech", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("choice_text")
  public @Nullable String getChoiceText() {
    return choiceText;
  }

  public void setChoiceText(@Nullable String choiceText) {
    this.choiceText = choiceText;
  }

  public PlayerChoice timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public PlayerChoice branchActivated(@Nullable String branchActivated) {
    this.branchActivated = branchActivated;
    return this;
  }

  /**
   * Branch ID активированный choice
   * @return branchActivated
   */
  
  @Schema(name = "branch_activated", description = "Branch ID активированный choice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branch_activated")
  public @Nullable String getBranchActivated() {
    return branchActivated;
  }

  public void setBranchActivated(@Nullable String branchActivated) {
    this.branchActivated = branchActivated;
  }

  public PlayerChoice significance(@Nullable SignificanceEnum significance) {
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

  public PlayerChoice consequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
    return this;
  }

  public PlayerChoice addConsequencesItem(Consequence consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  @Valid 
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<@Valid Consequence> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<@Valid Consequence> consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerChoice playerChoice = (PlayerChoice) o;
    return Objects.equals(this.choiceId, playerChoice.choiceId) &&
        Objects.equals(this.questId, playerChoice.questId) &&
        Objects.equals(this.questName, playerChoice.questName) &&
        Objects.equals(this.choiceText, playerChoice.choiceText) &&
        Objects.equals(this.timestamp, playerChoice.timestamp) &&
        Objects.equals(this.branchActivated, playerChoice.branchActivated) &&
        Objects.equals(this.significance, playerChoice.significance) &&
        Objects.equals(this.consequences, playerChoice.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(choiceId, questId, questName, choiceText, timestamp, branchActivated, significance, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerChoice {\n");
    sb.append("    choiceId: ").append(toIndentedString(choiceId)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    questName: ").append(toIndentedString(questName)).append("\n");
    sb.append("    choiceText: ").append(toIndentedString(choiceText)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    branchActivated: ").append(toIndentedString(branchActivated)).append("\n");
    sb.append("    significance: ").append(toIndentedString(significance)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

