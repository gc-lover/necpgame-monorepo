package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReferralSettingsLimits
 */

@JsonTypeName("ReferralSettings_limits")

public class ReferralSettingsLimits {

  private @Nullable Integer maxUses;

  private @Nullable Integer dailyCap;

  private @Nullable Integer cooldownHours;

  public ReferralSettingsLimits maxUses(@Nullable Integer maxUses) {
    this.maxUses = maxUses;
    return this;
  }

  /**
   * Get maxUses
   * @return maxUses
   */
  
  @Schema(name = "maxUses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxUses")
  public @Nullable Integer getMaxUses() {
    return maxUses;
  }

  public void setMaxUses(@Nullable Integer maxUses) {
    this.maxUses = maxUses;
  }

  public ReferralSettingsLimits dailyCap(@Nullable Integer dailyCap) {
    this.dailyCap = dailyCap;
    return this;
  }

  /**
   * Get dailyCap
   * @return dailyCap
   */
  
  @Schema(name = "dailyCap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dailyCap")
  public @Nullable Integer getDailyCap() {
    return dailyCap;
  }

  public void setDailyCap(@Nullable Integer dailyCap) {
    this.dailyCap = dailyCap;
  }

  public ReferralSettingsLimits cooldownHours(@Nullable Integer cooldownHours) {
    this.cooldownHours = cooldownHours;
    return this;
  }

  /**
   * Get cooldownHours
   * @return cooldownHours
   */
  
  @Schema(name = "cooldownHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownHours")
  public @Nullable Integer getCooldownHours() {
    return cooldownHours;
  }

  public void setCooldownHours(@Nullable Integer cooldownHours) {
    this.cooldownHours = cooldownHours;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferralSettingsLimits referralSettingsLimits = (ReferralSettingsLimits) o;
    return Objects.equals(this.maxUses, referralSettingsLimits.maxUses) &&
        Objects.equals(this.dailyCap, referralSettingsLimits.dailyCap) &&
        Objects.equals(this.cooldownHours, referralSettingsLimits.cooldownHours);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxUses, dailyCap, cooldownHours);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferralSettingsLimits {\n");
    sb.append("    maxUses: ").append(toIndentedString(maxUses)).append("\n");
    sb.append("    dailyCap: ").append(toIndentedString(dailyCap)).append("\n");
    sb.append("    cooldownHours: ").append(toIndentedString(cooldownHours)).append("\n");
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

