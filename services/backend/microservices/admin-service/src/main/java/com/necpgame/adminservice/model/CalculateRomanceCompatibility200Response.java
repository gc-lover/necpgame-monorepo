package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CalculateRomanceCompatibility200Response
 */

@JsonTypeName("calculateRomanceCompatibility_200_response")

public class CalculateRomanceCompatibility200Response {

  private @Nullable Integer compatibilityScore;

  private @Nullable Object factors;

  private @Nullable String recommendation;

  public CalculateRomanceCompatibility200Response compatibilityScore(@Nullable Integer compatibilityScore) {
    this.compatibilityScore = compatibilityScore;
    return this;
  }

  /**
   * Get compatibilityScore
   * @return compatibilityScore
   */
  
  @Schema(name = "compatibility_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatibility_score")
  public @Nullable Integer getCompatibilityScore() {
    return compatibilityScore;
  }

  public void setCompatibilityScore(@Nullable Integer compatibilityScore) {
    this.compatibilityScore = compatibilityScore;
  }

  public CalculateRomanceCompatibility200Response factors(@Nullable Object factors) {
    this.factors = factors;
    return this;
  }

  /**
   * Get factors
   * @return factors
   */
  
  @Schema(name = "factors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factors")
  public @Nullable Object getFactors() {
    return factors;
  }

  public void setFactors(@Nullable Object factors) {
    this.factors = factors;
  }

  public CalculateRomanceCompatibility200Response recommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
    return this;
  }

  /**
   * Get recommendation
   * @return recommendation
   */
  
  @Schema(name = "recommendation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendation")
  public @Nullable String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateRomanceCompatibility200Response calculateRomanceCompatibility200Response = (CalculateRomanceCompatibility200Response) o;
    return Objects.equals(this.compatibilityScore, calculateRomanceCompatibility200Response.compatibilityScore) &&
        Objects.equals(this.factors, calculateRomanceCompatibility200Response.factors) &&
        Objects.equals(this.recommendation, calculateRomanceCompatibility200Response.recommendation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(compatibilityScore, factors, recommendation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateRomanceCompatibility200Response {\n");
    sb.append("    compatibilityScore: ").append(toIndentedString(compatibilityScore)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    recommendation: ").append(toIndentedString(recommendation)).append("\n");
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

