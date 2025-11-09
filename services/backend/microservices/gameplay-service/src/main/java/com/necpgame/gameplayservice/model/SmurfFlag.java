package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * SmurfFlag
 */


public class SmurfFlag {

  private UUID playerId;

  private Float score;

  /**
   * Gets or Sets reasons
   */
  public enum ReasonsEnum {
    HIGH_WINRATE("HIGH_WINRATE"),
    
    FAST_RATING_GROWTH("FAST_RATING_GROWTH"),
    
    NEW_ACCOUNT("NEW_ACCOUNT"),
    
    MATCH_BEHAVIOUR("MATCH_BEHAVIOUR"),
    
    REPORTS("REPORTS");

    private final String value;

    ReasonsEnum(String value) {
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
    public static ReasonsEnum fromValue(String value) {
      for (ReasonsEnum b : ReasonsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<ReasonsEnum> reasons = new ArrayList<>();

  private @Nullable Integer gamesPlayed;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime flaggedAt;

  private @Nullable Boolean placementCompleted;

  public SmurfFlag() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SmurfFlag(UUID playerId, Float score, OffsetDateTime flaggedAt) {
    this.playerId = playerId;
    this.score = score;
    this.flaggedAt = flaggedAt;
  }

  public SmurfFlag playerId(UUID playerId) {
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

  public SmurfFlag score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * minimum: 0
   * maximum: 1
   * @return score
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public SmurfFlag reasons(List<ReasonsEnum> reasons) {
    this.reasons = reasons;
    return this;
  }

  public SmurfFlag addReasonsItem(ReasonsEnum reasonsItem) {
    if (this.reasons == null) {
      this.reasons = new ArrayList<>();
    }
    this.reasons.add(reasonsItem);
    return this;
  }

  /**
   * Get reasons
   * @return reasons
   */
  
  @Schema(name = "reasons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasons")
  public List<ReasonsEnum> getReasons() {
    return reasons;
  }

  public void setReasons(List<ReasonsEnum> reasons) {
    this.reasons = reasons;
  }

  public SmurfFlag gamesPlayed(@Nullable Integer gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
    return this;
  }

  /**
   * Get gamesPlayed
   * @return gamesPlayed
   */
  
  @Schema(name = "gamesPlayed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gamesPlayed")
  public @Nullable Integer getGamesPlayed() {
    return gamesPlayed;
  }

  public void setGamesPlayed(@Nullable Integer gamesPlayed) {
    this.gamesPlayed = gamesPlayed;
  }

  public SmurfFlag flaggedAt(OffsetDateTime flaggedAt) {
    this.flaggedAt = flaggedAt;
    return this;
  }

  /**
   * Get flaggedAt
   * @return flaggedAt
   */
  @NotNull @Valid 
  @Schema(name = "flaggedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("flaggedAt")
  public OffsetDateTime getFlaggedAt() {
    return flaggedAt;
  }

  public void setFlaggedAt(OffsetDateTime flaggedAt) {
    this.flaggedAt = flaggedAt;
  }

  public SmurfFlag placementCompleted(@Nullable Boolean placementCompleted) {
    this.placementCompleted = placementCompleted;
    return this;
  }

  /**
   * Get placementCompleted
   * @return placementCompleted
   */
  
  @Schema(name = "placementCompleted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placementCompleted")
  public @Nullable Boolean getPlacementCompleted() {
    return placementCompleted;
  }

  public void setPlacementCompleted(@Nullable Boolean placementCompleted) {
    this.placementCompleted = placementCompleted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmurfFlag smurfFlag = (SmurfFlag) o;
    return Objects.equals(this.playerId, smurfFlag.playerId) &&
        Objects.equals(this.score, smurfFlag.score) &&
        Objects.equals(this.reasons, smurfFlag.reasons) &&
        Objects.equals(this.gamesPlayed, smurfFlag.gamesPlayed) &&
        Objects.equals(this.flaggedAt, smurfFlag.flaggedAt) &&
        Objects.equals(this.placementCompleted, smurfFlag.placementCompleted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, score, reasons, gamesPlayed, flaggedAt, placementCompleted);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmurfFlag {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    reasons: ").append(toIndentedString(reasons)).append("\n");
    sb.append("    gamesPlayed: ").append(toIndentedString(gamesPlayed)).append("\n");
    sb.append("    flaggedAt: ").append(toIndentedString(flaggedAt)).append("\n");
    sb.append("    placementCompleted: ").append(toIndentedString(placementCompleted)).append("\n");
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

