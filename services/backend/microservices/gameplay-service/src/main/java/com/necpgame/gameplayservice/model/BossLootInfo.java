package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LootItem;
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
 * BossLootInfo
 */


public class BossLootInfo {

  private @Nullable String bossId;

  @Valid
  private List<@Valid LootItem> guaranteedItems = new ArrayList<>();

  private @Nullable String randomTableId;

  /**
   * Gets or Sets partyDistribution
   */
  public enum PartyDistributionEnum {
    SHARED("SHARED"),
    
    INDIVIDUAL("INDIVIDUAL");

    private final String value;

    PartyDistributionEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PartyDistributionEnum fromValue(String value) {
      for (PartyDistributionEnum b : PartyDistributionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PartyDistributionEnum partyDistribution;

  @Valid
  private List<String> achievements = new ArrayList<>();

  public BossLootInfo bossId(@Nullable String bossId) {
    this.bossId = bossId;
    return this;
  }

  /**
   * Get bossId
   * @return bossId
   */
  
  @Schema(name = "bossId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bossId")
  public @Nullable String getBossId() {
    return bossId;
  }

  public void setBossId(@Nullable String bossId) {
    this.bossId = bossId;
  }

  public BossLootInfo guaranteedItems(List<@Valid LootItem> guaranteedItems) {
    this.guaranteedItems = guaranteedItems;
    return this;
  }

  public BossLootInfo addGuaranteedItemsItem(LootItem guaranteedItemsItem) {
    if (this.guaranteedItems == null) {
      this.guaranteedItems = new ArrayList<>();
    }
    this.guaranteedItems.add(guaranteedItemsItem);
    return this;
  }

  /**
   * Get guaranteedItems
   * @return guaranteedItems
   */
  @Valid 
  @Schema(name = "guaranteedItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guaranteedItems")
  public List<@Valid LootItem> getGuaranteedItems() {
    return guaranteedItems;
  }

  public void setGuaranteedItems(List<@Valid LootItem> guaranteedItems) {
    this.guaranteedItems = guaranteedItems;
  }

  public BossLootInfo randomTableId(@Nullable String randomTableId) {
    this.randomTableId = randomTableId;
    return this;
  }

  /**
   * Get randomTableId
   * @return randomTableId
   */
  
  @Schema(name = "randomTableId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("randomTableId")
  public @Nullable String getRandomTableId() {
    return randomTableId;
  }

  public void setRandomTableId(@Nullable String randomTableId) {
    this.randomTableId = randomTableId;
  }

  public BossLootInfo partyDistribution(@Nullable PartyDistributionEnum partyDistribution) {
    this.partyDistribution = partyDistribution;
    return this;
  }

  /**
   * Get partyDistribution
   * @return partyDistribution
   */
  
  @Schema(name = "partyDistribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyDistribution")
  public @Nullable PartyDistributionEnum getPartyDistribution() {
    return partyDistribution;
  }

  public void setPartyDistribution(@Nullable PartyDistributionEnum partyDistribution) {
    this.partyDistribution = partyDistribution;
  }

  public BossLootInfo achievements(List<String> achievements) {
    this.achievements = achievements;
    return this;
  }

  public BossLootInfo addAchievementsItem(String achievementsItem) {
    if (this.achievements == null) {
      this.achievements = new ArrayList<>();
    }
    this.achievements.add(achievementsItem);
    return this;
  }

  /**
   * Get achievements
   * @return achievements
   */
  
  @Schema(name = "achievements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("achievements")
  public List<String> getAchievements() {
    return achievements;
  }

  public void setAchievements(List<String> achievements) {
    this.achievements = achievements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BossLootInfo bossLootInfo = (BossLootInfo) o;
    return Objects.equals(this.bossId, bossLootInfo.bossId) &&
        Objects.equals(this.guaranteedItems, bossLootInfo.guaranteedItems) &&
        Objects.equals(this.randomTableId, bossLootInfo.randomTableId) &&
        Objects.equals(this.partyDistribution, bossLootInfo.partyDistribution) &&
        Objects.equals(this.achievements, bossLootInfo.achievements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bossId, guaranteedItems, randomTableId, partyDistribution, achievements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BossLootInfo {\n");
    sb.append("    bossId: ").append(toIndentedString(bossId)).append("\n");
    sb.append("    guaranteedItems: ").append(toIndentedString(guaranteedItems)).append("\n");
    sb.append("    randomTableId: ").append(toIndentedString(randomTableId)).append("\n");
    sb.append("    partyDistribution: ").append(toIndentedString(partyDistribution)).append("\n");
    sb.append("    achievements: ").append(toIndentedString(achievements)).append("\n");
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

