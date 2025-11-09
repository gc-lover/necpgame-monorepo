package com.necpgame.worldservice.model;

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
 * RewardDescriptor
 */


public class RewardDescriptor {

  private @Nullable String rewardId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CURRENCY("CURRENCY"),
    
    ITEM("ITEM"),
    
    TITLE("TITLE"),
    
    BADGE("BADGE"),
    
    ACCESS("ACCESS");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer quantity;

  /**
   * Gets or Sets delivery
   */
  public enum DeliveryEnum {
    AUTO("AUTO"),
    
    CLAIM("CLAIM"),
    
    MAIL("MAIL");

    private final String value;

    DeliveryEnum(String value) {
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
    public static DeliveryEnum fromValue(String value) {
      for (DeliveryEnum b : DeliveryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DeliveryEnum delivery;

  public RewardDescriptor rewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardId")
  public @Nullable String getRewardId() {
    return rewardId;
  }

  public void setRewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
  }

  public RewardDescriptor type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public RewardDescriptor quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * @return quantity
   */
  @Min(value = 1) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public RewardDescriptor delivery(@Nullable DeliveryEnum delivery) {
    this.delivery = delivery;
    return this;
  }

  /**
   * Get delivery
   * @return delivery
   */
  
  @Schema(name = "delivery", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delivery")
  public @Nullable DeliveryEnum getDelivery() {
    return delivery;
  }

  public void setDelivery(@Nullable DeliveryEnum delivery) {
    this.delivery = delivery;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardDescriptor rewardDescriptor = (RewardDescriptor) o;
    return Objects.equals(this.rewardId, rewardDescriptor.rewardId) &&
        Objects.equals(this.type, rewardDescriptor.type) &&
        Objects.equals(this.quantity, rewardDescriptor.quantity) &&
        Objects.equals(this.delivery, rewardDescriptor.delivery);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardId, type, quantity, delivery);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardDescriptor {\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    delivery: ").append(toIndentedString(delivery)).append("\n");
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

