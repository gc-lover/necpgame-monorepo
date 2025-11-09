package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.XpGrantResponseLevelUpsInner;
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
 * XpGrantResponse
 */


public class XpGrantResponse {

  private @Nullable String seasonId;

  private @Nullable String playerId;

  private @Nullable Integer xpAdded;

  private @Nullable Integer currentLevel;

  private @Nullable Integer currentXP;

  private @Nullable Integer totalXPEarned;

  @Valid
  private List<@Valid XpGrantResponseLevelUpsInner> levelUps = new ArrayList<>();

  public XpGrantResponse seasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
    return this;
  }

  /**
   * Get seasonId
   * @return seasonId
   */
  
  @Schema(name = "seasonId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonId")
  public @Nullable String getSeasonId() {
    return seasonId;
  }

  public void setSeasonId(@Nullable String seasonId) {
    this.seasonId = seasonId;
  }

  public XpGrantResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public XpGrantResponse xpAdded(@Nullable Integer xpAdded) {
    this.xpAdded = xpAdded;
    return this;
  }

  /**
   * Get xpAdded
   * @return xpAdded
   */
  
  @Schema(name = "xpAdded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpAdded")
  public @Nullable Integer getXpAdded() {
    return xpAdded;
  }

  public void setXpAdded(@Nullable Integer xpAdded) {
    this.xpAdded = xpAdded;
  }

  public XpGrantResponse currentLevel(@Nullable Integer currentLevel) {
    this.currentLevel = currentLevel;
    return this;
  }

  /**
   * Get currentLevel
   * @return currentLevel
   */
  
  @Schema(name = "currentLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentLevel")
  public @Nullable Integer getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(@Nullable Integer currentLevel) {
    this.currentLevel = currentLevel;
  }

  public XpGrantResponse currentXP(@Nullable Integer currentXP) {
    this.currentXP = currentXP;
    return this;
  }

  /**
   * Get currentXP
   * @return currentXP
   */
  
  @Schema(name = "currentXP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentXP")
  public @Nullable Integer getCurrentXP() {
    return currentXP;
  }

  public void setCurrentXP(@Nullable Integer currentXP) {
    this.currentXP = currentXP;
  }

  public XpGrantResponse totalXPEarned(@Nullable Integer totalXPEarned) {
    this.totalXPEarned = totalXPEarned;
    return this;
  }

  /**
   * Get totalXPEarned
   * @return totalXPEarned
   */
  
  @Schema(name = "totalXPEarned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalXPEarned")
  public @Nullable Integer getTotalXPEarned() {
    return totalXPEarned;
  }

  public void setTotalXPEarned(@Nullable Integer totalXPEarned) {
    this.totalXPEarned = totalXPEarned;
  }

  public XpGrantResponse levelUps(List<@Valid XpGrantResponseLevelUpsInner> levelUps) {
    this.levelUps = levelUps;
    return this;
  }

  public XpGrantResponse addLevelUpsItem(XpGrantResponseLevelUpsInner levelUpsItem) {
    if (this.levelUps == null) {
      this.levelUps = new ArrayList<>();
    }
    this.levelUps.add(levelUpsItem);
    return this;
  }

  /**
   * Get levelUps
   * @return levelUps
   */
  @Valid 
  @Schema(name = "levelUps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("levelUps")
  public List<@Valid XpGrantResponseLevelUpsInner> getLevelUps() {
    return levelUps;
  }

  public void setLevelUps(List<@Valid XpGrantResponseLevelUpsInner> levelUps) {
    this.levelUps = levelUps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XpGrantResponse xpGrantResponse = (XpGrantResponse) o;
    return Objects.equals(this.seasonId, xpGrantResponse.seasonId) &&
        Objects.equals(this.playerId, xpGrantResponse.playerId) &&
        Objects.equals(this.xpAdded, xpGrantResponse.xpAdded) &&
        Objects.equals(this.currentLevel, xpGrantResponse.currentLevel) &&
        Objects.equals(this.currentXP, xpGrantResponse.currentXP) &&
        Objects.equals(this.totalXPEarned, xpGrantResponse.totalXPEarned) &&
        Objects.equals(this.levelUps, xpGrantResponse.levelUps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seasonId, playerId, xpAdded, currentLevel, currentXP, totalXPEarned, levelUps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XpGrantResponse {\n");
    sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    xpAdded: ").append(toIndentedString(xpAdded)).append("\n");
    sb.append("    currentLevel: ").append(toIndentedString(currentLevel)).append("\n");
    sb.append("    currentXP: ").append(toIndentedString(currentXP)).append("\n");
    sb.append("    totalXPEarned: ").append(toIndentedString(totalXPEarned)).append("\n");
    sb.append("    levelUps: ").append(toIndentedString(levelUps)).append("\n");
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

