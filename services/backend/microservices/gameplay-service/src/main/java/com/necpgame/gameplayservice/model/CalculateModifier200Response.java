package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CalculateModifier200Response
 */

@JsonTypeName("calculateModifier_200_response")

public class CalculateModifier200Response {

  private @Nullable Integer attributeValue;

  private @Nullable Integer attributeModifier;

  /**
   * Gets or Sets skillRank
   */
  public enum SkillRankEnum {
    NOVICE("novice"),
    
    COMPETENT("competent"),
    
    PROFICIENT("proficient"),
    
    EXPERT("expert"),
    
    LEGENDARY("legendary");

    private final String value;

    SkillRankEnum(String value) {
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
    public static SkillRankEnum fromValue(String value) {
      for (SkillRankEnum b : SkillRankEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SkillRankEnum skillRank;

  private @Nullable Integer skillBonus;

  private @Nullable Integer totalModifier;

  public CalculateModifier200Response attributeValue(@Nullable Integer attributeValue) {
    this.attributeValue = attributeValue;
    return this;
  }

  /**
   * Get attributeValue
   * @return attributeValue
   */
  
  @Schema(name = "attribute_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_value")
  public @Nullable Integer getAttributeValue() {
    return attributeValue;
  }

  public void setAttributeValue(@Nullable Integer attributeValue) {
    this.attributeValue = attributeValue;
  }

  public CalculateModifier200Response attributeModifier(@Nullable Integer attributeModifier) {
    this.attributeModifier = attributeModifier;
    return this;
  }

  /**
   * Get attributeModifier
   * @return attributeModifier
   */
  
  @Schema(name = "attribute_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_modifier")
  public @Nullable Integer getAttributeModifier() {
    return attributeModifier;
  }

  public void setAttributeModifier(@Nullable Integer attributeModifier) {
    this.attributeModifier = attributeModifier;
  }

  public CalculateModifier200Response skillRank(@Nullable SkillRankEnum skillRank) {
    this.skillRank = skillRank;
    return this;
  }

  /**
   * Get skillRank
   * @return skillRank
   */
  
  @Schema(name = "skill_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_rank")
  public @Nullable SkillRankEnum getSkillRank() {
    return skillRank;
  }

  public void setSkillRank(@Nullable SkillRankEnum skillRank) {
    this.skillRank = skillRank;
  }

  public CalculateModifier200Response skillBonus(@Nullable Integer skillBonus) {
    this.skillBonus = skillBonus;
    return this;
  }

  /**
   * Get skillBonus
   * @return skillBonus
   */
  
  @Schema(name = "skill_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_bonus")
  public @Nullable Integer getSkillBonus() {
    return skillBonus;
  }

  public void setSkillBonus(@Nullable Integer skillBonus) {
    this.skillBonus = skillBonus;
  }

  public CalculateModifier200Response totalModifier(@Nullable Integer totalModifier) {
    this.totalModifier = totalModifier;
    return this;
  }

  /**
   * Get totalModifier
   * @return totalModifier
   */
  
  @Schema(name = "total_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_modifier")
  public @Nullable Integer getTotalModifier() {
    return totalModifier;
  }

  public void setTotalModifier(@Nullable Integer totalModifier) {
    this.totalModifier = totalModifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateModifier200Response calculateModifier200Response = (CalculateModifier200Response) o;
    return Objects.equals(this.attributeValue, calculateModifier200Response.attributeValue) &&
        Objects.equals(this.attributeModifier, calculateModifier200Response.attributeModifier) &&
        Objects.equals(this.skillRank, calculateModifier200Response.skillRank) &&
        Objects.equals(this.skillBonus, calculateModifier200Response.skillBonus) &&
        Objects.equals(this.totalModifier, calculateModifier200Response.totalModifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeValue, attributeModifier, skillRank, skillBonus, totalModifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateModifier200Response {\n");
    sb.append("    attributeValue: ").append(toIndentedString(attributeValue)).append("\n");
    sb.append("    attributeModifier: ").append(toIndentedString(attributeModifier)).append("\n");
    sb.append("    skillRank: ").append(toIndentedString(skillRank)).append("\n");
    sb.append("    skillBonus: ").append(toIndentedString(skillBonus)).append("\n");
    sb.append("    totalModifier: ").append(toIndentedString(totalModifier)).append("\n");
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

