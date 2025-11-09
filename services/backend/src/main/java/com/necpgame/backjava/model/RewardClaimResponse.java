package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.RewardDefinition;
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
 * RewardClaimResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RewardClaimResponse {

  @Valid
  private List<@Valid RewardDefinition> rewards = new ArrayList<>();

  private @Nullable Integer xpEarned;

  private @Nullable Boolean premiumStatus;

  @Valid
  private List<String> events = new ArrayList<>();

  public RewardClaimResponse rewards(List<@Valid RewardDefinition> rewards) {
    this.rewards = rewards;
    return this;
  }

  public RewardClaimResponse addRewardsItem(RewardDefinition rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid RewardDefinition> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid RewardDefinition> rewards) {
    this.rewards = rewards;
  }

  public RewardClaimResponse xpEarned(@Nullable Integer xpEarned) {
    this.xpEarned = xpEarned;
    return this;
  }

  /**
   * Get xpEarned
   * @return xpEarned
   */
  
  @Schema(name = "xpEarned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpEarned")
  public @Nullable Integer getXpEarned() {
    return xpEarned;
  }

  public void setXpEarned(@Nullable Integer xpEarned) {
    this.xpEarned = xpEarned;
  }

  public RewardClaimResponse premiumStatus(@Nullable Boolean premiumStatus) {
    this.premiumStatus = premiumStatus;
    return this;
  }

  /**
   * Get premiumStatus
   * @return premiumStatus
   */
  
  @Schema(name = "premiumStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premiumStatus")
  public @Nullable Boolean getPremiumStatus() {
    return premiumStatus;
  }

  public void setPremiumStatus(@Nullable Boolean premiumStatus) {
    this.premiumStatus = premiumStatus;
  }

  public RewardClaimResponse events(List<String> events) {
    this.events = events;
    return this;
  }

  public RewardClaimResponse addEventsItem(String eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<String> getEvents() {
    return events;
  }

  public void setEvents(List<String> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardClaimResponse rewardClaimResponse = (RewardClaimResponse) o;
    return Objects.equals(this.rewards, rewardClaimResponse.rewards) &&
        Objects.equals(this.xpEarned, rewardClaimResponse.xpEarned) &&
        Objects.equals(this.premiumStatus, rewardClaimResponse.premiumStatus) &&
        Objects.equals(this.events, rewardClaimResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewards, xpEarned, premiumStatus, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardClaimResponse {\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    xpEarned: ").append(toIndentedString(xpEarned)).append("\n");
    sb.append("    premiumStatus: ").append(toIndentedString(premiumStatus)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

