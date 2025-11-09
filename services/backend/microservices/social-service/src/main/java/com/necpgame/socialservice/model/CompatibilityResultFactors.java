package com.necpgame.socialservice.model;

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
 * CompatibilityResultFactors
 */

@JsonTypeName("CompatibilityResult_factors")

public class CompatibilityResultFactors {

  private @Nullable Integer personalityMatch;

  private @Nullable Integer interestOverlap;

  private @Nullable Integer attributeAttraction;

  private @Nullable Integer factionCompatibility;

  public CompatibilityResultFactors personalityMatch(@Nullable Integer personalityMatch) {
    this.personalityMatch = personalityMatch;
    return this;
  }

  /**
   * Get personalityMatch
   * @return personalityMatch
   */
  
  @Schema(name = "personality_match", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personality_match")
  public @Nullable Integer getPersonalityMatch() {
    return personalityMatch;
  }

  public void setPersonalityMatch(@Nullable Integer personalityMatch) {
    this.personalityMatch = personalityMatch;
  }

  public CompatibilityResultFactors interestOverlap(@Nullable Integer interestOverlap) {
    this.interestOverlap = interestOverlap;
    return this;
  }

  /**
   * Get interestOverlap
   * @return interestOverlap
   */
  
  @Schema(name = "interest_overlap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interest_overlap")
  public @Nullable Integer getInterestOverlap() {
    return interestOverlap;
  }

  public void setInterestOverlap(@Nullable Integer interestOverlap) {
    this.interestOverlap = interestOverlap;
  }

  public CompatibilityResultFactors attributeAttraction(@Nullable Integer attributeAttraction) {
    this.attributeAttraction = attributeAttraction;
    return this;
  }

  /**
   * Get attributeAttraction
   * @return attributeAttraction
   */
  
  @Schema(name = "attribute_attraction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_attraction")
  public @Nullable Integer getAttributeAttraction() {
    return attributeAttraction;
  }

  public void setAttributeAttraction(@Nullable Integer attributeAttraction) {
    this.attributeAttraction = attributeAttraction;
  }

  public CompatibilityResultFactors factionCompatibility(@Nullable Integer factionCompatibility) {
    this.factionCompatibility = factionCompatibility;
    return this;
  }

  /**
   * Get factionCompatibility
   * @return factionCompatibility
   */
  
  @Schema(name = "faction_compatibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_compatibility")
  public @Nullable Integer getFactionCompatibility() {
    return factionCompatibility;
  }

  public void setFactionCompatibility(@Nullable Integer factionCompatibility) {
    this.factionCompatibility = factionCompatibility;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompatibilityResultFactors compatibilityResultFactors = (CompatibilityResultFactors) o;
    return Objects.equals(this.personalityMatch, compatibilityResultFactors.personalityMatch) &&
        Objects.equals(this.interestOverlap, compatibilityResultFactors.interestOverlap) &&
        Objects.equals(this.attributeAttraction, compatibilityResultFactors.attributeAttraction) &&
        Objects.equals(this.factionCompatibility, compatibilityResultFactors.factionCompatibility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personalityMatch, interestOverlap, attributeAttraction, factionCompatibility);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityResultFactors {\n");
    sb.append("    personalityMatch: ").append(toIndentedString(personalityMatch)).append("\n");
    sb.append("    interestOverlap: ").append(toIndentedString(interestOverlap)).append("\n");
    sb.append("    attributeAttraction: ").append(toIndentedString(attributeAttraction)).append("\n");
    sb.append("    factionCompatibility: ").append(toIndentedString(factionCompatibility)).append("\n");
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

