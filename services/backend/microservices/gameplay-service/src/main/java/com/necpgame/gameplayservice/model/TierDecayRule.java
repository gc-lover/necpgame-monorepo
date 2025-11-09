package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Tier;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TierDecayRule
 */


public class TierDecayRule {

  private Tier tier;

  private Integer inactivityDays;

  private @Nullable Integer penaltyPerDay;

  public TierDecayRule() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TierDecayRule(Tier tier, Integer inactivityDays) {
    this.tier = tier;
    this.inactivityDays = inactivityDays;
  }

  public TierDecayRule tier(Tier tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * @return tier
   */
  @NotNull @Valid 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tier")
  public Tier getTier() {
    return tier;
  }

  public void setTier(Tier tier) {
    this.tier = tier;
  }

  public TierDecayRule inactivityDays(Integer inactivityDays) {
    this.inactivityDays = inactivityDays;
    return this;
  }

  /**
   * Get inactivityDays
   * @return inactivityDays
   */
  @NotNull 
  @Schema(name = "inactivityDays", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("inactivityDays")
  public Integer getInactivityDays() {
    return inactivityDays;
  }

  public void setInactivityDays(Integer inactivityDays) {
    this.inactivityDays = inactivityDays;
  }

  public TierDecayRule penaltyPerDay(@Nullable Integer penaltyPerDay) {
    this.penaltyPerDay = penaltyPerDay;
    return this;
  }

  /**
   * Get penaltyPerDay
   * @return penaltyPerDay
   */
  
  @Schema(name = "penaltyPerDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penaltyPerDay")
  public @Nullable Integer getPenaltyPerDay() {
    return penaltyPerDay;
  }

  public void setPenaltyPerDay(@Nullable Integer penaltyPerDay) {
    this.penaltyPerDay = penaltyPerDay;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TierDecayRule tierDecayRule = (TierDecayRule) o;
    return Objects.equals(this.tier, tierDecayRule.tier) &&
        Objects.equals(this.inactivityDays, tierDecayRule.inactivityDays) &&
        Objects.equals(this.penaltyPerDay, tierDecayRule.penaltyPerDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tier, inactivityDays, penaltyPerDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TierDecayRule {\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    inactivityDays: ").append(toIndentedString(inactivityDays)).append("\n");
    sb.append("    penaltyPerDay: ").append(toIndentedString(penaltyPerDay)).append("\n");
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

