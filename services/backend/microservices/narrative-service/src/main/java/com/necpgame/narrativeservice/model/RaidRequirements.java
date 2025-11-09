package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * RaidRequirements
 */


public class RaidRequirements {

  private @Nullable String characterId;

  private @Nullable Boolean eligible;

  private @Nullable Integer level;

  private @Nullable Integer levelRequired;

  private @Nullable Integer gearScore;

  private @Nullable Integer gearScoreRequired;

  private @Nullable Integer netwatchReputation;

  private @Nullable Integer netwatchReputationRequired;

  @Valid
  private List<String> completedQuests = new ArrayList<>();

  @Valid
  private List<String> requiredQuests = new ArrayList<>();

  private @Nullable Boolean accessTokenRequired;

  @Valid
  private List<String> reasons = new ArrayList<>();

  public RaidRequirements characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public RaidRequirements eligible(@Nullable Boolean eligible) {
    this.eligible = eligible;
    return this;
  }

  /**
   * Get eligible
   * @return eligible
   */
  
  @Schema(name = "eligible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eligible")
  public @Nullable Boolean getEligible() {
    return eligible;
  }

  public void setEligible(@Nullable Boolean eligible) {
    this.eligible = eligible;
  }

  public RaidRequirements level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public RaidRequirements levelRequired(@Nullable Integer levelRequired) {
    this.levelRequired = levelRequired;
    return this;
  }

  /**
   * Get levelRequired
   * @return levelRequired
   */
  
  @Schema(name = "level_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_required")
  public @Nullable Integer getLevelRequired() {
    return levelRequired;
  }

  public void setLevelRequired(@Nullable Integer levelRequired) {
    this.levelRequired = levelRequired;
  }

  public RaidRequirements gearScore(@Nullable Integer gearScore) {
    this.gearScore = gearScore;
    return this;
  }

  /**
   * Get gearScore
   * @return gearScore
   */
  
  @Schema(name = "gear_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gear_score")
  public @Nullable Integer getGearScore() {
    return gearScore;
  }

  public void setGearScore(@Nullable Integer gearScore) {
    this.gearScore = gearScore;
  }

  public RaidRequirements gearScoreRequired(@Nullable Integer gearScoreRequired) {
    this.gearScoreRequired = gearScoreRequired;
    return this;
  }

  /**
   * Get gearScoreRequired
   * @return gearScoreRequired
   */
  
  @Schema(name = "gear_score_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gear_score_required")
  public @Nullable Integer getGearScoreRequired() {
    return gearScoreRequired;
  }

  public void setGearScoreRequired(@Nullable Integer gearScoreRequired) {
    this.gearScoreRequired = gearScoreRequired;
  }

  public RaidRequirements netwatchReputation(@Nullable Integer netwatchReputation) {
    this.netwatchReputation = netwatchReputation;
    return this;
  }

  /**
   * Get netwatchReputation
   * @return netwatchReputation
   */
  
