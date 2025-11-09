package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * BoostStatus
 */


public class BoostStatus {

  private @Nullable String boostId;

  /**
   * Gets or Sets boostType
   */
  public enum BoostTypeEnum {
    XP("XP"),
    
    REWARD_RATE("REWARD_RATE"),
    
    DOUBLE_DROPS("DOUBLE_DROPS");

    private final String value;

    BoostTypeEnum(String value) {
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
    public static BoostTypeEnum fromValue(String value) {
      for (BoostTypeEnum b : BoostTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BoostTypeEnum boostType;

  private @Nullable BigDecimal multiplier;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime activatedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public BoostStatus boostId(@Nullable String boostId) {
    this.boostId = boostId;
    return this;
  }

  /**
   * Get boostId
   * @return boostId
   */
  
  @Schema(name = "boostId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostId")
  public @Nullable String getBoostId() {
    return boostId;
  }

  public void setBoostId(@Nullable String boostId) {
    this.boostId = boostId;
  }

  public BoostStatus boostType(@Nullable BoostTypeEnum boostType) {
    this.boostType = boostType;
    return this;
  }

  /**
   * Get boostType
   * @return boostType
   */
  
  @Schema(name = "boostType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostType")
  public @Nullable BoostTypeEnum getBoostType() {
    return boostType;
  }

  public void setBoostType(@Nullable BoostTypeEnum boostType) {
    this.boostType = boostType;
  }

  public BoostStatus multiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Get multiplier
   * @return multiplier
   */
  @Valid 
  @Schema(name = "multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public @Nullable BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  public BoostStatus activatedAt(@Nullable OffsetDateTime activatedAt) {
    this.activatedAt = activatedAt;
    return this;
  }

  /**
   * Get activatedAt
   * @return activatedAt
   */
  @Valid 
  @Schema(name = "activatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activatedAt")
  public @Nullable OffsetDateTime getActivatedAt() {
    return activatedAt;
  }

  public void setActivatedAt(@Nullable OffsetDateTime activatedAt) {
    this.activatedAt = activatedAt;
  }

  public BoostStatus expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoostStatus boostStatus = (BoostStatus) o;
    return Objects.equals(this.boostId, boostStatus.boostId) &&
        Objects.equals(this.boostType, boostStatus.boostType) &&
        Objects.equals(this.multiplier, boostStatus.multiplier) &&
        Objects.equals(this.activatedAt, boostStatus.activatedAt) &&
        Objects.equals(this.expiresAt, boostStatus.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boostId, boostType, multiplier, activatedAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoostStatus {\n");
    sb.append("    boostId: ").append(toIndentedString(boostId)).append("\n");
    sb.append("    boostType: ").append(toIndentedString(boostType)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
    sb.append("    activatedAt: ").append(toIndentedString(activatedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

