package com.necpgame.adminservice.model;

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
 * FactionState
 */


public class FactionState {

  private @Nullable String factionId;

  private @Nullable BigDecimal powerLevel;

  @Valid
  private List<String> controlledTerritories = new ArrayList<>();

  @Valid
  private Map<String, Integer> relations = new HashMap<>();

  private @Nullable Integer activeOperations;

  public FactionState factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public FactionState powerLevel(@Nullable BigDecimal powerLevel) {
    this.powerLevel = powerLevel;
    return this;
  }

  /**
   * Get powerLevel
   * @return powerLevel
   */
  @Valid 
  @Schema(name = "power_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("power_level")
  public @Nullable BigDecimal getPowerLevel() {
    return powerLevel;
  }

  public void setPowerLevel(@Nullable BigDecimal powerLevel) {
    this.powerLevel = powerLevel;
  }

  public FactionState controlledTerritories(List<String> controlledTerritories) {
    this.controlledTerritories = controlledTerritories;
    return this;
  }

  public FactionState addControlledTerritoriesItem(String controlledTerritoriesItem) {
    if (this.controlledTerritories == null) {
      this.controlledTerritories = new ArrayList<>();
    }
    this.controlledTerritories.add(controlledTerritoriesItem);
    return this;
  }

  /**
   * Get controlledTerritories
   * @return controlledTerritories
   */
  
  @Schema(name = "controlled_territories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlled_territories")
  public List<String> getControlledTerritories() {
    return controlledTerritories;
  }

  public void setControlledTerritories(List<String> controlledTerritories) {
    this.controlledTerritories = controlledTerritories;
  }

  public FactionState relations(Map<String, Integer> relations) {
    this.relations = relations;
    return this;
  }

  public FactionState putRelationsItem(String key, Integer relationsItem) {
    if (this.relations == null) {
      this.relations = new HashMap<>();
    }
    this.relations.put(key, relationsItem);
    return this;
  }

  /**
   * Get relations
   * @return relations
   */
  
  @Schema(name = "relations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relations")
  public Map<String, Integer> getRelations() {
    return relations;
  }

  public void setRelations(Map<String, Integer> relations) {
    this.relations = relations;
  }

  public FactionState activeOperations(@Nullable Integer activeOperations) {
    this.activeOperations = activeOperations;
    return this;
  }

  /**
   * Get activeOperations
   * @return activeOperations
   */
  
  @Schema(name = "active_operations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_operations")
  public @Nullable Integer getActiveOperations() {
    return activeOperations;
  }

  public void setActiveOperations(@Nullable Integer activeOperations) {
    this.activeOperations = activeOperations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionState factionState = (FactionState) o;
    return Objects.equals(this.factionId, factionState.factionId) &&
        Objects.equals(this.powerLevel, factionState.powerLevel) &&
        Objects.equals(this.controlledTerritories, factionState.controlledTerritories) &&
        Objects.equals(this.relations, factionState.relations) &&
        Objects.equals(this.activeOperations, factionState.activeOperations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, powerLevel, controlledTerritories, relations, activeOperations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionState {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    powerLevel: ").append(toIndentedString(powerLevel)).append("\n");
    sb.append("    controlledTerritories: ").append(toIndentedString(controlledTerritories)).append("\n");
    sb.append("    relations: ").append(toIndentedString(relations)).append("\n");
    sb.append("    activeOperations: ").append(toIndentedString(activeOperations)).append("\n");
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

