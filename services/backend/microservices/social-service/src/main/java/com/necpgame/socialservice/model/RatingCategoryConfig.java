package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RatingCategoryBenefits;
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
 * RatingCategoryConfig
 */


public class RatingCategoryConfig {

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    BRONZE("bronze"),
    
    SILVER("silver"),
    
    GOLD("gold"),
    
    PLATINUM("platinum");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CategoryEnum category;

  private Float minScore;

  private Float maxScore;

  private @Nullable Float decayRate;

  private @Nullable Integer boostThreshold;

  private @Nullable RatingCategoryBenefits benefits;

  @Valid
  private List<String> warnings = new ArrayList<>();

  public RatingCategoryConfig() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingCategoryConfig(CategoryEnum category, Float minScore, Float maxScore) {
    this.category = category;
    this.minScore = minScore;
    this.maxScore = maxScore;
  }

  public RatingCategoryConfig category(CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(CategoryEnum category) {
    this.category = category;
  }

  public RatingCategoryConfig minScore(Float minScore) {
    this.minScore = minScore;
    return this;
  }

  /**
   * Get minScore
   * @return minScore
   */
  @NotNull 
  @Schema(name = "minScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("minScore")
  public Float getMinScore() {
    return minScore;
  }

  public void setMinScore(Float minScore) {
    this.minScore = minScore;
  }

  public RatingCategoryConfig maxScore(Float maxScore) {
    this.maxScore = maxScore;
    return this;
  }

  /**
   * Get maxScore
   * @return maxScore
   */
  @NotNull 
  @Schema(name = "maxScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxScore")
  public Float getMaxScore() {
    return maxScore;
  }

  public void setMaxScore(Float maxScore) {
    this.maxScore = maxScore;
  }

  public RatingCategoryConfig decayRate(@Nullable Float decayRate) {
    this.decayRate = decayRate;
    return this;
  }

  /**
   * Ежедневное снижение рейтинга без активности.
   * @return decayRate
   */
  
  @Schema(name = "decayRate", description = "Ежедневное снижение рейтинга без активности.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decayRate")
  public @Nullable Float getDecayRate() {
    return decayRate;
  }

  public void setDecayRate(@Nullable Float decayRate) {
    this.decayRate = decayRate;
  }

  public RatingCategoryConfig boostThreshold(@Nullable Integer boostThreshold) {
    this.boostThreshold = boostThreshold;
    return this;
  }

  /**
   * Количество успешных заказов до бонуса.
   * @return boostThreshold
   */
  
  @Schema(name = "boostThreshold", description = "Количество успешных заказов до бонуса.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostThreshold")
  public @Nullable Integer getBoostThreshold() {
    return boostThreshold;
  }

  public void setBoostThreshold(@Nullable Integer boostThreshold) {
    this.boostThreshold = boostThreshold;
  }

  public RatingCategoryConfig benefits(@Nullable RatingCategoryBenefits benefits) {
    this.benefits = benefits;
    return this;
  }

  /**
   * Get benefits
   * @return benefits
   */
  @Valid 
  @Schema(name = "benefits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("benefits")
  public @Nullable RatingCategoryBenefits getBenefits() {
    return benefits;
  }

  public void setBenefits(@Nullable RatingCategoryBenefits benefits) {
    this.benefits = benefits;
  }

  public RatingCategoryConfig warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public RatingCategoryConfig addWarningsItem(String warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingCategoryConfig ratingCategoryConfig = (RatingCategoryConfig) o;
    return Objects.equals(this.category, ratingCategoryConfig.category) &&
        Objects.equals(this.minScore, ratingCategoryConfig.minScore) &&
        Objects.equals(this.maxScore, ratingCategoryConfig.maxScore) &&
        Objects.equals(this.decayRate, ratingCategoryConfig.decayRate) &&
        Objects.equals(this.boostThreshold, ratingCategoryConfig.boostThreshold) &&
        Objects.equals(this.benefits, ratingCategoryConfig.benefits) &&
        Objects.equals(this.warnings, ratingCategoryConfig.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, minScore, maxScore, decayRate, boostThreshold, benefits, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingCategoryConfig {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    minScore: ").append(toIndentedString(minScore)).append("\n");
    sb.append("    maxScore: ").append(toIndentedString(maxScore)).append("\n");
    sb.append("    decayRate: ").append(toIndentedString(decayRate)).append("\n");
    sb.append("    boostThreshold: ").append(toIndentedString(boostThreshold)).append("\n");
    sb.append("    benefits: ").append(toIndentedString(benefits)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

