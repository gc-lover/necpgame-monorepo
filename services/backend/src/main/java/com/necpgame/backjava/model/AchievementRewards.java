package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.AchievementRewardsBadge;
import com.necpgame.backjava.model.AchievementRewardsCurrency;
import com.necpgame.backjava.model.AchievementRewardsItemsInner;
import com.necpgame.backjava.model.Title;
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
 * AchievementRewards
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AchievementRewards {

  private @Nullable Integer experience;

  private @Nullable AchievementRewardsCurrency currency;

  @Valid
  private List<@Valid AchievementRewardsItemsInner> items = new ArrayList<>();

  private @Nullable Title title;

  private @Nullable AchievementRewardsBadge badge;

  public AchievementRewards experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  
  @Schema(name = "experience", example = "1000", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public AchievementRewards currency(@Nullable AchievementRewardsCurrency currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Valid 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable AchievementRewardsCurrency getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable AchievementRewardsCurrency currency) {
    this.currency = currency;
  }

  public AchievementRewards items(List<@Valid AchievementRewardsItemsInner> items) {
    this.items = items;
    return this;
  }

  public AchievementRewards addItemsItem(AchievementRewardsItemsInner itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid AchievementRewardsItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid AchievementRewardsItemsInner> items) {
    this.items = items;
  }

  public AchievementRewards title(@Nullable Title title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @Valid 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable Title getTitle() {
    return title;
  }

  public void setTitle(@Nullable Title title) {
    this.title = title;
  }

  public AchievementRewards badge(@Nullable AchievementRewardsBadge badge) {
    this.badge = badge;
    return this;
  }

  /**
   * Get badge
   * @return badge
   */
  @Valid 
  @Schema(name = "badge", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("badge")
  public @Nullable AchievementRewardsBadge getBadge() {
    return badge;
  }

  public void setBadge(@Nullable AchievementRewardsBadge badge) {
    this.badge = badge;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AchievementRewards achievementRewards = (AchievementRewards) o;
    return Objects.equals(this.experience, achievementRewards.experience) &&
        Objects.equals(this.currency, achievementRewards.currency) &&
        Objects.equals(this.items, achievementRewards.items) &&
        Objects.equals(this.title, achievementRewards.title) &&
        Objects.equals(this.badge, achievementRewards.badge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, currency, items, title, badge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AchievementRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    badge: ").append(toIndentedString(badge)).append("\n");
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

