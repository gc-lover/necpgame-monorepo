package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * InsuranceAppliedEvent
 */


public class InsuranceAppliedEvent {

  private UUID orderId;

  /**
   * Gets or Sets tier
   */
  public enum TierEnum {
    BASIC("basic"),
    
    EXTENDED("extended"),
    
    PREMIUM("premium");

    private final String value;

    TierEnum(String value) {
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
    public static TierEnum fromValue(String value) {
      for (TierEnum b : TierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TierEnum tier;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime appliedAt;

  private @Nullable Float premium;

  private @Nullable UUID actorId;

  public InsuranceAppliedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InsuranceAppliedEvent(UUID orderId, TierEnum tier, OffsetDateTime appliedAt) {
    this.orderId = orderId;
    this.tier = tier;
    this.appliedAt = appliedAt;
  }

  public InsuranceAppliedEvent orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public InsuranceAppliedEvent tier(TierEnum tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  @NotNull 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tier")
  public TierEnum getTier() {
    return tier;
  }

  public void setTier(TierEnum tier) {
    this.tier = tier;
  }

  public InsuranceAppliedEvent appliedAt(OffsetDateTime appliedAt) {
    this.appliedAt = appliedAt;
    return this;
  }

  /**
   * Get appliedAt
   * @return appliedAt
   */
  @NotNull @Valid 
  @Schema(name = "appliedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appliedAt")
  public OffsetDateTime getAppliedAt() {
    return appliedAt;
  }

  public void setAppliedAt(OffsetDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public InsuranceAppliedEvent premium(@Nullable Float premium) {
    this.premium = premium;
    return this;
  }

  /**
   * Get premium
   * @return premium
   */
  
  @Schema(name = "premium", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premium")
  public @Nullable Float getPremium() {
    return premium;
  }

  public void setPremium(@Nullable Float premium) {
    this.premium = premium;
  }

  public InsuranceAppliedEvent actorId(@Nullable UUID actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  @Valid 
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable UUID getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable UUID actorId) {
    this.actorId = actorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsuranceAppliedEvent insuranceAppliedEvent = (InsuranceAppliedEvent) o;
    return Objects.equals(this.orderId, insuranceAppliedEvent.orderId) &&
        Objects.equals(this.tier, insuranceAppliedEvent.tier) &&
        Objects.equals(this.appliedAt, insuranceAppliedEvent.appliedAt) &&
        Objects.equals(this.premium, insuranceAppliedEvent.premium) &&
        Objects.equals(this.actorId, insuranceAppliedEvent.actorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, tier, appliedAt, premium, actorId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceAppliedEvent {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    appliedAt: ").append(toIndentedString(appliedAt)).append("\n");
    sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
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

