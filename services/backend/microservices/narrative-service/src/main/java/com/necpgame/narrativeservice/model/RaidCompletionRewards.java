package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * RaidCompletionRewards
 */

@JsonTypeName("RaidCompletion_rewards")

public class RaidCompletionRewards {

  @Valid
  private List<String> legendaryItems = new ArrayList<>();

  @Valid
  private List<String> corporateArtifacts = new ArrayList<>();

  private @Nullable BigDecimal experience;

  private @Nullable BigDecimal currency;

  public RaidCompletionRewards legendaryItems(List<String> legendaryItems) {
    this.legendaryItems = legendaryItems;
    return this;
  }

  public RaidCompletionRewards addLegendaryItemsItem(String legendaryItemsItem) {
    if (this.legendaryItems == null) {
      this.legendaryItems = new ArrayList<>();
    }
    this.legendaryItems.add(legendaryItemsItem);
    return this;
  }

  /**
   * Get legendaryItems
   * @return legendaryItems
   */
  
  @Schema(name = "legendary_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("legendary_items")
  public List<String> getLegendaryItems() {
    return legendaryItems;
  }

  public void setLegendaryItems(List<String> legendaryItems) {
    this.legendaryItems = legendaryItems;
  }

  public RaidCompletionRewards corporateArtifacts(List<String> corporateArtifacts) {
    this.corporateArtifacts = corporateArtifacts;
    return this;
  }

  public RaidCompletionRewards addCorporateArtifactsItem(String corporateArtifactsItem) {
    if (this.corporateArtifacts == null) {
      this.corporateArtifacts = new ArrayList<>();
    }
    this.corporateArtifacts.add(corporateArtifactsItem);
    return this;
  }

  /**
   * Get corporateArtifacts
   * @return corporateArtifacts
   */
  
  @Schema(name = "corporate_artifacts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("corporate_artifacts")
  public List<String> getCorporateArtifacts() {
    return corporateArtifacts;
  }

  public void setCorporateArtifacts(List<String> corporateArtifacts) {
    this.corporateArtifacts = corporateArtifacts;
  }

  public RaidCompletionRewards experience(@Nullable BigDecimal experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  @Valid 
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable BigDecimal getExperience() {
    return experience;
  }

  public void setExperience(@Nullable BigDecimal experience) {
    this.experience = experience;
  }

  public RaidCompletionRewards currency(@Nullable BigDecimal currency) {
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
  public @Nullable BigDecimal getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable BigDecimal currency) {
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
    RaidCompletionRewards raidCompletionRewards = (RaidCompletionRewards) o;
    return Objects.equals(this.legendaryItems, raidCompletionRewards.legendaryItems) &&
        Objects.equals(this.corporateArtifacts, raidCompletionRewards.corporateArtifacts) &&
        Objects.equals(this.experience, raidCompletionRewards.experience) &&
        Objects.equals(this.currency, raidCompletionRewards.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(legendaryItems, corporateArtifacts, experience, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RaidCompletionRewards {\n");
    sb.append("    legendaryItems: ").append(toIndentedString(legendaryItems)).append("\n");
    sb.append("    corporateArtifacts: ").append(toIndentedString(corporateArtifacts)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
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