  @Schema(name = "netwatch_reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("netwatch_reputation")
  public @Nullable Integer getNetwatchReputation() {
    return netwatchReputation;
  }

  public void setNetwatchReputation(@Nullable Integer netwatchReputation) {
    this.netwatchReputation = netwatchReputation;
  }

  public RaidRequirements netwatchReputationRequired(@Nullable Integer netwatchReputationRequired) {
    this.netwatchReputationRequired = netwatchReputationRequired;
    return this;
  }

  /**
   * Get netwatchReputationRequired
   * @return netwatchReputationRequired
   */
  
  @Schema(name = "netwatch_reputation_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("netwatch_reputation_required")
  public @Nullable Integer getNetwatchReputationRequired() {
    return netwatchReputationRequired;
  }

  public void setNetwatchReputationRequired(@Nullable Integer netwatchReputationRequired) {
    this.netwatchReputationRequired = netwatchReputationRequired;
  }

  public RaidRequirements completedQuests(List<String> completedQuests) {
    this.completedQuests = completedQuests;
    return this;
  }

  public RaidRequirements addCompletedQuestsItem(String completedQuestsItem) {
    if (this.completedQuests == null) {
      this.completedQuests = new ArrayList<>();
    }
    this.completedQuests.add(completedQuestsItem);
    return this;
  }

  /**
   * Get completedQuests
   * @return completedQuests
   */
  
  @Schema(name = "completed_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_quests")
  public List<String> getCompletedQuests() {
    return completedQuests;
  }

  public void setCompletedQuests(List<String> completedQuests) {
    this.completedQuests = completedQuests;
  }

  public RaidRequirements requiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
    return this;
  }

  public RaidRequirements addRequiredQuestsItem(String requiredQuestsItem) {
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
  
  @Schema(name = "required_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_quests")
  public List<String> getRequiredQuests() {
    return requiredQuests;
  }

  public void setRequiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
  }

  public RaidRequirements accessTokenRequired(@Nullable Boolean accessTokenRequired) {
    this.accessTokenRequired = accessTokenRequired;
    return this;
  }

  /**
   * Get accessTokenRequired
   * @return accessTokenRequired
   */
  
  @Schema(name = "access_token_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_token_required")
  public @Nullable Boolean getAccessTokenRequired() {
    return accessTokenRequired;
  }

  public void setAccessTokenRequired(@Nullable Boolean accessTokenRequired) {
    this.accessTokenRequired = accessTokenRequired;
  }

  public RaidRequirements reasons(List<String> reasons) {
    this.reasons = reasons;
    return this;
  }

  public RaidRequirements addReasonsItem(String reasonsItem) {
    if (this.reasons == null) {
      this.reasons = new ArrayList<>();
    }
    this.reasons.add(reasonsItem);
    return this;
  }

  /**
   * Причины, если не подходит
   * @return reasons
   */
  
  @Schema(name = "reasons", description = "Причины, если не подходит", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasons")
  public List<String> getReasons() {
    return reasons;
  }

  public void setReasons(List<String> reasons) {
    this.reasons = reasons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RaidRequirements raidRequirements = (RaidRequirements) o;
    return Objects.equals(this.characterId, raidRequirements.characterId) &&
        Objects.equals(this.eligible, raidRequirements.eligible) &&
        Objects.equals(this.level, raidRequirements.level) &&
        Objects.equals(this.levelRequired, raidRequirements.levelRequired) &&
        Objects.equals(this.gearScore, raidRequirements.gearScore) &&
        Objects.equals(this.gearScoreRequired, raidRequirements.gearScoreRequired) &&
        Objects.equals(this.netwatchReputation, raidRequirements.netwatchReputation) &&
        Objects.equals(this.netwatchReputationRequired, raidRequirements.netwatchReputationRequired) &&
        Objects.equals(this.completedQuests, raidRequirements.completedQuests) &&
        Objects.equals(this.requiredQuests, raidRequirements.requiredQuests) &&
        Objects.equals(this.accessTokenRequired, raidRequirements.accessTokenRequired) &&
        Objects.equals(this.reasons, raidRequirements.reasons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, eligible, level, levelRequired, gearScore, gearScoreRequired, netwatchReputation, netwatchReputationRequired, completedQuests, requiredQuests, accessTokenRequired, reasons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RaidRequirements {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    eligible: ").append(toIndentedString(eligible)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    levelRequired: ").append(toIndentedString(levelRequired)).append("\n");
    sb.append("    gearScore: ").append(toIndentedString(gearScore)).append("\n");
    sb.append("    gearScoreRequired: ").append(toIndentedString(gearScoreRequired)).append("\n");
    sb.append("    netwatchReputation: ").append(toIndentedString(netwatchReputation)).append("\n");
    sb.append("    netwatchReputationRequired: ").append(toIndentedString(netwatchReputationRequired)).append("\n");
    sb.append("    completedQuests: ").append(toIndentedString(completedQuests)).append("\n");
    sb.append("    requiredQuests: ").append(toIndentedString(requiredQuests)).append("\n");
    sb.append("    accessTokenRequired: ").append(toIndentedString(accessTokenRequired)).append("\n");
    sb.append("    reasons: ").append(toIndentedString(reasons)).append("\n");
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

