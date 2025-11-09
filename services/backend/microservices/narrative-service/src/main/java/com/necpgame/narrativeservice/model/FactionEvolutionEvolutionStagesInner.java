package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * FactionEvolutionEvolutionStagesInner
 */

@JsonTypeName("FactionEvolution_evolution_stages_inner")

public class FactionEvolutionEvolutionStagesInner {

  private @Nullable Integer year;

  private @Nullable Integer powerLevel;

  @Valid
  private List<String> territory = new ArrayList<>();

  @Valid
  private List<String> keyEvents = new ArrayList<>();

  public FactionEvolutionEvolutionStagesInner year(@Nullable Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public @Nullable Integer getYear() {
    return year;
  }

  public void setYear(@Nullable Integer year) {
    this.year = year;
  }

  public FactionEvolutionEvolutionStagesInner powerLevel(@Nullable Integer powerLevel) {
    this.powerLevel = powerLevel;
    return this;
  }

  /**
   * Get powerLevel
   * @return powerLevel
   */
  
  @Schema(name = "power_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("power_level")
  public @Nullable Integer getPowerLevel() {
    return powerLevel;
  }

  public void setPowerLevel(@Nullable Integer powerLevel) {
    this.powerLevel = powerLevel;
  }

  public FactionEvolutionEvolutionStagesInner territory(List<String> territory) {
    this.territory = territory;
    return this;
  }

  public FactionEvolutionEvolutionStagesInner addTerritoryItem(String territoryItem) {
    if (this.territory == null) {
      this.territory = new ArrayList<>();
    }
    this.territory.add(territoryItem);
    return this;
  }

  /**
   * Get territory
   * @return territory
   */
  
  @Schema(name = "territory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territory")
  public List<String> getTerritory() {
    return territory;
  }

  public void setTerritory(List<String> territory) {
    this.territory = territory;
  }

  public FactionEvolutionEvolutionStagesInner keyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
    return this;
  }

  public FactionEvolutionEvolutionStagesInner addKeyEventsItem(String keyEventsItem) {
    if (this.keyEvents == null) {
      this.keyEvents = new ArrayList<>();
    }
    this.keyEvents.add(keyEventsItem);
    return this;
  }

  /**
   * Get keyEvents
   * @return keyEvents
   */
  
  @Schema(name = "key_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_events")
  public List<String> getKeyEvents() {
    return keyEvents;
  }

  public void setKeyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionEvolutionEvolutionStagesInner factionEvolutionEvolutionStagesInner = (FactionEvolutionEvolutionStagesInner) o;
    return Objects.equals(this.year, factionEvolutionEvolutionStagesInner.year) &&
        Objects.equals(this.powerLevel, factionEvolutionEvolutionStagesInner.powerLevel) &&
        Objects.equals(this.territory, factionEvolutionEvolutionStagesInner.territory) &&
        Objects.equals(this.keyEvents, factionEvolutionEvolutionStagesInner.keyEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, powerLevel, territory, keyEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionEvolutionEvolutionStagesInner {\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    powerLevel: ").append(toIndentedString(powerLevel)).append("\n");
    sb.append("    territory: ").append(toIndentedString(territory)).append("\n");
    sb.append("    keyEvents: ").append(toIndentedString(keyEvents)).append("\n");
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

