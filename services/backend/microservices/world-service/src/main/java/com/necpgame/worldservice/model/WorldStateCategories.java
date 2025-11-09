package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Состояние по категориям
 */

@Schema(name = "WorldState_categories", description = "Состояние по категориям")
@JsonTypeName("WorldState_categories")

public class WorldStateCategories {

  private @Nullable Object territoryControl;

  private @Nullable Object factionPower;

  private @Nullable Object economicState;

  private @Nullable Object technologyLevel;

  private @Nullable Object socialStructure;

  private @Nullable Object questProgress;

  private @Nullable Object environmental;

  public WorldStateCategories territoryControl(@Nullable Object territoryControl) {
    this.territoryControl = territoryControl;
    return this;
  }

  /**
   * Get territoryControl
   * @return territoryControl
   */
  
  @Schema(name = "territory_control", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territory_control")
  public @Nullable Object getTerritoryControl() {
    return territoryControl;
  }

  public void setTerritoryControl(@Nullable Object territoryControl) {
    this.territoryControl = territoryControl;
  }

  public WorldStateCategories factionPower(@Nullable Object factionPower) {
    this.factionPower = factionPower;
    return this;
  }

  /**
   * Get factionPower
   * @return factionPower
   */
  
  @Schema(name = "faction_power", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_power")
  public @Nullable Object getFactionPower() {
    return factionPower;
  }

  public void setFactionPower(@Nullable Object factionPower) {
    this.factionPower = factionPower;
  }

  public WorldStateCategories economicState(@Nullable Object economicState) {
    this.economicState = economicState;
    return this;
  }

  /**
   * Get economicState
   * @return economicState
   */
  
  @Schema(name = "economic_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economic_state")
  public @Nullable Object getEconomicState() {
    return economicState;
  }

  public void setEconomicState(@Nullable Object economicState) {
    this.economicState = economicState;
  }

  public WorldStateCategories technologyLevel(@Nullable Object technologyLevel) {
    this.technologyLevel = technologyLevel;
    return this;
  }

  /**
   * Get technologyLevel
   * @return technologyLevel
   */
  
  @Schema(name = "technology_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technology_level")
  public @Nullable Object getTechnologyLevel() {
    return technologyLevel;
  }

  public void setTechnologyLevel(@Nullable Object technologyLevel) {
    this.technologyLevel = technologyLevel;
  }

  public WorldStateCategories socialStructure(@Nullable Object socialStructure) {
    this.socialStructure = socialStructure;
    return this;
  }

  /**
   * Get socialStructure
   * @return socialStructure
   */
  
  @Schema(name = "social_structure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social_structure")
  public @Nullable Object getSocialStructure() {
    return socialStructure;
  }

  public void setSocialStructure(@Nullable Object socialStructure) {
    this.socialStructure = socialStructure;
  }

  public WorldStateCategories questProgress(@Nullable Object questProgress) {
    this.questProgress = questProgress;
    return this;
  }

  /**
   * Get questProgress
   * @return questProgress
   */
  
  @Schema(name = "quest_progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_progress")
  public @Nullable Object getQuestProgress() {
    return questProgress;
  }

  public void setQuestProgress(@Nullable Object questProgress) {
    this.questProgress = questProgress;
  }

  public WorldStateCategories environmental(@Nullable Object environmental) {
    this.environmental = environmental;
    return this;
  }

  /**
   * Get environmental
   * @return environmental
   */
  
  @Schema(name = "environmental", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("environmental")
  public @Nullable Object getEnvironmental() {
    return environmental;
  }

  public void setEnvironmental(@Nullable Object environmental) {
    this.environmental = environmental;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldStateCategories worldStateCategories = (WorldStateCategories) o;
    return Objects.equals(this.territoryControl, worldStateCategories.territoryControl) &&
        Objects.equals(this.factionPower, worldStateCategories.factionPower) &&
        Objects.equals(this.economicState, worldStateCategories.economicState) &&
        Objects.equals(this.technologyLevel, worldStateCategories.technologyLevel) &&
        Objects.equals(this.socialStructure, worldStateCategories.socialStructure) &&
        Objects.equals(this.questProgress, worldStateCategories.questProgress) &&
        Objects.equals(this.environmental, worldStateCategories.environmental);
  }

  @Override
  public int hashCode() {
    return Objects.hash(territoryControl, factionPower, economicState, technologyLevel, socialStructure, questProgress, environmental);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldStateCategories {\n");
    sb.append("    territoryControl: ").append(toIndentedString(territoryControl)).append("\n");
    sb.append("    factionPower: ").append(toIndentedString(factionPower)).append("\n");
    sb.append("    economicState: ").append(toIndentedString(economicState)).append("\n");
    sb.append("    technologyLevel: ").append(toIndentedString(technologyLevel)).append("\n");
    sb.append("    socialStructure: ").append(toIndentedString(socialStructure)).append("\n");
    sb.append("    questProgress: ").append(toIndentedString(questProgress)).append("\n");
    sb.append("    environmental: ").append(toIndentedString(environmental)).append("\n");
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

