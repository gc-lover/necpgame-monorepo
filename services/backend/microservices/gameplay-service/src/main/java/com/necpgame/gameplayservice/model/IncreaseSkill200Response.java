package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * IncreaseSkill200Response
 */

@JsonTypeName("increaseSkill_200_response")

public class IncreaseSkill200Response {

  private @Nullable String skillId;

  private @Nullable Integer newRank;

  private @Nullable BigDecimal newProgress;

  private @Nullable Boolean rankIncreased;

  @Valid
  private List<String> bonusesUnlocked = new ArrayList<>();

  public IncreaseSkill200Response skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public IncreaseSkill200Response newRank(@Nullable Integer newRank) {
    this.newRank = newRank;
    return this;
  }

  /**
   * Get newRank
   * @return newRank
   */
  
  @Schema(name = "new_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_rank")
  public @Nullable Integer getNewRank() {
    return newRank;
  }

  public void setNewRank(@Nullable Integer newRank) {
    this.newRank = newRank;
  }

  public IncreaseSkill200Response newProgress(@Nullable BigDecimal newProgress) {
    this.newProgress = newProgress;
    return this;
  }

  /**
   * Get newProgress
   * @return newProgress
   */
  @Valid 
  @Schema(name = "new_progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_progress")
  public @Nullable BigDecimal getNewProgress() {
    return newProgress;
  }

  public void setNewProgress(@Nullable BigDecimal newProgress) {
    this.newProgress = newProgress;
  }

  public IncreaseSkill200Response rankIncreased(@Nullable Boolean rankIncreased) {
    this.rankIncreased = rankIncreased;
    return this;
  }

  /**
   * Get rankIncreased
   * @return rankIncreased
   */
  
  @Schema(name = "rank_increased", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank_increased")
  public @Nullable Boolean getRankIncreased() {
    return rankIncreased;
  }

  public void setRankIncreased(@Nullable Boolean rankIncreased) {
    this.rankIncreased = rankIncreased;
  }

  public IncreaseSkill200Response bonusesUnlocked(List<String> bonusesUnlocked) {
    this.bonusesUnlocked = bonusesUnlocked;
    return this;
  }

  public IncreaseSkill200Response addBonusesUnlockedItem(String bonusesUnlockedItem) {
    if (this.bonusesUnlocked == null) {
      this.bonusesUnlocked = new ArrayList<>();
    }
    this.bonusesUnlocked.add(bonusesUnlockedItem);
    return this;
  }

  /**
   * Get bonusesUnlocked
   * @return bonusesUnlocked
   */
  
  @Schema(name = "bonuses_unlocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses_unlocked")
  public List<String> getBonusesUnlocked() {
    return bonusesUnlocked;
  }

  public void setBonusesUnlocked(List<String> bonusesUnlocked) {
    this.bonusesUnlocked = bonusesUnlocked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncreaseSkill200Response increaseSkill200Response = (IncreaseSkill200Response) o;
    return Objects.equals(this.skillId, increaseSkill200Response.skillId) &&
        Objects.equals(this.newRank, increaseSkill200Response.newRank) &&
        Objects.equals(this.newProgress, increaseSkill200Response.newProgress) &&
        Objects.equals(this.rankIncreased, increaseSkill200Response.rankIncreased) &&
        Objects.equals(this.bonusesUnlocked, increaseSkill200Response.bonusesUnlocked);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, newRank, newProgress, rankIncreased, bonusesUnlocked);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncreaseSkill200Response {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    newRank: ").append(toIndentedString(newRank)).append("\n");
    sb.append("    newProgress: ").append(toIndentedString(newProgress)).append("\n");
    sb.append("    rankIncreased: ").append(toIndentedString(rankIncreased)).append("\n");
    sb.append("    bonusesUnlocked: ").append(toIndentedString(bonusesUnlocked)).append("\n");
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

