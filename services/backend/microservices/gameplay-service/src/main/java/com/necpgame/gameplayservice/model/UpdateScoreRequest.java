package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateScoreRequest
 */


public class UpdateScoreRequest {

  private UUID playerId;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    LEVEL("LEVEL"),
    
    WEALTH("WEALTH"),
    
    PVP_RATING("PVP_RATING"),
    
    ACHIEVEMENTS("ACHIEVEMENTS"),
    
    COMBAT_KILLS("COMBAT_KILLS"),
    
    RAID_CLEARS("RAID_CLEARS");

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

  private BigDecimal score;

  private JsonNullable<String> seasonId = JsonNullable.<String>undefined();

  public UpdateScoreRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateScoreRequest(UUID playerId, CategoryEnum category, BigDecimal score) {
    this.playerId = playerId;
    this.category = category;
    this.score = score;
  }

  public UpdateScoreRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public UpdateScoreRequest category(CategoryEnum category) {
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

  public UpdateScoreRequest score(BigDecimal score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  @NotNull @Valid 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public BigDecimal getScore() {
    return score;
  }

  public void setScore(BigDecimal score) {
    this.score = score;
  }

  public UpdateScoreRequest seasonId(String seasonId) {
    this.seasonId = JsonNullable.of(seasonId);
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  
  @Schema(name = "season_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("season_id")
  public JsonNullable<String> getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(JsonNullable<String> seasonId) {
    this.seasonId = seasonId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateScoreRequest updateScoreRequest = (UpdateScoreRequest) o;
    return Objects.equals(this.playerId, updateScoreRequest.playerId) &&
        Objects.equals(this.category, updateScoreRequest.category) &&
        Objects.equals(this.score, updateScoreRequest.score) &&
        equalsNullable(this.seasonId, updateScoreRequest.seasonId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, category, score, hashCodeNullable(seasonId));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateScoreRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
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

