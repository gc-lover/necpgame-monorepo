package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.MissionRewardPreviewReputation;
import com.necpgame.gameplayservice.model.RewardItem;
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
 * MissionRewardPreview
 */


public class MissionRewardPreview {

  private @Nullable Integer credits;

  private @Nullable Integer xpGain;

  @Valid
  private List<@Valid RewardItem> items = new ArrayList<>();

  private @Nullable MissionRewardPreviewReputation reputation;

  public MissionRewardPreview credits(@Nullable Integer credits) {
    this.credits = credits;
    return this;
  }

  /**
   * Get credits
   * @return credits
   */
  
  @Schema(name = "credits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("credits")
  public @Nullable Integer getCredits() {
    return credits;
  }

  public void setCredits(@Nullable Integer credits) {
    this.credits = credits;
  }

  public MissionRewardPreview xpGain(@Nullable Integer xpGain) {
    this.xpGain = xpGain;
    return this;
  }

  /**
   * Get xpGain
   * @return xpGain
   */
  
  @Schema(name = "xpGain", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpGain")
  public @Nullable Integer getXpGain() {
    return xpGain;
  }

  public void setXpGain(@Nullable Integer xpGain) {
    this.xpGain = xpGain;
  }

  public MissionRewardPreview items(List<@Valid RewardItem> items) {
    this.items = items;
    return this;
  }

  public MissionRewardPreview addItemsItem(RewardItem itemsItem) {
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
  public List<@Valid RewardItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid RewardItem> items) {
    this.items = items;
  }

  public MissionRewardPreview reputation(@Nullable MissionRewardPreviewReputation reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  @Valid 
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable MissionRewardPreviewReputation getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable MissionRewardPreviewReputation reputation) {
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
    MissionRewardPreview missionRewardPreview = (MissionRewardPreview) o;
    return Objects.equals(this.credits, missionRewardPreview.credits) &&
        Objects.equals(this.xpGain, missionRewardPreview.xpGain) &&
        Objects.equals(this.items, missionRewardPreview.items) &&
        Objects.equals(this.reputation, missionRewardPreview.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(credits, xpGain, items, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MissionRewardPreview {\n");
    sb.append("    credits: ").append(toIndentedString(credits)).append("\n");
    sb.append("    xpGain: ").append(toIndentedString(xpGain)).append("\n");
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

