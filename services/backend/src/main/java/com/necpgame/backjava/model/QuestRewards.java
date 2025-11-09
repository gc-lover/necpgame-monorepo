package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.necpgame.backjava.model.QuestRewardsItemsInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestRewards
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:05.709666800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class QuestRewards {

  private Integer experience;

  private Integer currency;

  @Valid
  private List<@Valid QuestRewardsItemsInner> items = new ArrayList<>();

  @Valid
  private Map<String, Integer> reputation = new HashMap<>();

  public QuestRewards() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QuestRewards(Integer experience, Integer currency) {
    this.experience = experience;
    this.currency = currency;
  }

  public QuestRewards experience(Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  @NotNull 
  @Schema(name = "experience", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("experience")
  public Integer getExperience() {
    return experience;
  }

  public void setExperience(Integer experience) {
    this.experience = experience;
  }

  public QuestRewards currency(Integer currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Eddies
   * @return currency
   */
  @NotNull 
  @Schema(name = "currency", example = "500", description = "Eddies", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currency")
  public Integer getCurrency() {
    return currency;
  }

  public void setCurrency(Integer currency) {
    this.currency = currency;
  }

  public QuestRewards items(List<@Valid QuestRewardsItemsInner> items) {
    this.items = items;
    return this;
  }

  public QuestRewards addItemsItem(QuestRewardsItemsInner itemsItem) {
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
  public List<@Valid QuestRewardsItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid QuestRewardsItemsInner> items) {
    this.items = items;
  }

  public QuestRewards reputation(Map<String, Integer> reputation) {
    this.reputation = reputation;
    return this;
  }

  public QuestRewards putReputationItem(String key, Integer reputationItem) {
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
  
  @Schema(name = "reputation", example = "{\"valentinos\":5}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    QuestRewards questRewards = (QuestRewards) o;
    return Objects.equals(this.experience, questRewards.experience) &&
        Objects.equals(this.currency, questRewards.currency) &&
        Objects.equals(this.items, questRewards.items) &&
        Objects.equals(this.reputation, questRewards.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, currency, items, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
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


