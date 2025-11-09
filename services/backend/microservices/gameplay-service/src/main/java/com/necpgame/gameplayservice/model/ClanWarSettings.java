package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ClanWarSettings
 */


public class ClanWarSettings {

  private @Nullable Integer maxConcurrentWars;

  private @Nullable Integer warCostBase;

  private @Nullable Integer preparationWindowHours;

  private @Nullable Integer territoryImmunityHours;

  private @Nullable Integer allyLimit;

  private @Nullable Integer broadcastCooldownMinutes;

  public ClanWarSettings maxConcurrentWars(@Nullable Integer maxConcurrentWars) {
    this.maxConcurrentWars = maxConcurrentWars;
    return this;
  }

  /**
   * Get maxConcurrentWars
   * @return maxConcurrentWars
   */
  
  @Schema(name = "maxConcurrentWars", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxConcurrentWars")
  public @Nullable Integer getMaxConcurrentWars() {
    return maxConcurrentWars;
  }

  public void setMaxConcurrentWars(@Nullable Integer maxConcurrentWars) {
    this.maxConcurrentWars = maxConcurrentWars;
  }

  public ClanWarSettings warCostBase(@Nullable Integer warCostBase) {
    this.warCostBase = warCostBase;
    return this;
  }

  /**
   * Get warCostBase
   * @return warCostBase
   */
  
  @Schema(name = "warCostBase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warCostBase")
  public @Nullable Integer getWarCostBase() {
    return warCostBase;
  }

  public void setWarCostBase(@Nullable Integer warCostBase) {
    this.warCostBase = warCostBase;
  }

  public ClanWarSettings preparationWindowHours(@Nullable Integer preparationWindowHours) {
    this.preparationWindowHours = preparationWindowHours;
    return this;
  }

  /**
   * Get preparationWindowHours
   * @return preparationWindowHours
   */
  
  @Schema(name = "preparationWindowHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preparationWindowHours")
  public @Nullable Integer getPreparationWindowHours() {
    return preparationWindowHours;
  }

  public void setPreparationWindowHours(@Nullable Integer preparationWindowHours) {
    this.preparationWindowHours = preparationWindowHours;
  }

  public ClanWarSettings territoryImmunityHours(@Nullable Integer territoryImmunityHours) {
    this.territoryImmunityHours = territoryImmunityHours;
    return this;
  }

  /**
   * Get territoryImmunityHours
   * @return territoryImmunityHours
   */
  
  @Schema(name = "territoryImmunityHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territoryImmunityHours")
  public @Nullable Integer getTerritoryImmunityHours() {
    return territoryImmunityHours;
  }

  public void setTerritoryImmunityHours(@Nullable Integer territoryImmunityHours) {
    this.territoryImmunityHours = territoryImmunityHours;
  }

  public ClanWarSettings allyLimit(@Nullable Integer allyLimit) {
    this.allyLimit = allyLimit;
    return this;
  }

  /**
   * Get allyLimit
   * @return allyLimit
   */
  
  @Schema(name = "allyLimit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allyLimit")
  public @Nullable Integer getAllyLimit() {
    return allyLimit;
  }

  public void setAllyLimit(@Nullable Integer allyLimit) {
    this.allyLimit = allyLimit;
  }

  public ClanWarSettings broadcastCooldownMinutes(@Nullable Integer broadcastCooldownMinutes) {
    this.broadcastCooldownMinutes = broadcastCooldownMinutes;
    return this;
  }

  /**
   * Get broadcastCooldownMinutes
   * @return broadcastCooldownMinutes
   */
  
  @Schema(name = "broadcastCooldownMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("broadcastCooldownMinutes")
  public @Nullable Integer getBroadcastCooldownMinutes() {
    return broadcastCooldownMinutes;
  }

  public void setBroadcastCooldownMinutes(@Nullable Integer broadcastCooldownMinutes) {
    this.broadcastCooldownMinutes = broadcastCooldownMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClanWarSettings clanWarSettings = (ClanWarSettings) o;
    return Objects.equals(this.maxConcurrentWars, clanWarSettings.maxConcurrentWars) &&
        Objects.equals(this.warCostBase, clanWarSettings.warCostBase) &&
        Objects.equals(this.preparationWindowHours, clanWarSettings.preparationWindowHours) &&
        Objects.equals(this.territoryImmunityHours, clanWarSettings.territoryImmunityHours) &&
        Objects.equals(this.allyLimit, clanWarSettings.allyLimit) &&
        Objects.equals(this.broadcastCooldownMinutes, clanWarSettings.broadcastCooldownMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxConcurrentWars, warCostBase, preparationWindowHours, territoryImmunityHours, allyLimit, broadcastCooldownMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClanWarSettings {\n");
    sb.append("    maxConcurrentWars: ").append(toIndentedString(maxConcurrentWars)).append("\n");
    sb.append("    warCostBase: ").append(toIndentedString(warCostBase)).append("\n");
    sb.append("    preparationWindowHours: ").append(toIndentedString(preparationWindowHours)).append("\n");
    sb.append("    territoryImmunityHours: ").append(toIndentedString(territoryImmunityHours)).append("\n");
    sb.append("    allyLimit: ").append(toIndentedString(allyLimit)).append("\n");
    sb.append("    broadcastCooldownMinutes: ").append(toIndentedString(broadcastCooldownMinutes)).append("\n");
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

