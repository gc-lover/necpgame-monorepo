package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.HashMap;
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
 * PlayerImpactImpactsByCategory
 */

@JsonTypeName("PlayerImpact_impacts_by_category")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerImpactImpactsByCategory {

  @Valid
  private Map<String, Integer> factionRelations = new HashMap<>();

  private @Nullable Integer worldEventsTriggered;

  private @Nullable Integer npcsAffected;

  private @Nullable BigDecimal economyImpact;

  private @Nullable Integer regionChanges;

  public PlayerImpactImpactsByCategory factionRelations(Map<String, Integer> factionRelations) {
    this.factionRelations = factionRelations;
    return this;
  }

  public PlayerImpactImpactsByCategory putFactionRelationsItem(String key, Integer factionRelationsItem) {
    if (this.factionRelations == null) {
      this.factionRelations = new HashMap<>();
    }
    this.factionRelations.put(key, factionRelationsItem);
    return this;
  }

  /**
   * Get factionRelations
   * @return factionRelations
   */
  
  @Schema(name = "faction_relations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_relations")
  public Map<String, Integer> getFactionRelations() {
    return factionRelations;
  }

  public void setFactionRelations(Map<String, Integer> factionRelations) {
    this.factionRelations = factionRelations;
  }

  public PlayerImpactImpactsByCategory worldEventsTriggered(@Nullable Integer worldEventsTriggered) {
    this.worldEventsTriggered = worldEventsTriggered;
    return this;
  }

  /**
   * Get worldEventsTriggered
   * @return worldEventsTriggered
   */
  
  @Schema(name = "world_events_triggered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world_events_triggered")
  public @Nullable Integer getWorldEventsTriggered() {
    return worldEventsTriggered;
  }

  public void setWorldEventsTriggered(@Nullable Integer worldEventsTriggered) {
    this.worldEventsTriggered = worldEventsTriggered;
  }

  public PlayerImpactImpactsByCategory npcsAffected(@Nullable Integer npcsAffected) {
    this.npcsAffected = npcsAffected;
    return this;
  }

  /**
   * Get npcsAffected
   * @return npcsAffected
   */
  
  @Schema(name = "npcs_affected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcs_affected")
  public @Nullable Integer getNpcsAffected() {
    return npcsAffected;
  }

  public void setNpcsAffected(@Nullable Integer npcsAffected) {
    this.npcsAffected = npcsAffected;
  }

  public PlayerImpactImpactsByCategory economyImpact(@Nullable BigDecimal economyImpact) {
    this.economyImpact = economyImpact;
    return this;
  }

  /**
   * Get economyImpact
   * @return economyImpact
   */
  @Valid 
  @Schema(name = "economy_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economy_impact")
  public @Nullable BigDecimal getEconomyImpact() {
    return economyImpact;
  }

  public void setEconomyImpact(@Nullable BigDecimal economyImpact) {
    this.economyImpact = economyImpact;
  }

  public PlayerImpactImpactsByCategory regionChanges(@Nullable Integer regionChanges) {
    this.regionChanges = regionChanges;
    return this;
  }

  /**
   * Get regionChanges
   * @return regionChanges
   */
  
  @Schema(name = "region_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region_changes")
  public @Nullable Integer getRegionChanges() {
    return regionChanges;
  }

  public void setRegionChanges(@Nullable Integer regionChanges) {
    this.regionChanges = regionChanges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerImpactImpactsByCategory playerImpactImpactsByCategory = (PlayerImpactImpactsByCategory) o;
    return Objects.equals(this.factionRelations, playerImpactImpactsByCategory.factionRelations) &&
        Objects.equals(this.worldEventsTriggered, playerImpactImpactsByCategory.worldEventsTriggered) &&
        Objects.equals(this.npcsAffected, playerImpactImpactsByCategory.npcsAffected) &&
        Objects.equals(this.economyImpact, playerImpactImpactsByCategory.economyImpact) &&
        Objects.equals(this.regionChanges, playerImpactImpactsByCategory.regionChanges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionRelations, worldEventsTriggered, npcsAffected, economyImpact, regionChanges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerImpactImpactsByCategory {\n");
    sb.append("    factionRelations: ").append(toIndentedString(factionRelations)).append("\n");
    sb.append("    worldEventsTriggered: ").append(toIndentedString(worldEventsTriggered)).append("\n");
    sb.append("    npcsAffected: ").append(toIndentedString(npcsAffected)).append("\n");
    sb.append("    economyImpact: ").append(toIndentedString(economyImpact)).append("\n");
    sb.append("    regionChanges: ").append(toIndentedString(regionChanges)).append("\n");
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

