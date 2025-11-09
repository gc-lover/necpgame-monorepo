package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BattlePassAnalyticsResponseXpDistributionInner
 */

@JsonTypeName("BattlePassAnalyticsResponse_xpDistribution_inner")

public class BattlePassAnalyticsResponseXpDistributionInner {

  private @Nullable String levelRange;

  private @Nullable Integer playerCount;

  public BattlePassAnalyticsResponseXpDistributionInner levelRange(@Nullable String levelRange) {
    this.levelRange = levelRange;
    return this;
  }

  /**
   * Get levelRange
   * @return levelRange
   */
  
  @Schema(name = "levelRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("levelRange")
  public @Nullable String getLevelRange() {
    return levelRange;
  }

  public void setLevelRange(@Nullable String levelRange) {
    this.levelRange = levelRange;
  }

  public BattlePassAnalyticsResponseXpDistributionInner playerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
    return this;
  }

  /**
   * Get playerCount
   * @return playerCount
   */
  
  @Schema(name = "playerCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerCount")
  public @Nullable Integer getPlayerCount() {
    return playerCount;
  }

  public void setPlayerCount(@Nullable Integer playerCount) {
    this.playerCount = playerCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassAnalyticsResponseXpDistributionInner battlePassAnalyticsResponseXpDistributionInner = (BattlePassAnalyticsResponseXpDistributionInner) o;
    return Objects.equals(this.levelRange, battlePassAnalyticsResponseXpDistributionInner.levelRange) &&
        Objects.equals(this.playerCount, battlePassAnalyticsResponseXpDistributionInner.playerCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(levelRange, playerCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassAnalyticsResponseXpDistributionInner {\n");
    sb.append("    levelRange: ").append(toIndentedString(levelRange)).append("\n");
    sb.append("    playerCount: ").append(toIndentedString(playerCount)).append("\n");
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

