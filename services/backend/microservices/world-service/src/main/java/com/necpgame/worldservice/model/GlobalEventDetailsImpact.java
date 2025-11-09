package com.necpgame.worldservice.model;

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
 * Влияние на игровой мир
 */

@Schema(name = "GlobalEventDetails_impact", description = "Влияние на игровой мир")
@JsonTypeName("GlobalEventDetails_impact")

public class GlobalEventDetailsImpact {

  @Valid
  private List<Object> globalModifiers = new ArrayList<>();

  private @Nullable Object regionalDifferences;

  private @Nullable Object factionReactions;

  public GlobalEventDetailsImpact globalModifiers(List<Object> globalModifiers) {
    this.globalModifiers = globalModifiers;
    return this;
  }

  public GlobalEventDetailsImpact addGlobalModifiersItem(Object globalModifiersItem) {
    if (this.globalModifiers == null) {
      this.globalModifiers = new ArrayList<>();
    }
    this.globalModifiers.add(globalModifiersItem);
    return this;
  }

  /**
   * Глобальные модификаторы
   * @return globalModifiers
   */
  
  @Schema(name = "global_modifiers", description = "Глобальные модификаторы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("global_modifiers")
  public List<Object> getGlobalModifiers() {
    return globalModifiers;
  }

  public void setGlobalModifiers(List<Object> globalModifiers) {
    this.globalModifiers = globalModifiers;
  }

  public GlobalEventDetailsImpact regionalDifferences(@Nullable Object regionalDifferences) {
    this.regionalDifferences = regionalDifferences;
    return this;
  }

  /**
   * Региональные различия влияния
   * @return regionalDifferences
   */
  
  @Schema(name = "regional_differences", description = "Региональные различия влияния", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regional_differences")
  public @Nullable Object getRegionalDifferences() {
    return regionalDifferences;
  }

  public void setRegionalDifferences(@Nullable Object regionalDifferences) {
    this.regionalDifferences = regionalDifferences;
  }

  public GlobalEventDetailsImpact factionReactions(@Nullable Object factionReactions) {
    this.factionReactions = factionReactions;
    return this;
  }

  /**
   * Реакции фракций на событие
   * @return factionReactions
   */
  
  @Schema(name = "faction_reactions", description = "Реакции фракций на событие", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_reactions")
  public @Nullable Object getFactionReactions() {
    return factionReactions;
  }

  public void setFactionReactions(@Nullable Object factionReactions) {
    this.factionReactions = factionReactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalEventDetailsImpact globalEventDetailsImpact = (GlobalEventDetailsImpact) o;
    return Objects.equals(this.globalModifiers, globalEventDetailsImpact.globalModifiers) &&
        Objects.equals(this.regionalDifferences, globalEventDetailsImpact.regionalDifferences) &&
        Objects.equals(this.factionReactions, globalEventDetailsImpact.factionReactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(globalModifiers, regionalDifferences, factionReactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GlobalEventDetailsImpact {\n");
    sb.append("    globalModifiers: ").append(toIndentedString(globalModifiers)).append("\n");
    sb.append("    regionalDifferences: ").append(toIndentedString(regionalDifferences)).append("\n");
    sb.append("    factionReactions: ").append(toIndentedString(factionReactions)).append("\n");
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

