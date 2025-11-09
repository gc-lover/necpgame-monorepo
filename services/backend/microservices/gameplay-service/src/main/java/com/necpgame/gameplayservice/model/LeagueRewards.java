package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LeagueRewardsRewardsInner;
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
 * LeagueRewards
 */


public class LeagueRewards {

  private @Nullable String accountId;

  private @Nullable Integer currentRank;

  @Valid
  private List<@Valid LeagueRewardsRewardsInner> rewards = new ArrayList<>();

  public LeagueRewards accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public LeagueRewards currentRank(@Nullable Integer currentRank) {
    this.currentRank = currentRank;
    return this;
  }

  /**
   * Get currentRank
   * @return currentRank
   */
  
  @Schema(name = "current_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_rank")
  public @Nullable Integer getCurrentRank() {
    return currentRank;
  }

  public void setCurrentRank(@Nullable Integer currentRank) {
    this.currentRank = currentRank;
  }

  public LeagueRewards rewards(List<@Valid LeagueRewardsRewardsInner> rewards) {
    this.rewards = rewards;
    return this;
  }

  public LeagueRewards addRewardsItem(LeagueRewardsRewardsInner rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Доступные награды на основе рейтинга
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", description = "Доступные награды на основе рейтинга", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid LeagueRewardsRewardsInner> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid LeagueRewardsRewardsInner> rewards) {
    this.rewards = rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeagueRewards leagueRewards = (LeagueRewards) o;
    return Objects.equals(this.accountId, leagueRewards.accountId) &&
        Objects.equals(this.currentRank, leagueRewards.currentRank) &&
        Objects.equals(this.rewards, leagueRewards.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, currentRank, rewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeagueRewards {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    currentRank: ").append(toIndentedString(currentRank)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
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

