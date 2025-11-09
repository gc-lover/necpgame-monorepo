package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardGrant
 */


public class RewardGrant {

  private String rewardId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CURRENCY("currency"),
    
    ITEM("item"),
    
    TITLE("title"),
    
    COSMETIC("cosmetic");

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

  private TypeEnum type;

  private @Nullable Integer amount;

  private @Nullable String grantedTo;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime grantedAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    GRANTED("GRANTED"),
    
    FAILED("FAILED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  public RewardGrant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RewardGrant(String rewardId, TypeEnum type, OffsetDateTime grantedAt) {
    this.rewardId = rewardId;
    this.type = type;
    this.grantedAt = grantedAt;
  }

  public RewardGrant rewardId(String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  @NotNull 
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewardId")
  public String getRewardId() {
    return rewardId;
  }

  public void setRewardId(String rewardId) {
    this.rewardId = rewardId;
  }

  public RewardGrant type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public RewardGrant amount(@Nullable Integer amount) {
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

  public RewardGrant grantedTo(@Nullable String grantedTo) {
    this.grantedTo = grantedTo;
    return this;
  }

  /**
   * Get grantedTo
   * @return grantedTo
   */
  
  @Schema(name = "grantedTo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grantedTo")
  public @Nullable String getGrantedTo() {
    return grantedTo;
  }

  public void setGrantedTo(@Nullable String grantedTo) {
    this.grantedTo = grantedTo;
  }

  public RewardGrant grantedAt(OffsetDateTime grantedAt) {
    this.grantedAt = grantedAt;
    return this;
  }

  /**
   * Get grantedAt
   * @return grantedAt
   */
  @NotNull @Valid 
  @Schema(name = "grantedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("grantedAt")
  public OffsetDateTime getGrantedAt() {
    return grantedAt;
  }

  public void setGrantedAt(OffsetDateTime grantedAt) {
    this.grantedAt = grantedAt;
  }

  public RewardGrant status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardGrant rewardGrant = (RewardGrant) o;
    return Objects.equals(this.rewardId, rewardGrant.rewardId) &&
        Objects.equals(this.type, rewardGrant.type) &&
        Objects.equals(this.amount, rewardGrant.amount) &&
        Objects.equals(this.grantedTo, rewardGrant.grantedTo) &&
        Objects.equals(this.grantedAt, rewardGrant.grantedAt) &&
        Objects.equals(this.status, rewardGrant.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardId, type, amount, grantedTo, grantedAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardGrant {\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    grantedTo: ").append(toIndentedString(grantedTo)).append("\n");
    sb.append("    grantedAt: ").append(toIndentedString(grantedAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

