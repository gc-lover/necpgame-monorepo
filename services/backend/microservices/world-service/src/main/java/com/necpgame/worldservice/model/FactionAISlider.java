package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * FactionAISlider
 */


public class FactionAISlider {

  private @Nullable String faction;

  private @Nullable Float influence;

  private @Nullable Float aggression;

  private @Nullable Float wealth;

  private @Nullable Float technology;

  @Valid
  private Map<String, Integer> relations = new HashMap<>();

  public FactionAISlider faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public FactionAISlider influence(@Nullable Float influence) {
    this.influence = influence;
    return this;
  }

  /**
   * Влияние в мире (0-100)
   * minimum: 0
   * maximum: 100
   * @return influence
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "influence", description = "Влияние в мире (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("influence")
  public @Nullable Float getInfluence() {
    return influence;
  }

  public void setInfluence(@Nullable Float influence) {
    this.influence = influence;
  }

  public FactionAISlider aggression(@Nullable Float aggression) {
    this.aggression = aggression;
    return this;
  }

  /**
   * Агрессивность (0-10)
   * minimum: 0
   * maximum: 10
   * @return aggression
   */
  @DecimalMin(value = "0") @DecimalMax(value = "10") 
  @Schema(name = "aggression", description = "Агрессивность (0-10)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aggression")
  public @Nullable Float getAggression() {
    return aggression;
  }

  public void setAggression(@Nullable Float aggression) {
    this.aggression = aggression;
  }

  public FactionAISlider wealth(@Nullable Float wealth) {
    this.wealth = wealth;
    return this;
  }

  /**
   * Экономическая мощь
   * @return wealth
   */
  
  @Schema(name = "wealth", description = "Экономическая мощь", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wealth")
  public @Nullable Float getWealth() {
    return wealth;
  }

  public void setWealth(@Nullable Float wealth) {
    this.wealth = wealth;
  }

  public FactionAISlider technology(@Nullable Float technology) {
    this.technology = technology;
    return this;
  }

  /**
   * Технологический уровень
   * minimum: 1
   * maximum: 10
   * @return technology
   */
  @DecimalMin(value = "1") @DecimalMax(value = "10") 
  @Schema(name = "technology", description = "Технологический уровень", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technology")
  public @Nullable Float getTechnology() {
    return technology;
  }

  public void setTechnology(@Nullable Float technology) {
    this.technology = technology;
  }

  public FactionAISlider relations(Map<String, Integer> relations) {
    this.relations = relations;
    return this;
  }

  public FactionAISlider putRelationsItem(String key, Integer relationsItem) {
    if (this.relations == null) {
      this.relations = new HashMap<>();
    }
    this.relations.put(key, relationsItem);
    return this;
  }

  /**
   * Отношения с другими фракциями (-100 to 100)
   * @return relations
   */
  
  @Schema(name = "relations", description = "Отношения с другими фракциями (-100 to 100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relations")
  public Map<String, Integer> getRelations() {
    return relations;
  }

  public void setRelations(Map<String, Integer> relations) {
    this.relations = relations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionAISlider factionAISlider = (FactionAISlider) o;
    return Objects.equals(this.faction, factionAISlider.faction) &&
        Objects.equals(this.influence, factionAISlider.influence) &&
        Objects.equals(this.aggression, factionAISlider.aggression) &&
        Objects.equals(this.wealth, factionAISlider.wealth) &&
        Objects.equals(this.technology, factionAISlider.technology) &&
        Objects.equals(this.relations, factionAISlider.relations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(faction, influence, aggression, wealth, technology, relations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionAISlider {\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    influence: ").append(toIndentedString(influence)).append("\n");
    sb.append("    aggression: ").append(toIndentedString(aggression)).append("\n");
    sb.append("    wealth: ").append(toIndentedString(wealth)).append("\n");
    sb.append("    technology: ").append(toIndentedString(technology)).append("\n");
    sb.append("    relations: ").append(toIndentedString(relations)).append("\n");
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

