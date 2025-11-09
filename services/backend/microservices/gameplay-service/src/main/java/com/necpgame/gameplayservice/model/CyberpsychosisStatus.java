package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CyberpsychosisStatusRiskFactors;
import com.necpgame.gameplayservice.model.CyberpsychosisStatusSocialImpact;
import com.necpgame.gameplayservice.model.CyberpsychosisSymptom;
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
 * CyberpsychosisStatus
 */


public class CyberpsychosisStatus {

  private @Nullable String characterId;

  private @Nullable BigDecimal humanityCurrent;

  private @Nullable BigDecimal humanityMax;

  private @Nullable BigDecimal humanityLost;

  /**
   * - none: 0-10% потери - early: 10-25% потери - moderate: 25-50% потери - late: 50-75% потери - cyberpsychosis: 75-100% потери 
   */
  public enum StageEnum {
    NONE("none"),
    
    EARLY("early"),
    
    MODERATE("moderate"),
    
    LATE("late"),
    
    CYBERPSYCHOSIS("cyberpsychosis");

    private final String value;

    StageEnum(String value) {
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
    public static StageEnum fromValue(String value) {
      for (StageEnum b : StageEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StageEnum stage;

  private @Nullable BigDecimal progressionRate;

  @Valid
  private List<@Valid CyberpsychosisSymptom> activeSymptoms = new ArrayList<>();

  private @Nullable CyberpsychosisStatusRiskFactors riskFactors;

  private @Nullable CyberpsychosisStatusSocialImpact socialImpact;

  public CyberpsychosisStatus characterId(@Nullable String characterId) {
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

  public CyberpsychosisStatus humanityCurrent(@Nullable BigDecimal humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
    return this;
  }

  /**
   * Текущая человечность (0-100%)
   * @return humanityCurrent
   */
  @Valid 
  @Schema(name = "humanity_current", description = "Текущая человечность (0-100%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_current")
  public @Nullable BigDecimal getHumanityCurrent() {
    return humanityCurrent;
  }

  public void setHumanityCurrent(@Nullable BigDecimal humanityCurrent) {
    this.humanityCurrent = humanityCurrent;
  }

  public CyberpsychosisStatus humanityMax(@Nullable BigDecimal humanityMax) {
    this.humanityMax = humanityMax;
    return this;
  }

  /**
   * Максимальная человечность (может снижаться от \"шрамов\")
   * @return humanityMax
   */
  @Valid 
  @Schema(name = "humanity_max", description = "Максимальная человечность (может снижаться от \"шрамов\")", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_max")
  public @Nullable BigDecimal getHumanityMax() {
    return humanityMax;
  }

  public void setHumanityMax(@Nullable BigDecimal humanityMax) {
    this.humanityMax = humanityMax;
  }

  public CyberpsychosisStatus humanityLost(@Nullable BigDecimal humanityLost) {
    this.humanityLost = humanityLost;
    return this;
  }

  /**
   * Потеряно человечности (%)
   * @return humanityLost
   */
  @Valid 
  @Schema(name = "humanity_lost", description = "Потеряно человечности (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_lost")
  public @Nullable BigDecimal getHumanityLost() {
    return humanityLost;
  }

  public void setHumanityLost(@Nullable BigDecimal humanityLost) {
    this.humanityLost = humanityLost;
  }

  public CyberpsychosisStatus stage(@Nullable StageEnum stage) {
    this.stage = stage;
    return this;
  }

  /**
   * - none: 0-10% потери - early: 10-25% потери - moderate: 25-50% потери - late: 50-75% потери - cyberpsychosis: 75-100% потери 
   * @return stage
   */
  
  @Schema(name = "stage", description = "- none: 0-10% потери - early: 10-25% потери - moderate: 25-50% потери - late: 50-75% потери - cyberpsychosis: 75-100% потери ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable StageEnum getStage() {
    return stage;
  }

  public void setStage(@Nullable StageEnum stage) {
    this.stage = stage;
  }

  public CyberpsychosisStatus progressionRate(@Nullable BigDecimal progressionRate) {
    this.progressionRate = progressionRate;
    return this;
  }

  /**
   * Скорость прогрессии (человечность в день)
   * @return progressionRate
   */
  @Valid 
  @Schema(name = "progression_rate", description = "Скорость прогрессии (человечность в день)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression_rate")
  public @Nullable BigDecimal getProgressionRate() {
    return progressionRate;
  }

  public void setProgressionRate(@Nullable BigDecimal progressionRate) {
    this.progressionRate = progressionRate;
  }

  public CyberpsychosisStatus activeSymptoms(List<@Valid CyberpsychosisSymptom> activeSymptoms) {
    this.activeSymptoms = activeSymptoms;
    return this;
  }

  public CyberpsychosisStatus addActiveSymptomsItem(CyberpsychosisSymptom activeSymptomsItem) {
    if (this.activeSymptoms == null) {
      this.activeSymptoms = new ArrayList<>();
    }
    this.activeSymptoms.add(activeSymptomsItem);
    return this;
  }

  /**
   * Get activeSymptoms
   * @return activeSymptoms
   */
  @Valid 
  @Schema(name = "active_symptoms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_symptoms")
  public List<@Valid CyberpsychosisSymptom> getActiveSymptoms() {
    return activeSymptoms;
  }

  public void setActiveSymptoms(List<@Valid CyberpsychosisSymptom> activeSymptoms) {
    this.activeSymptoms = activeSymptoms;
  }

  public CyberpsychosisStatus riskFactors(@Nullable CyberpsychosisStatusRiskFactors riskFactors) {
    this.riskFactors = riskFactors;
    return this;
  }

  /**
   * Get riskFactors
   * @return riskFactors
   */
  @Valid 
  @Schema(name = "risk_factors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_factors")
  public @Nullable CyberpsychosisStatusRiskFactors getRiskFactors() {
    return riskFactors;
  }

  public void setRiskFactors(@Nullable CyberpsychosisStatusRiskFactors riskFactors) {
    this.riskFactors = riskFactors;
  }

  public CyberpsychosisStatus socialImpact(@Nullable CyberpsychosisStatusSocialImpact socialImpact) {
    this.socialImpact = socialImpact;
    return this;
  }

  /**
   * Get socialImpact
   * @return socialImpact
   */
  @Valid 
  @Schema(name = "social_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social_impact")
  public @Nullable CyberpsychosisStatusSocialImpact getSocialImpact() {
    return socialImpact;
  }

  public void setSocialImpact(@Nullable CyberpsychosisStatusSocialImpact socialImpact) {
    this.socialImpact = socialImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberpsychosisStatus cyberpsychosisStatus = (CyberpsychosisStatus) o;
    return Objects.equals(this.characterId, cyberpsychosisStatus.characterId) &&
        Objects.equals(this.humanityCurrent, cyberpsychosisStatus.humanityCurrent) &&
        Objects.equals(this.humanityMax, cyberpsychosisStatus.humanityMax) &&
        Objects.equals(this.humanityLost, cyberpsychosisStatus.humanityLost) &&
        Objects.equals(this.stage, cyberpsychosisStatus.stage) &&
        Objects.equals(this.progressionRate, cyberpsychosisStatus.progressionRate) &&
        Objects.equals(this.activeSymptoms, cyberpsychosisStatus.activeSymptoms) &&
        Objects.equals(this.riskFactors, cyberpsychosisStatus.riskFactors) &&
        Objects.equals(this.socialImpact, cyberpsychosisStatus.socialImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, humanityCurrent, humanityMax, humanityLost, stage, progressionRate, activeSymptoms, riskFactors, socialImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisStatus {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    humanityCurrent: ").append(toIndentedString(humanityCurrent)).append("\n");
    sb.append("    humanityMax: ").append(toIndentedString(humanityMax)).append("\n");
    sb.append("    humanityLost: ").append(toIndentedString(humanityLost)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    progressionRate: ").append(toIndentedString(progressionRate)).append("\n");
    sb.append("    activeSymptoms: ").append(toIndentedString(activeSymptoms)).append("\n");
    sb.append("    riskFactors: ").append(toIndentedString(riskFactors)).append("\n");
    sb.append("    socialImpact: ").append(toIndentedString(socialImpact)).append("\n");
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

