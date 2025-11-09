package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * BondingActivityResponse
 */


public class BondingActivityResponse {

  private @Nullable Integer newBondingLevel;

  private @Nullable String newEmotionState;

  private @Nullable Integer xpGranted;

  private @Nullable Integer loyaltyDelta;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime cooldownUntil;

  public BondingActivityResponse newBondingLevel(@Nullable Integer newBondingLevel) {
    this.newBondingLevel = newBondingLevel;
    return this;
  }

  /**
   * Get newBondingLevel
   * @return newBondingLevel
   */
  
  @Schema(name = "newBondingLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newBondingLevel")
  public @Nullable Integer getNewBondingLevel() {
    return newBondingLevel;
  }

  public void setNewBondingLevel(@Nullable Integer newBondingLevel) {
    this.newBondingLevel = newBondingLevel;
  }

  public BondingActivityResponse newEmotionState(@Nullable String newEmotionState) {
    this.newEmotionState = newEmotionState;
    return this;
  }

  /**
   * Get newEmotionState
   * @return newEmotionState
   */
  
  @Schema(name = "newEmotionState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newEmotionState")
  public @Nullable String getNewEmotionState() {
    return newEmotionState;
  }

  public void setNewEmotionState(@Nullable String newEmotionState) {
    this.newEmotionState = newEmotionState;
  }

  public BondingActivityResponse xpGranted(@Nullable Integer xpGranted) {
    this.xpGranted = xpGranted;
    return this;
  }

  /**
   * Get xpGranted
   * @return xpGranted
   */
  
  @Schema(name = "xpGranted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("xpGranted")
  public @Nullable Integer getXpGranted() {
    return xpGranted;
  }

  public void setXpGranted(@Nullable Integer xpGranted) {
    this.xpGranted = xpGranted;
  }

  public BondingActivityResponse loyaltyDelta(@Nullable Integer loyaltyDelta) {
    this.loyaltyDelta = loyaltyDelta;
    return this;
  }

  /**
   * Get loyaltyDelta
   * @return loyaltyDelta
   */
  
  @Schema(name = "loyaltyDelta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loyaltyDelta")
  public @Nullable Integer getLoyaltyDelta() {
    return loyaltyDelta;
  }

  public void setLoyaltyDelta(@Nullable Integer loyaltyDelta) {
    this.loyaltyDelta = loyaltyDelta;
  }

  public BondingActivityResponse cooldownUntil(@Nullable OffsetDateTime cooldownUntil) {
    this.cooldownUntil = cooldownUntil;
    return this;
  }

  /**
   * Get cooldownUntil
   * @return cooldownUntil
   */
  @Valid 
  @Schema(name = "cooldownUntil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownUntil")
  public @Nullable OffsetDateTime getCooldownUntil() {
    return cooldownUntil;
  }

  public void setCooldownUntil(@Nullable OffsetDateTime cooldownUntil) {
    this.cooldownUntil = cooldownUntil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BondingActivityResponse bondingActivityResponse = (BondingActivityResponse) o;
    return Objects.equals(this.newBondingLevel, bondingActivityResponse.newBondingLevel) &&
        Objects.equals(this.newEmotionState, bondingActivityResponse.newEmotionState) &&
        Objects.equals(this.xpGranted, bondingActivityResponse.xpGranted) &&
        Objects.equals(this.loyaltyDelta, bondingActivityResponse.loyaltyDelta) &&
        Objects.equals(this.cooldownUntil, bondingActivityResponse.cooldownUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newBondingLevel, newEmotionState, xpGranted, loyaltyDelta, cooldownUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BondingActivityResponse {\n");
    sb.append("    newBondingLevel: ").append(toIndentedString(newBondingLevel)).append("\n");
    sb.append("    newEmotionState: ").append(toIndentedString(newEmotionState)).append("\n");
    sb.append("    xpGranted: ").append(toIndentedString(xpGranted)).append("\n");
    sb.append("    loyaltyDelta: ").append(toIndentedString(loyaltyDelta)).append("\n");
    sb.append("    cooldownUntil: ").append(toIndentedString(cooldownUntil)).append("\n");
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

