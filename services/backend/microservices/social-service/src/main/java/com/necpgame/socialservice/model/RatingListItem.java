package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RatingListItem
 */


public class RatingListItem {

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

  private @Nullable Float trendDelta;

  @Valid
  private List<@Valid RatingWarning> warnings = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public RatingListItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingListItem(UUID playerId, RoleEnum role, Float score, CategoryEnum category, OffsetDateTime updatedAt) {
    this.playerId = playerId;
    this.role = role;
    this.score = score;
    this.category = category;
    this.updatedAt = updatedAt;
  }

  public RatingListItem playerId(UUID playerId) {
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

  public RatingListItem role(RoleEnum role) {
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

  public RatingListItem score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @NotNull 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public RatingListItem category(CategoryEnum category) {
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

  public RatingListItem trendDelta(@Nullable Float trendDelta) {
    this.trendDelta = trendDelta;
    return this;
  }

  /**
   * Изменение рейтинга за выбранный период.
   * @return trendDelta
   */
  
  @Schema(name = "trendDelta", description = "Изменение рейтинга за выбранный период.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trendDelta")
  public @Nullable Float getTrendDelta() {
    return trendDelta;
  }

  public void setTrendDelta(@Nullable Float trendDelta) {
    this.trendDelta = trendDelta;
  }

  public RatingListItem warnings(List<@Valid RatingWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public RatingListItem addWarningsItem(RatingWarning warningsItem) {
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

  public RatingListItem updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
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
    RatingListItem ratingListItem = (RatingListItem) o;
    return Objects.equals(this.playerId, ratingListItem.playerId) &&
        Objects.equals(this.role, ratingListItem.role) &&
        Objects.equals(this.score, ratingListItem.score) &&
        Objects.equals(this.category, ratingListItem.category) &&
        Objects.equals(this.trendDelta, ratingListItem.trendDelta) &&
        Objects.equals(this.warnings, ratingListItem.warnings) &&
        Objects.equals(this.updatedAt, ratingListItem.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, role, score, category, trendDelta, warnings, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingListItem {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    trendDelta: ").append(toIndentedString(trendDelta)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

