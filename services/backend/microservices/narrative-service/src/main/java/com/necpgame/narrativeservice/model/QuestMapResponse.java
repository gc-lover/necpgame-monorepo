package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.QuestMapResponseFactionsInner;
import com.necpgame.narrativeservice.model.QuestMapResponseMapMetadata;
import com.necpgame.narrativeservice.model.QuestMapResponseRomanceQuestsInner;
import com.necpgame.narrativeservice.model.RegionMap;
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
 * QuestMapResponse
 */


public class QuestMapResponse {

  @Valid
  private List<@Valid RegionMap> regions = new ArrayList<>();

  @Valid
  private List<@Valid QuestMapResponseFactionsInner> factions = new ArrayList<>();

  @Valid
  private List<@Valid QuestMapResponseRomanceQuestsInner> romanceQuests = new ArrayList<>();

  @Valid
  private List<String> worldEvents = new ArrayList<>();

  private @Nullable Integer totalQuests;

  private @Nullable QuestMapResponseMapMetadata mapMetadata;

  public QuestMapResponse regions(List<@Valid RegionMap> regions) {
    this.regions = regions;
    return this;
  }

  public QuestMapResponse addRegionsItem(RegionMap regionsItem) {
    if (this.regions == null) {
      this.regions = new ArrayList<>();
    }
    this.regions.add(regionsItem);
    return this;
  }

  /**
   * Get regions
   * @return regions
   */
  @Valid 
  @Schema(name = "regions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regions")
  public List<@Valid RegionMap> getRegions() {
    return regions;
  }

  public void setRegions(List<@Valid RegionMap> regions) {
    this.regions = regions;
  }

  public QuestMapResponse factions(List<@Valid QuestMapResponseFactionsInner> factions) {
    this.factions = factions;
    return this;
  }

  public QuestMapResponse addFactionsItem(QuestMapResponseFactionsInner factionsItem) {
    if (this.factions == null) {
      this.factions = new ArrayList<>();
    }
    this.factions.add(factionsItem);
    return this;
  }

  /**
   * Faction quest chains
   * @return factions
   */
  @Valid 
  @Schema(name = "factions", description = "Faction quest chains", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factions")
  public List<@Valid QuestMapResponseFactionsInner> getFactions() {
    return factions;
  }

  public void setFactions(List<@Valid QuestMapResponseFactionsInner> factions) {
    this.factions = factions;
  }

  public QuestMapResponse romanceQuests(List<@Valid QuestMapResponseRomanceQuestsInner> romanceQuests) {
    this.romanceQuests = romanceQuests;
    return this;
  }

  public QuestMapResponse addRomanceQuestsItem(QuestMapResponseRomanceQuestsInner romanceQuestsItem) {
    if (this.romanceQuests == null) {
      this.romanceQuests = new ArrayList<>();
    }
    this.romanceQuests.add(romanceQuestsItem);
    return this;
  }

  /**
   * Romance quest chains
   * @return romanceQuests
   */
  @Valid 
  @Schema(name = "romance_quests", description = "Romance quest chains", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("romance_quests")
  public List<@Valid QuestMapResponseRomanceQuestsInner> getRomanceQuests() {
    return romanceQuests;
  }

  public void setRomanceQuests(List<@Valid QuestMapResponseRomanceQuestsInner> romanceQuests) {
    this.romanceQuests = romanceQuests;
  }

  public QuestMapResponse worldEvents(List<String> worldEvents) {
    this.worldEvents = worldEvents;
    return this;
  }

  public QuestMapResponse addWorldEventsItem(String worldEventsItem) {
    if (this.worldEvents == null) {
      this.worldEvents = new ArrayList<>();
    }
    this.worldEvents.add(worldEventsItem);
    return this;
  }

  /**
   * World events
   * @return worldEvents
   */
  
  @Schema(name = "world_events", description = "World events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world_events")
  public List<String> getWorldEvents() {
    return worldEvents;
  }

  public void setWorldEvents(List<String> worldEvents) {
    this.worldEvents = worldEvents;
  }

  public QuestMapResponse totalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
    return this;
  }

  /**
   * Общее количество квестов
   * @return totalQuests
   */
  
  @Schema(name = "total_quests", example = "357", description = "Общее количество квестов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_quests")
  public @Nullable Integer getTotalQuests() {
    return totalQuests;
  }

  public void setTotalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
  }

  public QuestMapResponse mapMetadata(@Nullable QuestMapResponseMapMetadata mapMetadata) {
    this.mapMetadata = mapMetadata;
    return this;
  }

  /**
   * Get mapMetadata
   * @return mapMetadata
   */
  @Valid 
  @Schema(name = "map_metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("map_metadata")
  public @Nullable QuestMapResponseMapMetadata getMapMetadata() {
    return mapMetadata;
  }

  public void setMapMetadata(@Nullable QuestMapResponseMapMetadata mapMetadata) {
    this.mapMetadata = mapMetadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestMapResponse questMapResponse = (QuestMapResponse) o;
    return Objects.equals(this.regions, questMapResponse.regions) &&
        Objects.equals(this.factions, questMapResponse.factions) &&
        Objects.equals(this.romanceQuests, questMapResponse.romanceQuests) &&
        Objects.equals(this.worldEvents, questMapResponse.worldEvents) &&
        Objects.equals(this.totalQuests, questMapResponse.totalQuests) &&
        Objects.equals(this.mapMetadata, questMapResponse.mapMetadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regions, factions, romanceQuests, worldEvents, totalQuests, mapMetadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestMapResponse {\n");
    sb.append("    regions: ").append(toIndentedString(regions)).append("\n");
    sb.append("    factions: ").append(toIndentedString(factions)).append("\n");
    sb.append("    romanceQuests: ").append(toIndentedString(romanceQuests)).append("\n");
    sb.append("    worldEvents: ").append(toIndentedString(worldEvents)).append("\n");
    sb.append("    totalQuests: ").append(toIndentedString(totalQuests)).append("\n");
    sb.append("    mapMetadata: ").append(toIndentedString(mapMetadata)).append("\n");
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

