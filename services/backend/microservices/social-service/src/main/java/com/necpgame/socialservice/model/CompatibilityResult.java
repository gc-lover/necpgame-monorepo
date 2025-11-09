package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.CompatibilityResultFactors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompatibilityResult
 */


public class CompatibilityResult {

  private @Nullable UUID characterId;

  private @Nullable String npcId;

  private @Nullable Integer compatibilityScore;

  private @Nullable CompatibilityResultFactors factors;

  /**
   * Gets or Sets recommendation
   */
  public enum RecommendationEnum {
    HIGHLY_COMPATIBLE("HIGHLY_COMPATIBLE"),
    
    COMPATIBLE("COMPATIBLE"),
    
    NEUTRAL("NEUTRAL"),
    
    INCOMPATIBLE("INCOMPATIBLE");

    private final String value;

    RecommendationEnum(String value) {
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
    public static RecommendationEnum fromValue(String value) {
      for (RecommendationEnum b : RecommendationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RecommendationEnum recommendation;

  @Valid
  private List<String> advice = new ArrayList<>();

  public CompatibilityResult characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public CompatibilityResult npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public CompatibilityResult compatibilityScore(@Nullable Integer compatibilityScore) {
    this.compatibilityScore = compatibilityScore;
    return this;
  }

  /**
   * Get compatibilityScore
   * minimum: 0
   * maximum: 100
   * @return compatibilityScore
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "compatibility_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatibility_score")
  public @Nullable Integer getCompatibilityScore() {
    return compatibilityScore;
  }

  public void setCompatibilityScore(@Nullable Integer compatibilityScore) {
    this.compatibilityScore = compatibilityScore;
  }

  public CompatibilityResult factors(@Nullable CompatibilityResultFactors factors) {
    this.factors = factors;
    return this;
  }

  /**
   * Get factors
   * @return factors
   */
  @Valid 
  @Schema(name = "factors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factors")
  public @Nullable CompatibilityResultFactors getFactors() {
    return factors;
  }

  public void setFactors(@Nullable CompatibilityResultFactors factors) {
    this.factors = factors;
  }

  public CompatibilityResult recommendation(@Nullable RecommendationEnum recommendation) {
    this.recommendation = recommendation;
    return this;
  }

  /**
   * Get recommendation
   * @return recommendation
   */
  
  @Schema(name = "recommendation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendation")
  public @Nullable RecommendationEnum getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(@Nullable RecommendationEnum recommendation) {
    this.recommendation = recommendation;
  }

  public CompatibilityResult advice(List<String> advice) {
    this.advice = advice;
    return this;
  }

  public CompatibilityResult addAdviceItem(String adviceItem) {
    if (this.advice == null) {
      this.advice = new ArrayList<>();
    }
    this.advice.add(adviceItem);
    return this;
  }

  /**
   * Get advice
   * @return advice
   */
  
  @Schema(name = "advice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advice")
  public List<String> getAdvice() {
    return advice;
  }

  public void setAdvice(List<String> advice) {
    this.advice = advice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompatibilityResult compatibilityResult = (CompatibilityResult) o;
    return Objects.equals(this.characterId, compatibilityResult.characterId) &&
        Objects.equals(this.npcId, compatibilityResult.npcId) &&
        Objects.equals(this.compatibilityScore, compatibilityResult.compatibilityScore) &&
        Objects.equals(this.factors, compatibilityResult.factors) &&
        Objects.equals(this.recommendation, compatibilityResult.recommendation) &&
        Objects.equals(this.advice, compatibilityResult.advice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, npcId, compatibilityScore, factors, recommendation, advice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityResult {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    compatibilityScore: ").append(toIndentedString(compatibilityScore)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    recommendation: ").append(toIndentedString(recommendation)).append("\n");
    sb.append("    advice: ").append(toIndentedString(advice)).append("\n");
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

