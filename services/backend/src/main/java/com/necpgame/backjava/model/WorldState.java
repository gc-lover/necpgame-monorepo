package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WorldState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class WorldState {

  private @Nullable Integer currentYear;

  private @Nullable String currentEra;

  @Valid
  private List<String> activeGlobalEvents = new ArrayList<>();

  @Valid
  private Map<String, BigDecimal> factionPowerBalance = new HashMap<>();

  private @Nullable Object economicIndicators;

  public WorldState currentYear(@Nullable Integer currentYear) {
    this.currentYear = currentYear;
    return this;
  }

  /**
   * Get currentYear
   * @return currentYear
   */
  
  @Schema(name = "current_year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_year")
  public @Nullable Integer getCurrentYear() {
    return currentYear;
  }

  public void setCurrentYear(@Nullable Integer currentYear) {
    this.currentYear = currentYear;
  }

  public WorldState currentEra(@Nullable String currentEra) {
    this.currentEra = currentEra;
    return this;
  }

  /**
   * Get currentEra
   * @return currentEra
   */
  
  @Schema(name = "current_era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_era")
  public @Nullable String getCurrentEra() {
    return currentEra;
  }

  public void setCurrentEra(@Nullable String currentEra) {
    this.currentEra = currentEra;
  }

  public WorldState activeGlobalEvents(List<String> activeGlobalEvents) {
    this.activeGlobalEvents = activeGlobalEvents;
    return this;
  }

  public WorldState addActiveGlobalEventsItem(String activeGlobalEventsItem) {
    if (this.activeGlobalEvents == null) {
      this.activeGlobalEvents = new ArrayList<>();
    }
    this.activeGlobalEvents.add(activeGlobalEventsItem);
    return this;
  }

  /**
   * Get activeGlobalEvents
   * @return activeGlobalEvents
   */
  
  @Schema(name = "active_global_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_global_events")
  public List<String> getActiveGlobalEvents() {
    return activeGlobalEvents;
  }

  public void setActiveGlobalEvents(List<String> activeGlobalEvents) {
    this.activeGlobalEvents = activeGlobalEvents;
  }

  public WorldState factionPowerBalance(Map<String, BigDecimal> factionPowerBalance) {
    this.factionPowerBalance = factionPowerBalance;
    return this;
  }

  public WorldState putFactionPowerBalanceItem(String key, BigDecimal factionPowerBalanceItem) {
    if (this.factionPowerBalance == null) {
      this.factionPowerBalance = new HashMap<>();
    }
    this.factionPowerBalance.put(key, factionPowerBalanceItem);
    return this;
  }

  /**
   * Get factionPowerBalance
   * @return factionPowerBalance
   */
  @Valid 
  @Schema(name = "faction_power_balance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_power_balance")
  public Map<String, BigDecimal> getFactionPowerBalance() {
    return factionPowerBalance;
  }

  public void setFactionPowerBalance(Map<String, BigDecimal> factionPowerBalance) {
    this.factionPowerBalance = factionPowerBalance;
  }

  public WorldState economicIndicators(@Nullable Object economicIndicators) {
    this.economicIndicators = economicIndicators;
    return this;
  }

  /**
   * Get economicIndicators
   * @return economicIndicators
   */
  
  @Schema(name = "economic_indicators", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economic_indicators")
  public @Nullable Object getEconomicIndicators() {
    return economicIndicators;
  }

  public void setEconomicIndicators(@Nullable Object economicIndicators) {
    this.economicIndicators = economicIndicators;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldState worldState = (WorldState) o;
    return Objects.equals(this.currentYear, worldState.currentYear) &&
        Objects.equals(this.currentEra, worldState.currentEra) &&
        Objects.equals(this.activeGlobalEvents, worldState.activeGlobalEvents) &&
        Objects.equals(this.factionPowerBalance, worldState.factionPowerBalance) &&
        Objects.equals(this.economicIndicators, worldState.economicIndicators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentYear, currentEra, activeGlobalEvents, factionPowerBalance, economicIndicators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldState {\n");
    sb.append("    currentYear: ").append(toIndentedString(currentYear)).append("\n");
    sb.append("    currentEra: ").append(toIndentedString(currentEra)).append("\n");
    sb.append("    activeGlobalEvents: ").append(toIndentedString(activeGlobalEvents)).append("\n");
    sb.append("    factionPowerBalance: ").append(toIndentedString(factionPowerBalance)).append("\n");
    sb.append("    economicIndicators: ").append(toIndentedString(economicIndicators)).append("\n");
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

