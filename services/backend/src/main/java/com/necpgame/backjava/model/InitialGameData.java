package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.InitialGameDataNpcsInner;
import com.necpgame.backjava.model.InitialGameDataStarterItemsInner;
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
 * InitialGameData
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class InitialGameData {

  @Valid
  private List<@Valid InitialGameDataStarterItemsInner> starterItems = new ArrayList<>();

  @Valid
  private List<String> starterQuests = new ArrayList<>();

  @Valid
  private List<String> starterLocations = new ArrayList<>();

  @Valid
  private List<@Valid InitialGameDataNpcsInner> npcs = new ArrayList<>();

  @Valid
  private List<Object> factions = new ArrayList<>();

  public InitialGameData starterItems(List<@Valid InitialGameDataStarterItemsInner> starterItems) {
    this.starterItems = starterItems;
    return this;
  }

  public InitialGameData addStarterItemsItem(InitialGameDataStarterItemsInner starterItemsItem) {
    if (this.starterItems == null) {
      this.starterItems = new ArrayList<>();
    }
    this.starterItems.add(starterItemsItem);
    return this;
  }

  /**
   * Стартовые предметы для новых персонажей
   * @return starterItems
   */
  @Valid 
  @Schema(name = "starter_items", description = "Стартовые предметы для новых персонажей", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starter_items")
  public List<@Valid InitialGameDataStarterItemsInner> getStarterItems() {
    return starterItems;
  }

  public void setStarterItems(List<@Valid InitialGameDataStarterItemsInner> starterItems) {
    this.starterItems = starterItems;
  }

  public InitialGameData starterQuests(List<String> starterQuests) {
    this.starterQuests = starterQuests;
    return this;
  }

  public InitialGameData addStarterQuestsItem(String starterQuestsItem) {
    if (this.starterQuests == null) {
      this.starterQuests = new ArrayList<>();
    }
    this.starterQuests.add(starterQuestsItem);
    return this;
  }

  /**
   * Первые квесты
   * @return starterQuests
   */
  
  @Schema(name = "starter_quests", description = "Первые квесты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starter_quests")
  public List<String> getStarterQuests() {
    return starterQuests;
  }

  public void setStarterQuests(List<String> starterQuests) {
    this.starterQuests = starterQuests;
  }

  public InitialGameData starterLocations(List<String> starterLocations) {
    this.starterLocations = starterLocations;
    return this;
  }

  public InitialGameData addStarterLocationsItem(String starterLocationsItem) {
    if (this.starterLocations == null) {
      this.starterLocations = new ArrayList<>();
    }
    this.starterLocations.add(starterLocationsItem);
    return this;
  }

  /**
   * Доступные стартовые локации
   * @return starterLocations
   */
  
  @Schema(name = "starter_locations", description = "Доступные стартовые локации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starter_locations")
  public List<String> getStarterLocations() {
    return starterLocations;
  }

  public void setStarterLocations(List<String> starterLocations) {
    this.starterLocations = starterLocations;
  }

  public InitialGameData npcs(List<@Valid InitialGameDataNpcsInner> npcs) {
    this.npcs = npcs;
    return this;
  }

  public InitialGameData addNpcsItem(InitialGameDataNpcsInner npcsItem) {
    if (this.npcs == null) {
      this.npcs = new ArrayList<>();
    }
    this.npcs.add(npcsItem);
    return this;
  }

  /**
   * Ключевые NPC для начала
   * @return npcs
   */
  @Valid 
  @Schema(name = "npcs", description = "Ключевые NPC для начала", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcs")
  public List<@Valid InitialGameDataNpcsInner> getNpcs() {
    return npcs;
  }

  public void setNpcs(List<@Valid InitialGameDataNpcsInner> npcs) {
    this.npcs = npcs;
  }

  public InitialGameData factions(List<Object> factions) {
    this.factions = factions;
    return this;
  }

  public InitialGameData addFactionsItem(Object factionsItem) {
    if (this.factions == null) {
      this.factions = new ArrayList<>();
    }
    this.factions.add(factionsItem);
    return this;
  }

  /**
   * Фракции доступные с начала
   * @return factions
   */
  
  @Schema(name = "factions", description = "Фракции доступные с начала", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factions")
  public List<Object> getFactions() {
    return factions;
  }

  public void setFactions(List<Object> factions) {
    this.factions = factions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitialGameData initialGameData = (InitialGameData) o;
    return Objects.equals(this.starterItems, initialGameData.starterItems) &&
        Objects.equals(this.starterQuests, initialGameData.starterQuests) &&
        Objects.equals(this.starterLocations, initialGameData.starterLocations) &&
        Objects.equals(this.npcs, initialGameData.npcs) &&
        Objects.equals(this.factions, initialGameData.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(starterItems, starterQuests, starterLocations, npcs, factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitialGameData {\n");
    sb.append("    starterItems: ").append(toIndentedString(starterItems)).append("\n");
    sb.append("    starterQuests: ").append(toIndentedString(starterQuests)).append("\n");
    sb.append("    starterLocations: ").append(toIndentedString(starterLocations)).append("\n");
    sb.append("    npcs: ").append(toIndentedString(npcs)).append("\n");
    sb.append("    factions: ").append(toIndentedString(factions)).append("\n");
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

