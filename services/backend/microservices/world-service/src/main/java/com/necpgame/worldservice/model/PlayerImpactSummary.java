package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * PlayerImpactSummary
 */


public class PlayerImpactSummary {

  private @Nullable String playerGroupId;

  private @Nullable String factionId;

  private @Nullable BigDecimal influenceDelta;

  @Valid
  private List<String> activities = new ArrayList<>();

  public PlayerImpactSummary playerGroupId(@Nullable String playerGroupId) {
    this.playerGroupId = playerGroupId;
    return this;
  }

  /**
   * Get playerGroupId
   * @return playerGroupId
   */
  
  @Schema(name = "playerGroupId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerGroupId")
  public @Nullable String getPlayerGroupId() {
    return playerGroupId;
  }

  public void setPlayerGroupId(@Nullable String playerGroupId) {
    this.playerGroupId = playerGroupId;
  }

  public PlayerImpactSummary factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionId")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public PlayerImpactSummary influenceDelta(@Nullable BigDecimal influenceDelta) {
    this.influenceDelta = influenceDelta;
    return this;
  }

  /**
   * Get influenceDelta
   * @return influenceDelta
   */
  @Valid 
  @Schema(name = "influenceDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("influenceDelta")
  public @Nullable BigDecimal getInfluenceDelta() {
    return influenceDelta;
  }

  public void setInfluenceDelta(@Nullable BigDecimal influenceDelta) {
    this.influenceDelta = influenceDelta;
  }

  public PlayerImpactSummary activities(List<String> activities) {
    this.activities = activities;
    return this;
  }

  public PlayerImpactSummary addActivitiesItem(String activitiesItem) {
    if (this.activities == null) {
      this.activities = new ArrayList<>();
    }
    this.activities.add(activitiesItem);
    return this;
  }

  /**
   * Get activities
   * @return activities
   */
  
  @Schema(name = "activities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activities")
  public List<String> getActivities() {
    return activities;
  }

  public void setActivities(List<String> activities) {
    this.activities = activities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerImpactSummary playerImpactSummary = (PlayerImpactSummary) o;
    return Objects.equals(this.playerGroupId, playerImpactSummary.playerGroupId) &&
        Objects.equals(this.factionId, playerImpactSummary.factionId) &&
        Objects.equals(this.influenceDelta, playerImpactSummary.influenceDelta) &&
        Objects.equals(this.activities, playerImpactSummary.activities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerGroupId, factionId, influenceDelta, activities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerImpactSummary {\n");
    sb.append("    playerGroupId: ").append(toIndentedString(playerGroupId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    influenceDelta: ").append(toIndentedString(influenceDelta)).append("\n");
    sb.append("    activities: ").append(toIndentedString(activities)).append("\n");
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

