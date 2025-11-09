package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * WeeklyQuestRewards
 */

@JsonTypeName("WeeklyQuest_rewards")

public class WeeklyQuestRewards {

  private @Nullable Integer experience;

  private @Nullable Integer currency;

  @Valid
  private List<Object> rareItems = new ArrayList<>();

  private @Nullable Object reputation;

  public WeeklyQuestRewards experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public WeeklyQuestRewards currency(@Nullable Integer currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable Integer getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable Integer currency) {
    this.currency = currency;
  }

  public WeeklyQuestRewards rareItems(List<Object> rareItems) {
    this.rareItems = rareItems;
    return this;
  }

  public WeeklyQuestRewards addRareItemsItem(Object rareItemsItem) {
    if (this.rareItems == null) {
      this.rareItems = new ArrayList<>();
    }
    this.rareItems.add(rareItemsItem);
    return this;
  }

  /**
   * Get rareItems
   * @return rareItems
   */
  
  @Schema(name = "rare_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rare_items")
  public List<Object> getRareItems() {
    return rareItems;
  }

  public void setRareItems(List<Object> rareItems) {
    this.rareItems = rareItems;
  }

  public WeeklyQuestRewards reputation(@Nullable Object reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable Object getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable Object reputation) {
    this.reputation = reputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeeklyQuestRewards weeklyQuestRewards = (WeeklyQuestRewards) o;
    return Objects.equals(this.experience, weeklyQuestRewards.experience) &&
        Objects.equals(this.currency, weeklyQuestRewards.currency) &&
        Objects.equals(this.rareItems, weeklyQuestRewards.rareItems) &&
        Objects.equals(this.reputation, weeklyQuestRewards.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, currency, rareItems, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeeklyQuestRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    rareItems: ").append(toIndentedString(rareItems)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
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

