package com.necpgame.backjava.model;

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
 * DailyQuestRewards
 */

@JsonTypeName("DailyQuest_rewards")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DailyQuestRewards {

  private @Nullable Integer experience;

  private @Nullable Integer currency;

  private @Nullable Object reputation;

  @Valid
  private List<Object> bonusRewards = new ArrayList<>();

  public DailyQuestRewards experience(@Nullable Integer experience) {
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

  public DailyQuestRewards currency(@Nullable Integer currency) {
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

  public DailyQuestRewards reputation(@Nullable Object reputation) {
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

  public DailyQuestRewards bonusRewards(List<Object> bonusRewards) {
    this.bonusRewards = bonusRewards;
    return this;
  }

  public DailyQuestRewards addBonusRewardsItem(Object bonusRewardsItem) {
    if (this.bonusRewards == null) {
      this.bonusRewards = new ArrayList<>();
    }
    this.bonusRewards.add(bonusRewardsItem);
    return this;
  }

  /**
   * Дополнительные награды при первом выполнении
   * @return bonusRewards
   */
  
  @Schema(name = "bonus_rewards", description = "Дополнительные награды при первом выполнении", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_rewards")
  public List<Object> getBonusRewards() {
    return bonusRewards;
  }

  public void setBonusRewards(List<Object> bonusRewards) {
    this.bonusRewards = bonusRewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DailyQuestRewards dailyQuestRewards = (DailyQuestRewards) o;
    return Objects.equals(this.experience, dailyQuestRewards.experience) &&
        Objects.equals(this.currency, dailyQuestRewards.currency) &&
        Objects.equals(this.reputation, dailyQuestRewards.reputation) &&
        Objects.equals(this.bonusRewards, dailyQuestRewards.bonusRewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(experience, currency, reputation, bonusRewards);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DailyQuestRewards {\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
    sb.append("    bonusRewards: ").append(toIndentedString(bonusRewards)).append("\n");
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

