package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WarReward
 */


public class WarReward {

  /**
   * Gets or Sets rewardType
   */
  public enum RewardTypeEnum {
    CURRENCY("currency"),
    
    ITEM("item"),
    
    BUFF("buff"),
    
    TITLE("title"),
    
    COSMETIC("cosmetic");

    private final String value;

    RewardTypeEnum(String value) {
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
    public static RewardTypeEnum fromValue(String value) {
      for (RewardTypeEnum b : RewardTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RewardTypeEnum rewardType;

  private @Nullable Integer amount;

  private @Nullable String itemId;

  private @Nullable Integer durationHours;

  public WarReward() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WarReward(RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
  }

  public WarReward rewardType(RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
    return this;
  }

  /**
   * Get rewardType
   * @return rewardType
   */
  @NotNull 
  @Schema(name = "rewardType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewardType")
  public RewardTypeEnum getRewardType() {
    return rewardType;
  }

  public void setRewardType(RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
  }

  public WarReward amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public WarReward itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public WarReward durationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Get durationHours
   * @return durationHours
   */
  
  @Schema(name = "durationHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationHours")
  public @Nullable Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarReward warReward = (WarReward) o;
    return Objects.equals(this.rewardType, warReward.rewardType) &&
        Objects.equals(this.amount, warReward.amount) &&
        Objects.equals(this.itemId, warReward.itemId) &&
        Objects.equals(this.durationHours, warReward.durationHours);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardType, amount, itemId, durationHours);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarReward {\n");
    sb.append("    rewardType: ").append(toIndentedString(rewardType)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
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

