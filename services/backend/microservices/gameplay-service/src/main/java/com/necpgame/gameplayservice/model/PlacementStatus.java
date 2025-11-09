package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Tier;
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
 * PlacementStatus
 */


public class PlacementStatus {

  private UUID playerId;

  private Boolean placementCompleted;

  private @Nullable Integer gamesRequired;

  private @Nullable Integer gamesPlayed;

  private @Nullable Integer provisionalRating;

  private @Nullable Tier recommendedTier;

  public PlacementStatus() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlacementStatus(UUID playerId, Boolean placementCompleted) {
    this.playerId = playerId;
    this.placementCompleted = placementCompleted;
  }

  public PlacementStatus playerId(UUID playerId) {
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

  public PlacementStatus placementCompleted(Boolean placementCompleted) {
    this.placementCompleted = placementCompleted;
    return this;
  }

  /**
   * Get placementCompleted
   * @return placementCompleted
   */
  @NotNull 
  @Schema(name = "placementCompleted", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("placementCompleted")
  public Boolean getPlacementCompleted() {
    return placementCompleted;
  }

  public void setPlacementCompleted(Boolean placementCompleted) {
    this.placementCompleted = placementCompleted;
  }

  public PlacementStatus gamesRequired(@Nullable Integer gamesRequired) {
    this.gamesRequired = gamesRequired;
    return this;
  }

  /**
   * Get gamesRequired
   * @return gamesRequired
   */
  
  @Schema(name = "gamesRequired", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gamesRequired")
  public @Nullable Integer getGamesRequired() {
    return gamesRequired;
  }

  public void setGamesRequired(@Nullable Integer gamesRequired) {
    this.gamesRequired = gamesRequired;
  }

  public PlacementStatus gamesPlayed(@Nullable Integer gamesPlayed) {
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

  public PlacementStatus provisionalRating(@Nullable Integer provisionalRating) {
    this.provisionalRating = provisionalRating;
    return this;
  }

  /**
   * Get provisionalRating
   * @return provisionalRating
   */
  
  @Schema(name = "provisionalRating", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("provisionalRating")
  public @Nullable Integer getProvisionalRating() {
    return provisionalRating;
  }

  public void setProvisionalRating(@Nullable Integer provisionalRating) {
    this.provisionalRating = provisionalRating;
  }

  public PlacementStatus recommendedTier(@Nullable Tier recommendedTier) {
    this.recommendedTier = recommendedTier;
    return this;
  }

  /**
   * Get recommendedTier
   * @return recommendedTier
   */
  @Valid 
  @Schema(name = "recommendedTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedTier")
  public @Nullable Tier getRecommendedTier() {
    return recommendedTier;
  }

  public void setRecommendedTier(@Nullable Tier recommendedTier) {
    this.recommendedTier = recommendedTier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementStatus placementStatus = (PlacementStatus) o;
    return Objects.equals(this.playerId, placementStatus.playerId) &&
        Objects.equals(this.placementCompleted, placementStatus.placementCompleted) &&
        Objects.equals(this.gamesRequired, placementStatus.gamesRequired) &&
        Objects.equals(this.gamesPlayed, placementStatus.gamesPlayed) &&
        Objects.equals(this.provisionalRating, placementStatus.provisionalRating) &&
        Objects.equals(this.recommendedTier, placementStatus.recommendedTier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, placementCompleted, gamesRequired, gamesPlayed, provisionalRating, recommendedTier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlacementStatus {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    placementCompleted: ").append(toIndentedString(placementCompleted)).append("\n");
    sb.append("    gamesRequired: ").append(toIndentedString(gamesRequired)).append("\n");
    sb.append("    gamesPlayed: ").append(toIndentedString(gamesPlayed)).append("\n");
    sb.append("    provisionalRating: ").append(toIndentedString(provisionalRating)).append("\n");
    sb.append("    recommendedTier: ").append(toIndentedString(recommendedTier)).append("\n");
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

