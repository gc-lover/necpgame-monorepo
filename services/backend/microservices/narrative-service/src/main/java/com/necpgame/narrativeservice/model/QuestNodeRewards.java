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
 * QuestNodeRewards
 */

@JsonTypeName("QuestNode_rewards")

public class QuestNodeRewards {

  private @Nullable Integer experience;

  private @Nullable Integer streetCred;

  private @Nullable Integer money;

  @Valid
  private List<String> items = new ArrayList<>();

  public QuestNodeRewards experience(@Nullable Integer experience) {
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

  public QuestNodeRewards streetCred(@Nullable Integer streetCred) {
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

  public QuestNodeRewards money(@Nullable Integer money) {
    this.money = money;
    return this;
  }

  /**
   * Get money
   * @return money
   */
  
  @Schema(name = "money", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("money")
  public @Nullable Integer getMoney() {
    return money;
  }

  public void setMoney(@Nullable Integer money) {
    this.money = money;
  }

  public QuestNodeRewards items(List<String> items) {
    this.items = items;
    return this;
  }

  public QuestNodeRewards addItemsItem(String itemsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestNodeRewards questNodeRewards = (QuestNodeRewards) o;
    return Objects.equals(this.experience, questNodeRewards.experience) &&
        Objects.equals(this.streetCred, questNodeRewards.streetCred) &&
        Objects.equals(this.money, questNodeRewards.money) &&
        Objects.equals(this.items, questNodeRewards.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, streetCred, money, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestNodeRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    streetCred: ").append(toIndentedString(streetCred)).append("\n");
    sb.append("    money: ").append(toIndentedString(money)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

