package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.StarterQuestRewardsItemsInner;
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
 * StarterQuestRewards
 */

@JsonTypeName("StarterQuest_rewards")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class StarterQuestRewards {

  private @Nullable Integer experience;

  @Valid
  private List<@Valid StarterQuestRewardsItemsInner> items = new ArrayList<>();

  private @Nullable Integer currency;

  public StarterQuestRewards experience(@Nullable Integer experience) {
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

  public StarterQuestRewards items(List<@Valid StarterQuestRewardsItemsInner> items) {
    this.items = items;
    return this;
  }

  public StarterQuestRewards addItemsItem(StarterQuestRewardsItemsInner itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Предметы, выдаваемые за квест
   * @return items
   */
  @Valid 
  @Schema(name = "items", description = "Предметы, выдаваемые за квест", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid StarterQuestRewardsItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid StarterQuestRewardsItemsInner> items) {
    this.items = items;
  }

  public StarterQuestRewards currency(@Nullable Integer currency) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StarterQuestRewards starterQuestRewards = (StarterQuestRewards) o;
    return Objects.equals(this.experience, starterQuestRewards.experience) &&
        Objects.equals(this.items, starterQuestRewards.items) &&
        Objects.equals(this.currency, starterQuestRewards.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, items, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StarterQuestRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

