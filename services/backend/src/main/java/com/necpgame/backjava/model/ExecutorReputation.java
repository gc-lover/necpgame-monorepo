package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonTypeName("ExecutorReputation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ExecutorReputation {

    @Schema(name = "character_id")
    @JsonProperty("character_id")
    private UUID characterId;

    @Schema(name = "reputation_score")
    @JsonProperty("reputation_score")
    private Integer reputationScore;

    @Schema(name = "tier")
    @JsonProperty("tier")
    private String tier;

    @Schema(name = "orders_completed")
    @JsonProperty("orders_completed")
    private Integer ordersCompleted;

    @Schema(name = "success_rate")
    @JsonProperty("success_rate")
    private Double successRate;

    @Schema(name = "average_rating")
    @JsonProperty("average_rating")
    private Double averageRating;

    @Schema(name = "total_earned")
    @JsonProperty("total_earned")
    private Integer totalEarned;

    @Schema(name = "specializations")
    @JsonProperty("specializations")
    private List<String> specializations;

    @Valid
    @Schema(name = "reviews")
    @JsonProperty("reviews")
    private List<PlayerOrderDetailedAllOfReviews> reviews;

    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    public Integer getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(Integer reputationScore) {
        this.reputationScore = reputationScore;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Integer getOrdersCompleted() {
        return ordersCompleted;
    }

    public void setOrdersCompleted(Integer ordersCompleted) {
        this.ordersCompleted = ordersCompleted;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalEarned() {
        return totalEarned;
    }

    public void setTotalEarned(Integer totalEarned) {
        this.totalEarned = totalEarned;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public List<PlayerOrderDetailedAllOfReviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<PlayerOrderDetailedAllOfReviews> reviews) {
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
        ExecutorReputation that = (ExecutorReputation) o;
        return Objects.equals(characterId, that.characterId)
            && Objects.equals(reputationScore, that.reputationScore)
            && Objects.equals(tier, that.tier)
            && Objects.equals(ordersCompleted, that.ordersCompleted)
            && Objects.equals(successRate, that.successRate)
            && Objects.equals(averageRating, that.averageRating)
            && Objects.equals(totalEarned, that.totalEarned)
            && Objects.equals(specializations, that.specializations)
            && Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterId, reputationScore, tier, ordersCompleted, successRate, averageRating,
            totalEarned, specializations, reviews);
    }

    @Override
    public String toString() {
        return "ExecutorReputation{" +
            "characterId=" + characterId +
            ", reputationScore=" + reputationScore +
            ", tier='" + tier + '\'' +
            ", ordersCompleted=" + ordersCompleted +
            ", successRate=" + successRate +
            ", averageRating=" + averageRating +
            ", totalEarned=" + totalEarned +
            ", specializations=" + specializations +
            ", reviews=" + reviews +
            '}';
    }
}


