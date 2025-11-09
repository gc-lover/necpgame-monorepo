package com.necpgame.gameplayservice.model;

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
 * CompanionProgression
 */


public class CompanionProgression {

  private @Nullable Integer level;

  private @Nullable Integer xp;

  private @Nullable Integer xpToNext;

  private @Nullable Integer skillPoints;

  @Valid
  private List<String> unlockedAbilities = new ArrayList<>();

  private @Nullable Integer synergyScore;

  public CompanionProgression level(@Nullable Integer level) {
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

  public CompanionProgression xp(@Nullable Integer xp) {
    this.xp = xp;
    return this;
  }

  /**
   * Get xp
   * @return xp
   */
  
  @Schema(name = "xp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xp")
  public @Nullable Integer getXp() {
    return xp;
  }

  public void setXp(@Nullable Integer xp) {
    this.xp = xp;
  }

  public CompanionProgression xpToNext(@Nullable Integer xpToNext) {
    this.xpToNext = xpToNext;
    return this;
  }

  /**
   * Get xpToNext
   * @return xpToNext
   */
  
  @Schema(name = "xpToNext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpToNext")
  public @Nullable Integer getXpToNext() {
    return xpToNext;
  }

  public void setXpToNext(@Nullable Integer xpToNext) {
    this.xpToNext = xpToNext;
  }

  public CompanionProgression skillPoints(@Nullable Integer skillPoints) {
    this.skillPoints = skillPoints;
    return this;
  }

  /**
   * Get skillPoints
   * @return skillPoints
   */
  
  @Schema(name = "skillPoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skillPoints")
  public @Nullable Integer getSkillPoints() {
    return skillPoints;
  }

  public void setSkillPoints(@Nullable Integer skillPoints) {
    this.skillPoints = skillPoints;
  }

  public CompanionProgression unlockedAbilities(List<String> unlockedAbilities) {
    this.unlockedAbilities = unlockedAbilities;
    return this;
  }

  public CompanionProgression addUnlockedAbilitiesItem(String unlockedAbilitiesItem) {
    if (this.unlockedAbilities == null) {
      this.unlockedAbilities = new ArrayList<>();
    }
    this.unlockedAbilities.add(unlockedAbilitiesItem);
    return this;
  }

  /**
   * Get unlockedAbilities
   * @return unlockedAbilities
   */
  
  @Schema(name = "unlockedAbilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlockedAbilities")
  public List<String> getUnlockedAbilities() {
    return unlockedAbilities;
  }

  public void setUnlockedAbilities(List<String> unlockedAbilities) {
    this.unlockedAbilities = unlockedAbilities;
  }

  public CompanionProgression synergyScore(@Nullable Integer synergyScore) {
    this.synergyScore = synergyScore;
    return this;
  }

  /**
   * Синергия с классом игрока
   * @return synergyScore
   */
  
  @Schema(name = "synergyScore", description = "Синергия с классом игрока", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergyScore")
  public @Nullable Integer getSynergyScore() {
    return synergyScore;
  }

  public void setSynergyScore(@Nullable Integer synergyScore) {
    this.synergyScore = synergyScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionProgression companionProgression = (CompanionProgression) o;
    return Objects.equals(this.level, companionProgression.level) &&
        Objects.equals(this.xp, companionProgression.xp) &&
        Objects.equals(this.xpToNext, companionProgression.xpToNext) &&
        Objects.equals(this.skillPoints, companionProgression.skillPoints) &&
        Objects.equals(this.unlockedAbilities, companionProgression.unlockedAbilities) &&
        Objects.equals(this.synergyScore, companionProgression.synergyScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, xp, xpToNext, skillPoints, unlockedAbilities, synergyScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionProgression {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    xp: ").append(toIndentedString(xp)).append("\n");
    sb.append("    xpToNext: ").append(toIndentedString(xpToNext)).append("\n");
    sb.append("    skillPoints: ").append(toIndentedString(skillPoints)).append("\n");
    sb.append("    unlockedAbilities: ").append(toIndentedString(unlockedAbilities)).append("\n");
    sb.append("    synergyScore: ").append(toIndentedString(synergyScore)).append("\n");
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

