package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.RewardAsset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardPayload
 */


public class RewardPayload {

  @Valid
  private List<@Valid RewardAsset> rewards = new ArrayList<>();

  private @Nullable String economyProfile;

  /**
   * Gets or Sets deliveryMethod
   */
  public enum DeliveryMethodEnum {
    DIRECT("DIRECT"),
    
    MAIL("MAIL"),
    
    VENDOR("VENDOR");

    private final String value;

    DeliveryMethodEnum(String value) {
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
    public static DeliveryMethodEnum fromValue(String value) {
      for (DeliveryMethodEnum b : DeliveryMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DeliveryMethodEnum deliveryMethod;

  @Valid
  private List<UUID> unlockedVendors = new ArrayList<>();

  public RewardPayload rewards(List<@Valid RewardAsset> rewards) {
    this.rewards = rewards;
    return this;
  }

  public RewardPayload addRewardsItem(RewardAsset rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid RewardAsset> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid RewardAsset> rewards) {
    this.rewards = rewards;
  }

  public RewardPayload economyProfile(@Nullable String economyProfile) {
    this.economyProfile = economyProfile;
    return this;
  }

  /**
   * Get economyProfile
   * @return economyProfile
   */
  
  @Schema(name = "economyProfile", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economyProfile")
  public @Nullable String getEconomyProfile() {
    return economyProfile;
  }

  public void setEconomyProfile(@Nullable String economyProfile) {
    this.economyProfile = economyProfile;
  }

  public RewardPayload deliveryMethod(@Nullable DeliveryMethodEnum deliveryMethod) {
    this.deliveryMethod = deliveryMethod;
    return this;
  }

  /**
   * Get deliveryMethod
   * @return deliveryMethod
   */
  
  @Schema(name = "deliveryMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveryMethod")
  public @Nullable DeliveryMethodEnum getDeliveryMethod() {
    return deliveryMethod;
  }

  public void setDeliveryMethod(@Nullable DeliveryMethodEnum deliveryMethod) {
    this.deliveryMethod = deliveryMethod;
  }

  public RewardPayload unlockedVendors(List<UUID> unlockedVendors) {
    this.unlockedVendors = unlockedVendors;
    return this;
  }

  public RewardPayload addUnlockedVendorsItem(UUID unlockedVendorsItem) {
    if (this.unlockedVendors == null) {
      this.unlockedVendors = new ArrayList<>();
    }
    this.unlockedVendors.add(unlockedVendorsItem);
    return this;
  }

  /**
   * Get unlockedVendors
   * @return unlockedVendors
   */
  @Valid 
  @Schema(name = "unlockedVendors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlockedVendors")
  public List<UUID> getUnlockedVendors() {
    return unlockedVendors;
  }

  public void setUnlockedVendors(List<UUID> unlockedVendors) {
    this.unlockedVendors = unlockedVendors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardPayload rewardPayload = (RewardPayload) o;
    return Objects.equals(this.rewards, rewardPayload.rewards) &&
        Objects.equals(this.economyProfile, rewardPayload.economyProfile) &&
        Objects.equals(this.deliveryMethod, rewardPayload.deliveryMethod) &&
        Objects.equals(this.unlockedVendors, rewardPayload.unlockedVendors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewards, economyProfile, deliveryMethod, unlockedVendors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardPayload {\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    economyProfile: ").append(toIndentedString(economyProfile)).append("\n");
    sb.append("    deliveryMethod: ").append(toIndentedString(deliveryMethod)).append("\n");
    sb.append("    unlockedVendors: ").append(toIndentedString(unlockedVendors)).append("\n");
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

