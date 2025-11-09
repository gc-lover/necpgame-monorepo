package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LevelUpRewardsCurrency;
import com.necpgame.gameplayservice.model.LevelUpRewardsItemsInner;
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
 * LevelUpRewards
 */


public class LevelUpRewards {

  private @Nullable Integer attributePoints;

  private @Nullable Integer skillPoints;

  private @Nullable Integer perkPoints;

  private @Nullable LevelUpRewardsCurrency currency;

  @Valid
  private List<@Valid LevelUpRewardsItemsInner> items = new ArrayList<>();

  public LevelUpRewards attributePoints(@Nullable Integer attributePoints) {
    this.attributePoints = attributePoints;
    return this;
  }

  /**
   * Get attributePoints
   * @return attributePoints
   */
  
  @Schema(name = "attribute_points", example = "3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_points")
  public @Nullable Integer getAttributePoints() {
    return attributePoints;
  }

  public void setAttributePoints(@Nullable Integer attributePoints) {
    this.attributePoints = attributePoints;
  }

  public LevelUpRewards skillPoints(@Nullable Integer skillPoints) {
    this.skillPoints = skillPoints;
    return this;
  }

  /**
   * Get skillPoints
   * @return skillPoints
   */
  
  @Schema(name = "skill_points", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_points")
  public @Nullable Integer getSkillPoints() {
    return skillPoints;
  }

  public void setSkillPoints(@Nullable Integer skillPoints) {
    this.skillPoints = skillPoints;
  }

  public LevelUpRewards perkPoints(@Nullable Integer perkPoints) {
    this.perkPoints = perkPoints;
    return this;
  }

  /**
   * Get perkPoints
   * @return perkPoints
   */
  
  @Schema(name = "perk_points", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("perk_points")
  public @Nullable Integer getPerkPoints() {
    return perkPoints;
  }

  public void setPerkPoints(@Nullable Integer perkPoints) {
    this.perkPoints = perkPoints;
  }

  public LevelUpRewards currency(@Nullable LevelUpRewardsCurrency currency) {
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
  public @Nullable LevelUpRewardsCurrency getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable LevelUpRewardsCurrency currency) {
    this.currency = currency;
  }

  public LevelUpRewards items(List<@Valid LevelUpRewardsItemsInner> items) {
    this.items = items;
    return this;
  }

  public LevelUpRewards addItemsItem(LevelUpRewardsItemsInner itemsItem) {
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
  public List<@Valid LevelUpRewardsItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid LevelUpRewardsItemsInner> items) {
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
    LevelUpRewards levelUpRewards = (LevelUpRewards) o;
    return Objects.equals(this.attributePoints, levelUpRewards.attributePoints) &&
        Objects.equals(this.skillPoints, levelUpRewards.skillPoints) &&
        Objects.equals(this.perkPoints, levelUpRewards.perkPoints) &&
        Objects.equals(this.currency, levelUpRewards.currency) &&
        Objects.equals(this.items, levelUpRewards.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributePoints, skillPoints, perkPoints, currency, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelUpRewards {\n");
    sb.append("    attributePoints: ").append(toIndentedString(attributePoints)).append("\n");
    sb.append("    skillPoints: ").append(toIndentedString(skillPoints)).append("\n");
    sb.append("    perkPoints: ").append(toIndentedString(perkPoints)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

