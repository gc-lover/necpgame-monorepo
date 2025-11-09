package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ReferralSettingsLimits;
import com.necpgame.socialservice.model.ReferralSettingsReferrerRewardsInner;
import com.necpgame.socialservice.model.ReferralSettingsWelcomeBonus;
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
 * ReferralSettings
 */


public class ReferralSettings {

  private @Nullable ReferralSettingsWelcomeBonus welcomeBonus;

  @Valid
  private List<@Valid ReferralSettingsReferrerRewardsInner> referrerRewards = new ArrayList<>();

  private @Nullable ReferralSettingsLimits limits;

  public ReferralSettings welcomeBonus(@Nullable ReferralSettingsWelcomeBonus welcomeBonus) {
    this.welcomeBonus = welcomeBonus;
    return this;
  }

  /**
   * Get welcomeBonus
   * @return welcomeBonus
   */
  @Valid 
  @Schema(name = "welcomeBonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("welcomeBonus")
  public @Nullable ReferralSettingsWelcomeBonus getWelcomeBonus() {
    return welcomeBonus;
  }

  public void setWelcomeBonus(@Nullable ReferralSettingsWelcomeBonus welcomeBonus) {
    this.welcomeBonus = welcomeBonus;
  }

  public ReferralSettings referrerRewards(List<@Valid ReferralSettingsReferrerRewardsInner> referrerRewards) {
    this.referrerRewards = referrerRewards;
    return this;
  }

  public ReferralSettings addReferrerRewardsItem(ReferralSettingsReferrerRewardsInner referrerRewardsItem) {
    if (this.referrerRewards == null) {
      this.referrerRewards = new ArrayList<>();
    }
    this.referrerRewards.add(referrerRewardsItem);
    return this;
  }

  /**
   * Get referrerRewards
   * @return referrerRewards
   */
  @Valid 
  @Schema(name = "referrerRewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referrerRewards")
  public List<@Valid ReferralSettingsReferrerRewardsInner> getReferrerRewards() {
    return referrerRewards;
  }

  public void setReferrerRewards(List<@Valid ReferralSettingsReferrerRewardsInner> referrerRewards) {
    this.referrerRewards = referrerRewards;
  }

  public ReferralSettings limits(@Nullable ReferralSettingsLimits limits) {
    this.limits = limits;
    return this;
  }

  /**
   * Get limits
   * @return limits
   */
  @Valid 
  @Schema(name = "limits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limits")
  public @Nullable ReferralSettingsLimits getLimits() {
    return limits;
  }

  public void setLimits(@Nullable ReferralSettingsLimits limits) {
    this.limits = limits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferralSettings referralSettings = (ReferralSettings) o;
    return Objects.equals(this.welcomeBonus, referralSettings.welcomeBonus) &&
        Objects.equals(this.referrerRewards, referralSettings.referrerRewards) &&
        Objects.equals(this.limits, referralSettings.limits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(welcomeBonus, referrerRewards, limits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferralSettings {\n");
    sb.append("    welcomeBonus: ").append(toIndentedString(welcomeBonus)).append("\n");
    sb.append("    referrerRewards: ").append(toIndentedString(referrerRewards)).append("\n");
    sb.append("    limits: ").append(toIndentedString(limits)).append("\n");
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

