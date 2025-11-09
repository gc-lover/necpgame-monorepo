package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RatingMetrics;
import com.necpgame.socialservice.model.RatingSeasonStats;
import com.necpgame.socialservice.model.RatingTrend;
import com.necpgame.socialservice.model.RatingWarning;
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
 * PlayerOrderRating
 */


public class PlayerOrderRating {

  private UUID playerId;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  private Float score;

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

  private Float decayApplied;

  private @Nullable RatingTrend trend;

  private @Nullable RatingMetrics metrics;

  @Valid
  private List<@Valid RatingWarning> warnings = new ArrayList<>();

  private @Nullable RatingSeasonStats seasonStats;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public PlayerOrderRating() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderRating(UUID playerId, RoleEnum role, Float score, CategoryEnum category, Float decayApplied, OffsetDateTime updatedAt) {
    this.playerId = playerId;
    this.role = role;
    this.score = score;
    this.category = category;
    this.decayApplied = decayApplied;
    this.updatedAt = updatedAt;
  }

  public PlayerOrderRating playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public PlayerOrderRating role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public PlayerOrderRating score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * minimum: 0
   * maximum: 100
   * @return score
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "score", example = "78.4", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public PlayerOrderRating category(CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull 
  @Schema(name = "category", example = "gold", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(CategoryEnum category) {
    this.category = category;
  }

  public PlayerOrderRating decayApplied(Float decayApplied) {
    this.decayApplied = decayApplied;
    return this;
  }

  /**
   * Процентное снижение рейтинга из-за неактивности.
   * minimum: 0
   * maximum: 100
   * @return decayApplied
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "decayApplied", example = "6.2", description = "Процентное снижение рейтинга из-за неактивности.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("decayApplied")
  public Float getDecayApplied() {
    return decayApplied;
  }

  public void setDecayApplied(Float decayApplied) {
    this.decayApplied = decayApplied;
  }

  public PlayerOrderRating trend(@Nullable RatingTrend trend) {
    this.trend = trend;
    return this;
  }

  /**
   * Get trend
   * @return trend
   */
  @Valid 
  @Schema(name = "trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trend")
  public @Nullable RatingTrend getTrend() {
    return trend;
  }

  public void setTrend(@Nullable RatingTrend trend) {
    this.trend = trend;
  }

  public PlayerOrderRating metrics(@Nullable RatingMetrics metrics) {
    this.metrics = metrics;
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public @Nullable RatingMetrics getMetrics() {
    return metrics;
  }

  public void setMetrics(@Nullable RatingMetrics metrics) {
    this.metrics = metrics;
  }

  public PlayerOrderRating warnings(List<@Valid RatingWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public PlayerOrderRating addWarningsItem(RatingWarning warningsItem) {
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
  @Valid 
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid RatingWarning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid RatingWarning> warnings) {
    this.warnings = warnings;
  }

  public PlayerOrderRating seasonStats(@Nullable RatingSeasonStats seasonStats) {
    this.seasonStats = seasonStats;
    return this;
  }

  /**
   * Get seasonStats
   * @return seasonStats
   */
  @Valid 
  @Schema(name = "seasonStats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonStats")
  public @Nullable RatingSeasonStats getSeasonStats() {
    return seasonStats;
  }

  public void setSeasonStats(@Nullable RatingSeasonStats seasonStats) {
    this.seasonStats = seasonStats;
  }

  public PlayerOrderRating updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", example = "2025-11-08T15:45:21Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderRating playerOrderRating = (PlayerOrderRating) o;
    return Objects.equals(this.playerId, playerOrderRating.playerId) &&
        Objects.equals(this.role, playerOrderRating.role) &&
        Objects.equals(this.score, playerOrderRating.score) &&
        Objects.equals(this.category, playerOrderRating.category) &&
        Objects.equals(this.decayApplied, playerOrderRating.decayApplied) &&
        Objects.equals(this.trend, playerOrderRating.trend) &&
        Objects.equals(this.metrics, playerOrderRating.metrics) &&
        Objects.equals(this.warnings, playerOrderRating.warnings) &&
        Objects.equals(this.seasonStats, playerOrderRating.seasonStats) &&
        Objects.equals(this.updatedAt, playerOrderRating.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, role, score, category, decayApplied, trend, metrics, warnings, seasonStats, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderRating {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    decayApplied: ").append(toIndentedString(decayApplied)).append("\n");
    sb.append("    trend: ").append(toIndentedString(trend)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    seasonStats: ").append(toIndentedString(seasonStats)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

