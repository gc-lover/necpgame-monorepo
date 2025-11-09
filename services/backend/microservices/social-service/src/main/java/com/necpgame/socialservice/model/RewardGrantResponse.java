package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RewardGrant;
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
 * RewardGrantResponse
 */


public class RewardGrantResponse {

  @Valid
  private List<@Valid RewardGrant> rewards = new ArrayList<>();

  @Valid
  private List<String> notifications = new ArrayList<>();

  public RewardGrantResponse rewards(List<@Valid RewardGrant> rewards) {
    this.rewards = rewards;
    return this;
  }

  public RewardGrantResponse addRewardsItem(RewardGrant rewardsItem) {
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
  public List<@Valid RewardGrant> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid RewardGrant> rewards) {
    this.rewards = rewards;
  }

  public RewardGrantResponse notifications(List<String> notifications) {
    this.notifications = notifications;
    return this;
  }

  public RewardGrantResponse addNotificationsItem(String notificationsItem) {
    if (this.notifications == null) {
      this.notifications = new ArrayList<>();
    }
    this.notifications.add(notificationsItem);
    return this;
  }

  /**
   * Get notifications
   * @return notifications
   */
  
  @Schema(name = "notifications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifications")
  public List<String> getNotifications() {
    return notifications;
  }

  public void setNotifications(List<String> notifications) {
    this.notifications = notifications;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardGrantResponse rewardGrantResponse = (RewardGrantResponse) o;
    return Objects.equals(this.rewards, rewardGrantResponse.rewards) &&
        Objects.equals(this.notifications, rewardGrantResponse.notifications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewards, notifications);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardGrantResponse {\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    notifications: ").append(toIndentedString(notifications)).append("\n");
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

