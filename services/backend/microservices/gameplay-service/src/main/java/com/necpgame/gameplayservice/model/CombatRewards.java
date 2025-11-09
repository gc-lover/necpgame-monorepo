package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CombatRewardsCurrency;
import com.necpgame.gameplayservice.model.CombatRewardsLootInner;
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
 * CombatRewards
 */


public class CombatRewards {

  private @Nullable Integer experience;

  private @Nullable CombatRewardsCurrency currency;

  @Valid
  private List<@Valid CombatRewardsLootInner> loot = new ArrayList<>();

  public CombatRewards experience(@Nullable Integer experience) {
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

  public CombatRewards currency(@Nullable CombatRewardsCurrency currency) {
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
  public @Nullable CombatRewardsCurrency getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable CombatRewardsCurrency currency) {
    this.currency = currency;
  }

  public CombatRewards loot(List<@Valid CombatRewardsLootInner> loot) {
    this.loot = loot;
    return this;
  }

  public CombatRewards addLootItem(CombatRewardsLootInner lootItem) {
    if (this.loot == null) {
      this.loot = new ArrayList<>();
    }
    this.loot.add(lootItem);
    return this;
  }

  /**
   * Get loot
   * @return loot
   */
  @Valid 
  @Schema(name = "loot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot")
  public List<@Valid CombatRewardsLootInner> getLoot() {
    return loot;
  }

  public void setLoot(List<@Valid CombatRewardsLootInner> loot) {
    this.loot = loot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatRewards combatRewards = (CombatRewards) o;
    return Objects.equals(this.experience, combatRewards.experience) &&
        Objects.equals(this.currency, combatRewards.currency) &&
        Objects.equals(this.loot, combatRewards.loot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, currency, loot);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    loot: ").append(toIndentedString(loot)).append("\n");
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

