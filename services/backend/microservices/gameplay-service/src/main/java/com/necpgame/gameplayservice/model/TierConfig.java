package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.TierDecayRule;
import com.necpgame.gameplayservice.model.TierDefinition;
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
 * TierConfig
 */


public class TierConfig {

  @Valid
  private List<@Valid TierDefinition> tiers = new ArrayList<>();

  private @Nullable Integer placementGames;

  @Valid
  private List<@Valid TierDecayRule> decayRules = new ArrayList<>();

  public TierConfig() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TierConfig(List<@Valid TierDefinition> tiers) {
    this.tiers = tiers;
  }

  public TierConfig tiers(List<@Valid TierDefinition> tiers) {
    this.tiers = tiers;
    return this;
  }

  public TierConfig addTiersItem(TierDefinition tiersItem) {
    if (this.tiers == null) {
      this.tiers = new ArrayList<>();
    }
    this.tiers.add(tiersItem);
    return this;
  }

  /**
   * Get tiers
   * @return tiers
   */
  @NotNull @Valid 
  @Schema(name = "tiers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tiers")
  public List<@Valid TierDefinition> getTiers() {
    return tiers;
  }

  public void setTiers(List<@Valid TierDefinition> tiers) {
    this.tiers = tiers;
  }

  public TierConfig placementGames(@Nullable Integer placementGames) {
    this.placementGames = placementGames;
    return this;
  }

  /**
   * Get placementGames
   * @return placementGames
   */
  
  @Schema(name = "placementGames", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placementGames")
  public @Nullable Integer getPlacementGames() {
    return placementGames;
  }

  public void setPlacementGames(@Nullable Integer placementGames) {
    this.placementGames = placementGames;
  }

  public TierConfig decayRules(List<@Valid TierDecayRule> decayRules) {
    this.decayRules = decayRules;
    return this;
  }

  public TierConfig addDecayRulesItem(TierDecayRule decayRulesItem) {
    if (this.decayRules == null) {
      this.decayRules = new ArrayList<>();
    }
    this.decayRules.add(decayRulesItem);
    return this;
  }

  /**
   * Get decayRules
   * @return decayRules
   */
  @Valid 
  @Schema(name = "decayRules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decayRules")
  public List<@Valid TierDecayRule> getDecayRules() {
    return decayRules;
  }

  public void setDecayRules(List<@Valid TierDecayRule> decayRules) {
    this.decayRules = decayRules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TierConfig tierConfig = (TierConfig) o;
    return Objects.equals(this.tiers, tierConfig.tiers) &&
        Objects.equals(this.placementGames, tierConfig.placementGames) &&
        Objects.equals(this.decayRules, tierConfig.decayRules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tiers, placementGames, decayRules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TierConfig {\n");
    sb.append("    tiers: ").append(toIndentedString(tiers)).append("\n");
    sb.append("    placementGames: ").append(toIndentedString(placementGames)).append("\n");
    sb.append("    decayRules: ").append(toIndentedString(decayRules)).append("\n");
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

