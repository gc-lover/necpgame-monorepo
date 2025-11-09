package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.TierChange;
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
 * RatingDeltaResult
 */


public class RatingDeltaResult {

  private Integer oldRating;

  private Integer newRating;

  private Integer delta;

  private @Nullable TierChange tierChange;

  private Boolean smurfTriggered = false;

  private @Nullable UUID analyticsEventId;

  public RatingDeltaResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingDeltaResult(Integer oldRating, Integer newRating, Integer delta) {
    this.oldRating = oldRating;
    this.newRating = newRating;
    this.delta = delta;
  }

  public RatingDeltaResult oldRating(Integer oldRating) {
    this.oldRating = oldRating;
    return this;
  }

  /**
   * Get oldRating
   * @return oldRating
   */
  @NotNull 
  @Schema(name = "oldRating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("oldRating")
  public Integer getOldRating() {
    return oldRating;
  }

  public void setOldRating(Integer oldRating) {
    this.oldRating = oldRating;
  }

  public RatingDeltaResult newRating(Integer newRating) {
    this.newRating = newRating;
    return this;
  }

  /**
   * Get newRating
   * @return newRating
   */
  @NotNull 
  @Schema(name = "newRating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newRating")
  public Integer getNewRating() {
    return newRating;
  }

  public void setNewRating(Integer newRating) {
    this.newRating = newRating;
  }

  public RatingDeltaResult delta(Integer delta) {
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

  public RatingDeltaResult tierChange(@Nullable TierChange tierChange) {
    this.tierChange = tierChange;
    return this;
  }

  /**
   * Get tierChange
   * @return tierChange
   */
  @Valid 
  @Schema(name = "tierChange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tierChange")
  public @Nullable TierChange getTierChange() {
    return tierChange;
  }

  public void setTierChange(@Nullable TierChange tierChange) {
    this.tierChange = tierChange;
  }

  public RatingDeltaResult smurfTriggered(Boolean smurfTriggered) {
    this.smurfTriggered = smurfTriggered;
    return this;
  }

  /**
   * Get smurfTriggered
   * @return smurfTriggered
   */
  
  @Schema(name = "smurfTriggered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("smurfTriggered")
  public Boolean getSmurfTriggered() {
    return smurfTriggered;
  }

  public void setSmurfTriggered(Boolean smurfTriggered) {
    this.smurfTriggered = smurfTriggered;
  }

  public RatingDeltaResult analyticsEventId(@Nullable UUID analyticsEventId) {
    this.analyticsEventId = analyticsEventId;
    return this;
  }

  /**
   * Get analyticsEventId
   * @return analyticsEventId
   */
  @Valid 
  @Schema(name = "analyticsEventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("analyticsEventId")
  public @Nullable UUID getAnalyticsEventId() {
    return analyticsEventId;
  }

  public void setAnalyticsEventId(@Nullable UUID analyticsEventId) {
    this.analyticsEventId = analyticsEventId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingDeltaResult ratingDeltaResult = (RatingDeltaResult) o;
    return Objects.equals(this.oldRating, ratingDeltaResult.oldRating) &&
        Objects.equals(this.newRating, ratingDeltaResult.newRating) &&
        Objects.equals(this.delta, ratingDeltaResult.delta) &&
        Objects.equals(this.tierChange, ratingDeltaResult.tierChange) &&
        Objects.equals(this.smurfTriggered, ratingDeltaResult.smurfTriggered) &&
        Objects.equals(this.analyticsEventId, ratingDeltaResult.analyticsEventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oldRating, newRating, delta, tierChange, smurfTriggered, analyticsEventId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingDeltaResult {\n");
    sb.append("    oldRating: ").append(toIndentedString(oldRating)).append("\n");
    sb.append("    newRating: ").append(toIndentedString(newRating)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    tierChange: ").append(toIndentedString(tierChange)).append("\n");
    sb.append("    smurfTriggered: ").append(toIndentedString(smurfTriggered)).append("\n");
    sb.append("    analyticsEventId: ").append(toIndentedString(analyticsEventId)).append("\n");
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

