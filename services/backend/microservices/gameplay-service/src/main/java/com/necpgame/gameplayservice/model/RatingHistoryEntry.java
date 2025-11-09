package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.MatchResult;
import com.necpgame.gameplayservice.model.RatingBonus;
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
 * RatingHistoryEntry
 */


public class RatingHistoryEntry {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private @Nullable UUID matchId;

  private Integer rating;

  private Integer delta;

  private @Nullable Integer opponentRating;

  private @Nullable MatchResult result;

  @Valid
  private List<@Valid RatingBonus> bonusesApplied = new ArrayList<>();

  public RatingHistoryEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingHistoryEntry(OffsetDateTime timestamp, Integer rating, Integer delta) {
    this.timestamp = timestamp;
    this.rating = rating;
    this.delta = delta;
  }

  public RatingHistoryEntry timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public RatingHistoryEntry matchId(@Nullable UUID matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  @Valid 
  @Schema(name = "matchId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchId")
  public @Nullable UUID getMatchId() {
    return matchId;
  }

  public void setMatchId(@Nullable UUID matchId) {
    this.matchId = matchId;
  }

  public RatingHistoryEntry rating(Integer rating) {
    this.rating = rating;
    return this;
  }

  /**
   * Get rating
   * @return rating
   */
  @NotNull 
  @Schema(name = "rating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rating")
  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

  public RatingHistoryEntry delta(Integer delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Integer getDelta() {
    return delta;
  }

  public void setDelta(Integer delta) {
    this.delta = delta;
  }

  public RatingHistoryEntry opponentRating(@Nullable Integer opponentRating) {
    this.opponentRating = opponentRating;
    return this;
  }

  /**
   * Get opponentRating
   * @return opponentRating
   */
  
  @Schema(name = "opponentRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opponentRating")
  public @Nullable Integer getOpponentRating() {
    return opponentRating;
  }

  public void setOpponentRating(@Nullable Integer opponentRating) {
    this.opponentRating = opponentRating;
  }

  public RatingHistoryEntry result(@Nullable MatchResult result) {
    this.result = result;
    return this;
  }

  /**
   * Get result
   * @return result
   */
  @Valid 
  @Schema(name = "result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result")
  public @Nullable MatchResult getResult() {
    return result;
  }

  public void setResult(@Nullable MatchResult result) {
    this.result = result;
  }

  public RatingHistoryEntry bonusesApplied(List<@Valid RatingBonus> bonusesApplied) {
    this.bonusesApplied = bonusesApplied;
    return this;
  }

  public RatingHistoryEntry addBonusesAppliedItem(RatingBonus bonusesAppliedItem) {
    if (this.bonusesApplied == null) {
      this.bonusesApplied = new ArrayList<>();
    }
    this.bonusesApplied.add(bonusesAppliedItem);
    return this;
  }

  /**
   * Get bonusesApplied
   * @return bonusesApplied
   */
  @Valid 
  @Schema(name = "bonusesApplied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonusesApplied")
  public List<@Valid RatingBonus> getBonusesApplied() {
    return bonusesApplied;
  }

  public void setBonusesApplied(List<@Valid RatingBonus> bonusesApplied) {
    this.bonusesApplied = bonusesApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingHistoryEntry ratingHistoryEntry = (RatingHistoryEntry) o;
    return Objects.equals(this.timestamp, ratingHistoryEntry.timestamp) &&
        Objects.equals(this.matchId, ratingHistoryEntry.matchId) &&
        Objects.equals(this.rating, ratingHistoryEntry.rating) &&
        Objects.equals(this.delta, ratingHistoryEntry.delta) &&
        Objects.equals(this.opponentRating, ratingHistoryEntry.opponentRating) &&
        Objects.equals(this.result, ratingHistoryEntry.result) &&
        Objects.equals(this.bonusesApplied, ratingHistoryEntry.bonusesApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, matchId, rating, delta, opponentRating, result, bonusesApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingHistoryEntry {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    opponentRating: ").append(toIndentedString(opponentRating)).append("\n");
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    bonusesApplied: ").append(toIndentedString(bonusesApplied)).append("\n");
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

