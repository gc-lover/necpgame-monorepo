package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
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
 * AchievementRewardsBadge
 */

@JsonTypeName("AchievementRewards_badge")

public class AchievementRewardsBadge {

  private @Nullable UUID badgeId;

  private @Nullable String name;

  private @Nullable URI icon;

  public AchievementRewardsBadge badgeId(@Nullable UUID badgeId) {
    this.badgeId = badgeId;
    return this;
  }

  /**
   * Get badgeId
   * @return badgeId
   */
  @Valid 
  @Schema(name = "badge_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("badge_id")
  public @Nullable UUID getBadgeId() {
    return badgeId;
  }

  public void setBadgeId(@Nullable UUID badgeId) {
    this.badgeId = badgeId;
  }

  public AchievementRewardsBadge name(@Nullable String name) {
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

  public AchievementRewardsBadge icon(@Nullable URI icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get icon
   * @return icon
   */
  @Valid 
  @Schema(name = "icon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("icon")
  public @Nullable URI getIcon() {
    return icon;
  }

  public void setIcon(@Nullable URI icon) {
    this.icon = icon;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AchievementRewardsBadge achievementRewardsBadge = (AchievementRewardsBadge) o;
    return Objects.equals(this.badgeId, achievementRewardsBadge.badgeId) &&
        Objects.equals(this.name, achievementRewardsBadge.name) &&
        Objects.equals(this.icon, achievementRewardsBadge.icon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(badgeId, name, icon);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AchievementRewardsBadge {\n");
    sb.append("    badgeId: ").append(toIndentedString(badgeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
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

