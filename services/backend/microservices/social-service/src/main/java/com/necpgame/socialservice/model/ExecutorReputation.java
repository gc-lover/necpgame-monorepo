package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * ExecutorReputation
 */


public class ExecutorReputation {

  private @Nullable UUID characterId;

  private @Nullable Integer reputationScore;

  /**
   * Gets or Sets tier
   */
  public enum TierEnum {
    NOVICE("NOVICE"),
    
    COMPETENT("COMPETENT"),
    
    EXPERT("EXPERT"),
    
    MASTER("MASTER"),
    
    LEGENDARY("LEGENDARY");

    private final String value;

    TierEnum(String value) {
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
    public static TierEnum fromValue(String value) {
      for (TierEnum b : TierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TierEnum tier;

  private @Nullable Integer ordersCompleted;

  private @Nullable Float successRate;

  private @Nullable Float averageRating;

  private @Nullable Integer totalEarned;

  @Valid
  private List<String> specializations = new ArrayList<>();

  @Valid
  private List<Object> reviews = new ArrayList<>();

  public ExecutorReputation characterId(@Nullable UUID characterId) {
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

  public ExecutorReputation reputationScore(@Nullable Integer reputationScore) {
    this.reputationScore = reputationScore;
    return this;
  }

  /**
   * Get reputationScore
   * @return reputationScore
   */
  
  @Schema(name = "reputation_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_score")
  public @Nullable Integer getReputationScore() {
    return reputationScore;
  }

  public void setReputationScore(@Nullable Integer reputationScore) {
    this.reputationScore = reputationScore;
  }

  public ExecutorReputation tier(@Nullable TierEnum tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tier")
  public @Nullable TierEnum getTier() {
    return tier;
  }

  public void setTier(@Nullable TierEnum tier) {
    this.tier = tier;
  }

  public ExecutorReputation ordersCompleted(@Nullable Integer ordersCompleted) {
    this.ordersCompleted = ordersCompleted;
    return this;
  }

  /**
   * Get ordersCompleted
   * @return ordersCompleted
   */
  
  @Schema(name = "orders_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orders_completed")
  public @Nullable Integer getOrdersCompleted() {
    return ordersCompleted;
  }

  public void setOrdersCompleted(@Nullable Integer ordersCompleted) {
    this.ordersCompleted = ordersCompleted;
  }

  public ExecutorReputation successRate(@Nullable Float successRate) {
    this.successRate = successRate;
    return this;
  }

  /**
   * Get successRate
   * @return successRate
   */
  
  @Schema(name = "success_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success_rate")
  public @Nullable Float getSuccessRate() {
    return successRate;
  }

  public void setSuccessRate(@Nullable Float successRate) {
    this.successRate = successRate;
  }

  public ExecutorReputation averageRating(@Nullable Float averageRating) {
    this.averageRating = averageRating;
    return this;
  }

  /**
   * Get averageRating
   * @return averageRating
   */
  
  @Schema(name = "average_rating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_rating")
  public @Nullable Float getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(@Nullable Float averageRating) {
    this.averageRating = averageRating;
  }

  public ExecutorReputation totalEarned(@Nullable Integer totalEarned) {
    this.totalEarned = totalEarned;
    return this;
  }

  /**
   * Get totalEarned
   * @return totalEarned
   */
  
  @Schema(name = "total_earned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_earned")
  public @Nullable Integer getTotalEarned() {
    return totalEarned;
  }

  public void setTotalEarned(@Nullable Integer totalEarned) {
    this.totalEarned = totalEarned;
  }

  public ExecutorReputation specializations(List<String> specializations) {
    this.specializations = specializations;
    return this;
  }

  public ExecutorReputation addSpecializationsItem(String specializationsItem) {
    if (this.specializations == null) {
      this.specializations = new ArrayList<>();
    }
    this.specializations.add(specializationsItem);
    return this;
  }

  /**
   * Get specializations
   * @return specializations
   */
  
  @Schema(name = "specializations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specializations")
  public List<String> getSpecializations() {
    return specializations;
  }

  public void setSpecializations(List<String> specializations) {
    this.specializations = specializations;
  }

  public ExecutorReputation reviews(List<Object> reviews) {
    this.reviews = reviews;
    return this;
  }

  public ExecutorReputation addReviewsItem(Object reviewsItem) {
    if (this.reviews == null) {
      this.reviews = new ArrayList<>();
    }
    this.reviews.add(reviewsItem);
    return this;
  }

  /**
   * Get reviews
   * @return reviews
   */
  
  @Schema(name = "reviews", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reviews")
  public List<Object> getReviews() {
    return reviews;
  }

  public void setReviews(List<Object> reviews) {
    this.reviews = reviews;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecutorReputation executorReputation = (ExecutorReputation) o;
    return Objects.equals(this.characterId, executorReputation.characterId) &&
        Objects.equals(this.reputationScore, executorReputation.reputationScore) &&
        Objects.equals(this.tier, executorReputation.tier) &&
        Objects.equals(this.ordersCompleted, executorReputation.ordersCompleted) &&
        Objects.equals(this.successRate, executorReputation.successRate) &&
        Objects.equals(this.averageRating, executorReputation.averageRating) &&
        Objects.equals(this.totalEarned, executorReputation.totalEarned) &&
        Objects.equals(this.specializations, executorReputation.specializations) &&
        Objects.equals(this.reviews, executorReputation.reviews);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, reputationScore, tier, ordersCompleted, successRate, averageRating, totalEarned, specializations, reviews);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecutorReputation {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    reputationScore: ").append(toIndentedString(reputationScore)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    ordersCompleted: ").append(toIndentedString(ordersCompleted)).append("\n");
    sb.append("    successRate: ").append(toIndentedString(successRate)).append("\n");
    sb.append("    averageRating: ").append(toIndentedString(averageRating)).append("\n");
    sb.append("    totalEarned: ").append(toIndentedString(totalEarned)).append("\n");
    sb.append("    specializations: ").append(toIndentedString(specializations)).append("\n");
    sb.append("    reviews: ").append(toIndentedString(reviews)).append("\n");
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

