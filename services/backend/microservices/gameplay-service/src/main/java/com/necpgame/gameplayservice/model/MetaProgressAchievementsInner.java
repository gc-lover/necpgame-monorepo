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
 * MetaProgressAchievementsInner
 */

@JsonTypeName("MetaProgress_achievements_inner")

public class MetaProgressAchievementsInner {

  private @Nullable String achievementId;

  private @Nullable String name;

  private @Nullable String earnedInLeague;

  public MetaProgressAchievementsInner achievementId(@Nullable String achievementId) {
    this.achievementId = achievementId;
    return this;
  }

  /**
   * Get achievementId
   * @return achievementId
   */
  
  @Schema(name = "achievement_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievement_id")
  public @Nullable String getAchievementId() {
    return achievementId;
  }

  public void setAchievementId(@Nullable String achievementId) {
    this.achievementId = achievementId;
  }

  public MetaProgressAchievementsInner name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public MetaProgressAchievementsInner earnedInLeague(@Nullable String earnedInLeague) {
    this.earnedInLeague = earnedInLeague;
    return this;
  }

  /**
   * Get earnedInLeague
   * @return earnedInLeague
   */
  
  @Schema(name = "earned_in_league", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("earned_in_league")
  public @Nullable String getEarnedInLeague() {
    return earnedInLeague;
  }

  public void setEarnedInLeague(@Nullable String earnedInLeague) {
    this.earnedInLeague = earnedInLeague;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaProgressAchievementsInner metaProgressAchievementsInner = (MetaProgressAchievementsInner) o;
    return Objects.equals(this.achievementId, metaProgressAchievementsInner.achievementId) &&
        Objects.equals(this.name, metaProgressAchievementsInner.name) &&
        Objects.equals(this.earnedInLeague, metaProgressAchievementsInner.earnedInLeague);
  }

  @Override
  public int hashCode() {
    return Objects.hash(achievementId, name, earnedInLeague);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaProgressAchievementsInner {\n");
    sb.append("    achievementId: ").append(toIndentedString(achievementId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    earnedInLeague: ").append(toIndentedString(earnedInLeague)).append("\n");
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

