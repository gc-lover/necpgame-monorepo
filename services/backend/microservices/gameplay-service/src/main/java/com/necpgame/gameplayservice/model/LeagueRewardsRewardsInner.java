package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LeagueRewardsRewardsInner
 */

@JsonTypeName("LeagueRewards_rewards_inner")

public class LeagueRewardsRewardsInner {

  /**
   * Gets or Sets rewardType
   */
  public enum RewardTypeEnum {
    TITLE("title"),
    
    COSMETIC("cosmetic"),
    
    LEGACY_ITEM("legacy_item"),
    
    ACHIEVEMENT("achievement");

    private final String value;

    RewardTypeEnum(String value) {
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
    public static RewardTypeEnum fromValue(String value) {
      for (RewardTypeEnum b : RewardTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RewardTypeEnum rewardType;

  private @Nullable String rewardId;

  private @Nullable String name;

  private @Nullable String requirement;

  public LeagueRewardsRewardsInner rewardType(@Nullable RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
    return this;
  }

  /**
   * Get rewardType
   * @return rewardType
   */
  
  @Schema(name = "reward_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reward_type")
  public @Nullable RewardTypeEnum getRewardType() {
    return rewardType;
  }

  public void setRewardType(@Nullable RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
  }

  public LeagueRewardsRewardsInner rewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  
  @Schema(name = "reward_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reward_id")
  public @Nullable String getRewardId() {
    return rewardId;
  }

  public void setRewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
  }

  public LeagueRewardsRewardsInner name(@Nullable String name) {
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

  public LeagueRewardsRewardsInner requirement(@Nullable String requirement) {
    this.requirement = requirement;
    return this;
  }

  /**
   * Требование для получения (например, Top 100)
   * @return requirement
   */
  
  @Schema(name = "requirement", description = "Требование для получения (например, Top 100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirement")
  public @Nullable String getRequirement() {
    return requirement;
  }

  public void setRequirement(@Nullable String requirement) {
    this.requirement = requirement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeagueRewardsRewardsInner leagueRewardsRewardsInner = (LeagueRewardsRewardsInner) o;
    return Objects.equals(this.rewardType, leagueRewardsRewardsInner.rewardType) &&
        Objects.equals(this.rewardId, leagueRewardsRewardsInner.rewardId) &&
        Objects.equals(this.name, leagueRewardsRewardsInner.name) &&
        Objects.equals(this.requirement, leagueRewardsRewardsInner.requirement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardType, rewardId, name, requirement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeagueRewardsRewardsInner {\n");
    sb.append("    rewardType: ").append(toIndentedString(rewardType)).append("\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    requirement: ").append(toIndentedString(requirement)).append("\n");
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

