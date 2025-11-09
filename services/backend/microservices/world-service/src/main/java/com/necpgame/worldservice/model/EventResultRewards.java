package com.necpgame.worldservice.model;

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
 * EventResultRewards
 */

@JsonTypeName("EventResult_rewards")

public class EventResultRewards {

  private @Nullable Integer experience;

  private @Nullable Integer currency;

  @Valid
  private List<String> items = new ArrayList<>();

  @Valid
  private Map<String, Integer> reputation = new HashMap<>();

  public EventResultRewards experience(@Nullable Integer experience) {
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

  public EventResultRewards currency(@Nullable Integer currency) {
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

  public EventResultRewards items(List<String> items) {
    this.items = items;
    return this;
  }

  public EventResultRewards addItemsItem(String itemsItem) {
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
  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

  public EventResultRewards reputation(Map<String, Integer> reputation) {
    this.reputation = reputation;
    return this;
  }

  public EventResultRewards putReputationItem(String key, Integer reputationItem) {
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
    EventResultRewards eventResultRewards = (EventResultRewards) o;
    return Objects.equals(this.experience, eventResultRewards.experience) &&
        Objects.equals(this.currency, eventResultRewards.currency) &&
        Objects.equals(this.items, eventResultRewards.items) &&
        Objects.equals(this.reputation, eventResultRewards.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, currency, items, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventResultRewards {\n");
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

