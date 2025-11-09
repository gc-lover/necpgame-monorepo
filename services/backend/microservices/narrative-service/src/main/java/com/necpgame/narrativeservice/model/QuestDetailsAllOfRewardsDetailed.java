package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestDetailsAllOfRewardsDetailed
 */

@JsonTypeName("QuestDetails_allOf_rewards_detailed")

public class QuestDetailsAllOfRewardsDetailed {

  private @Nullable Integer experience;

  private @Nullable Integer streetCred;

  private @Nullable Object currency;

  @Valid
  private List<Object> items = new ArrayList<>();

  @Valid
  private Map<String, Integer> reputation = new HashMap<>();

  public QuestDetailsAllOfRewardsDetailed experience(@Nullable Integer experience) {
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

  public QuestDetailsAllOfRewardsDetailed streetCred(@Nullable Integer streetCred) {
    this.streetCred = streetCred;
    return this;
  }

  /**
   * Get streetCred
   * @return streetCred
   */
  
  @Schema(name = "street_cred", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("street_cred")
  public @Nullable Integer getStreetCred() {
    return streetCred;
  }

  public void setStreetCred(@Nullable Integer streetCred) {
    this.streetCred = streetCred;
  }

  public QuestDetailsAllOfRewardsDetailed currency(@Nullable Object currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable Object getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable Object currency) {
    this.currency = currency;
  }

  public QuestDetailsAllOfRewardsDetailed items(List<Object> items) {
    this.items = items;
    return this;
  }

  public QuestDetailsAllOfRewardsDetailed addItemsItem(Object itemsItem) {
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
  
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<Object> getItems() {
    return items;
  }

  public void setItems(List<Object> items) {
    this.items = items;
  }

  public QuestDetailsAllOfRewardsDetailed reputation(Map<String, Integer> reputation) {
    this.reputation = reputation;
    return this;
  }

  public QuestDetailsAllOfRewardsDetailed putReputationItem(String key, Integer reputationItem) {
    if (this.reputation == null) {
      this.reputation = new HashMap<>();
    }
    this.reputation.put(key, reputationItem);
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public Map<String, Integer> getReputation() {
    return reputation;
  }

  public void setReputation(Map<String, Integer> reputation) {
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
    QuestDetailsAllOfRewardsDetailed questDetailsAllOfRewardsDetailed = (QuestDetailsAllOfRewardsDetailed) o;
    return Objects.equals(this.experience, questDetailsAllOfRewardsDetailed.experience) &&
        Objects.equals(this.streetCred, questDetailsAllOfRewardsDetailed.streetCred) &&
        Objects.equals(this.currency, questDetailsAllOfRewardsDetailed.currency) &&
        Objects.equals(this.items, questDetailsAllOfRewardsDetailed.items) &&
        Objects.equals(this.reputation, questDetailsAllOfRewardsDetailed.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, streetCred, currency, items, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestDetailsAllOfRewardsDetailed {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    streetCred: ").append(toIndentedString(streetCred)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

