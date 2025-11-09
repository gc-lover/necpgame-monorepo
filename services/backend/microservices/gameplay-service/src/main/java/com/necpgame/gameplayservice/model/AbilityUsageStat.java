package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AbilityUsageStat
 */


public class AbilityUsageStat {

  private @Nullable String abilityId;

  private @Nullable Integer uses;

  private @Nullable BigDecimal winContribution;

  private @Nullable BigDecimal averageCooldown;

  public AbilityUsageStat abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "abilityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilityId")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  public AbilityUsageStat uses(@Nullable Integer uses) {
    this.uses = uses;
    return this;
  }

  /**
   * Get uses
   * @return uses
   */
  
  @Schema(name = "uses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uses")
  public @Nullable Integer getUses() {
    return uses;
  }

  public void setUses(@Nullable Integer uses) {
    this.uses = uses;
  }

  public AbilityUsageStat winContribution(@Nullable BigDecimal winContribution) {
    this.winContribution = winContribution;
    return this;
  }

  /**
   * Get winContribution
   * @return winContribution
   */
  @Valid 
  @Schema(name = "winContribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winContribution")
  public @Nullable BigDecimal getWinContribution() {
    return winContribution;
  }

  public void setWinContribution(@Nullable BigDecimal winContribution) {
    this.winContribution = winContribution;
  }

  public AbilityUsageStat averageCooldown(@Nullable BigDecimal averageCooldown) {
    this.averageCooldown = averageCooldown;
    return this;
  }

  /**
   * Get averageCooldown
   * @return averageCooldown
   */
  @Valid 
  @Schema(name = "averageCooldown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageCooldown")
  public @Nullable BigDecimal getAverageCooldown() {
    return averageCooldown;
  }

  public void setAverageCooldown(@Nullable BigDecimal averageCooldown) {
    this.averageCooldown = averageCooldown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityUsageStat abilityUsageStat = (AbilityUsageStat) o;
    return Objects.equals(this.abilityId, abilityUsageStat.abilityId) &&
        Objects.equals(this.uses, abilityUsageStat.uses) &&
        Objects.equals(this.winContribution, abilityUsageStat.winContribution) &&
        Objects.equals(this.averageCooldown, abilityUsageStat.averageCooldown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilityId, uses, winContribution, averageCooldown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityUsageStat {\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    uses: ").append(toIndentedString(uses)).append("\n");
    sb.append("    winContribution: ").append(toIndentedString(winContribution)).append("\n");
    sb.append("    averageCooldown: ").append(toIndentedString(averageCooldown)).append("\n");
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

