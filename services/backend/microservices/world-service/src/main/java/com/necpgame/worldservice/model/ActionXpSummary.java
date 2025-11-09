package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * ActionXpSummary
 */


public class ActionXpSummary {

  private UUID characterId;

  private String skillId;

  private BigDecimal dailyXpTotal;

  private BigDecimal fatigueModifier;

  private BigDecimal fatigueScore;

  private BigDecimal softCap;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextResetAt;

  @Valid
  private List<String> boostsActive = new ArrayList<>();

  @Valid
  private List<String> recommendations = new ArrayList<>();

  public ActionXpSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpSummary(UUID characterId, String skillId, BigDecimal dailyXpTotal, BigDecimal fatigueModifier, BigDecimal fatigueScore, BigDecimal softCap) {
    this.characterId = characterId;
    this.skillId = skillId;
    this.dailyXpTotal = dailyXpTotal;
    this.fatigueModifier = fatigueModifier;
    this.fatigueScore = fatigueScore;
    this.softCap = softCap;
  }

  public ActionXpSummary characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public ActionXpSummary skillId(String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  @NotNull 
  @Schema(name = "skillId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skillId")
  public String getSkillId() {
    return skillId;
  }

  public void setSkillId(String skillId) {
    this.skillId = skillId;
  }

  public ActionXpSummary dailyXpTotal(BigDecimal dailyXpTotal) {
    this.dailyXpTotal = dailyXpTotal;
    return this;
  }

  /**
   * Get dailyXpTotal
   * @return dailyXpTotal
   */
  @NotNull @Valid 
  @Schema(name = "dailyXpTotal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dailyXpTotal")
  public BigDecimal getDailyXpTotal() {
    return dailyXpTotal;
  }

  public void setDailyXpTotal(BigDecimal dailyXpTotal) {
    this.dailyXpTotal = dailyXpTotal;
  }

  public ActionXpSummary fatigueModifier(BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
    return this;
  }

  /**
   * Get fatigueModifier
   * @return fatigueModifier
   */
  @NotNull @Valid 
  @Schema(name = "fatigueModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fatigueModifier")
  public BigDecimal getFatigueModifier() {
    return fatigueModifier;
  }

  public void setFatigueModifier(BigDecimal fatigueModifier) {
    this.fatigueModifier = fatigueModifier;
  }

  public ActionXpSummary fatigueScore(BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
    return this;
  }

  /**
   * Get fatigueScore
   * @return fatigueScore
   */
  @NotNull @Valid 
  @Schema(name = "fatigueScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fatigueScore")
  public BigDecimal getFatigueScore() {
    return fatigueScore;
  }

  public void setFatigueScore(BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
  }

  public ActionXpSummary softCap(BigDecimal softCap) {
    this.softCap = softCap;
    return this;
  }

  /**
   * Get softCap
   * @return softCap
   */
  @NotNull @Valid 
  @Schema(name = "softCap", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("softCap")
  public BigDecimal getSoftCap() {
    return softCap;
  }

  public void setSoftCap(BigDecimal softCap) {
    this.softCap = softCap;
  }

  public ActionXpSummary nextResetAt(@Nullable OffsetDateTime nextResetAt) {
    this.nextResetAt = nextResetAt;
    return this;
  }

  /**
   * Get nextResetAt
   * @return nextResetAt
   */
  @Valid 
  @Schema(name = "nextResetAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextResetAt")
  public @Nullable OffsetDateTime getNextResetAt() {
    return nextResetAt;
  }

  public void setNextResetAt(@Nullable OffsetDateTime nextResetAt) {
    this.nextResetAt = nextResetAt;
  }

  public ActionXpSummary boostsActive(List<String> boostsActive) {
    this.boostsActive = boostsActive;
    return this;
  }

  public ActionXpSummary addBoostsActiveItem(String boostsActiveItem) {
    if (this.boostsActive == null) {
      this.boostsActive = new ArrayList<>();
    }
    this.boostsActive.add(boostsActiveItem);
    return this;
  }

  /**
   * Get boostsActive
   * @return boostsActive
   */
  
  @Schema(name = "boostsActive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostsActive")
  public List<String> getBoostsActive() {
    return boostsActive;
  }

  public void setBoostsActive(List<String> boostsActive) {
    this.boostsActive = boostsActive;
  }

  public ActionXpSummary recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public ActionXpSummary addRecommendationsItem(String recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
   */
  
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
    this.recommendations = recommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpSummary actionXpSummary = (ActionXpSummary) o;
    return Objects.equals(this.characterId, actionXpSummary.characterId) &&
        Objects.equals(this.skillId, actionXpSummary.skillId) &&
        Objects.equals(this.dailyXpTotal, actionXpSummary.dailyXpTotal) &&
        Objects.equals(this.fatigueModifier, actionXpSummary.fatigueModifier) &&
        Objects.equals(this.fatigueScore, actionXpSummary.fatigueScore) &&
        Objects.equals(this.softCap, actionXpSummary.softCap) &&
        Objects.equals(this.nextResetAt, actionXpSummary.nextResetAt) &&
        Objects.equals(this.boostsActive, actionXpSummary.boostsActive) &&
        Objects.equals(this.recommendations, actionXpSummary.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skillId, dailyXpTotal, fatigueModifier, fatigueScore, softCap, nextResetAt, boostsActive, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpSummary {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    dailyXpTotal: ").append(toIndentedString(dailyXpTotal)).append("\n");
    sb.append("    fatigueModifier: ").append(toIndentedString(fatigueModifier)).append("\n");
    sb.append("    fatigueScore: ").append(toIndentedString(fatigueScore)).append("\n");
    sb.append("    softCap: ").append(toIndentedString(softCap)).append("\n");
    sb.append("    nextResetAt: ").append(toIndentedString(nextResetAt)).append("\n");
    sb.append("    boostsActive: ").append(toIndentedString(boostsActive)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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

