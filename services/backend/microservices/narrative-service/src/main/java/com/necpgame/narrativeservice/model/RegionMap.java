package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.RegionMapDataFortressesInner;
import com.necpgame.narrativeservice.model.RegionMapDistrictsInner;
import com.necpgame.narrativeservice.model.RegionMapQuestChainsInner;
import com.necpgame.narrativeservice.model.RegionMapZonesInner;
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
 * RegionMap
 */


public class RegionMap {

  /**
   * Gets or Sets regionName
   */
  public enum RegionNameEnum {
    NIGHT_CITY("night_city"),
    
    BADLANDS("badlands"),
    
    CYBERSPACE("cyberspace");

    private final String value;

    RegionNameEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RegionNameEnum fromValue(String value) {
      for (RegionNameEnum b : RegionNameEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RegionNameEnum regionName;

  @Valid
  private List<@Valid RegionMapDistrictsInner> districts = new ArrayList<>();

  @Valid
  private List<@Valid RegionMapZonesInner> zones = new ArrayList<>();

  @Valid
  private List<@Valid RegionMapDataFortressesInner> dataFortresses = new ArrayList<>();

  @Valid
  private List<@Valid RegionMapQuestChainsInner> questChains = new ArrayList<>();

  @Valid
  private List<String> connections = new ArrayList<>();

  public RegionMap() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegionMap(RegionNameEnum regionName) {
    this.regionName = regionName;
  }

  public RegionMap regionName(RegionNameEnum regionName) {
    this.regionName = regionName;
    return this;
  }

  /**
   * Get regionName
   * @return regionName
   */
  @NotNull 
  @Schema(name = "region_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region_name")
  public RegionNameEnum getRegionName() {
    return regionName;
  }

  public void setRegionName(RegionNameEnum regionName) {
    this.regionName = regionName;
  }

  public RegionMap districts(List<@Valid RegionMapDistrictsInner> districts) {
    this.districts = districts;
    return this;
  }

  public RegionMap addDistrictsItem(RegionMapDistrictsInner districtsItem) {
    if (this.districts == null) {
      this.districts = new ArrayList<>();
    }
    this.districts.add(districtsItem);
    return this;
  }

  /**
   * Для Night City
   * @return districts
   */
  @Valid 
  @Schema(name = "districts", description = "Для Night City", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("districts")
  public List<@Valid RegionMapDistrictsInner> getDistricts() {
    return districts;
  }

  public void setDistricts(List<@Valid RegionMapDistrictsInner> districts) {
    this.districts = districts;
  }

  public RegionMap zones(List<@Valid RegionMapZonesInner> zones) {
    this.zones = zones;
    return this;
  }

  public RegionMap addZonesItem(RegionMapZonesInner zonesItem) {
    if (this.zones == null) {
      this.zones = new ArrayList<>();
    }
    this.zones.add(zonesItem);
    return this;
  }

  /**
   * Для Badlands
   * @return zones
   */
  @Valid 
  @Schema(name = "zones", description = "Для Badlands", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zones")
  public List<@Valid RegionMapZonesInner> getZones() {
    return zones;
  }

  public void setZones(List<@Valid RegionMapZonesInner> zones) {
    this.zones = zones;
  }

  public RegionMap dataFortresses(List<@Valid RegionMapDataFortressesInner> dataFortresses) {
    this.dataFortresses = dataFortresses;
    return this;
  }

  public RegionMap addDataFortressesItem(RegionMapDataFortressesInner dataFortressesItem) {
    if (this.dataFortresses == null) {
      this.dataFortresses = new ArrayList<>();
    }
    this.dataFortresses.add(dataFortressesItem);
    return this;
  }

  /**
   * Для Cyberspace
   * @return dataFortresses
   */
  @Valid 
  @Schema(name = "data_fortresses", description = "Для Cyberspace", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data_fortresses")
  public List<@Valid RegionMapDataFortressesInner> getDataFortresses() {
    return dataFortresses;
  }

  public void setDataFortresses(List<@Valid RegionMapDataFortressesInner> dataFortresses) {
    this.dataFortresses = dataFortresses;
  }

  public RegionMap questChains(List<@Valid RegionMapQuestChainsInner> questChains) {
    this.questChains = questChains;
    return this;
  }

  public RegionMap addQuestChainsItem(RegionMapQuestChainsInner questChainsItem) {
    if (this.questChains == null) {
      this.questChains = new ArrayList<>();
    }
    this.questChains.add(questChainsItem);
    return this;
  }

  /**
   * Quest chains в регионе
   * @return questChains
   */
  @Valid 
  @Schema(name = "quest_chains", description = "Quest chains в регионе", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_chains")
  public List<@Valid RegionMapQuestChainsInner> getQuestChains() {
    return questChains;
  }

  public void setQuestChains(List<@Valid RegionMapQuestChainsInner> questChains) {
    this.questChains = questChains;
  }

  public RegionMap connections(List<String> connections) {
    this.connections = connections;
    return this;
  }

  public RegionMap addConnectionsItem(String connectionsItem) {
    if (this.connections == null) {
      this.connections = new ArrayList<>();
    }
    this.connections.add(connectionsItem);
    return this;
  }

  /**
   * Связи с другими регионами
   * @return connections
   */
  
  @Schema(name = "connections", description = "Связи с другими регионами", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connections")
  public List<String> getConnections() {
    return connections;
  }

  public void setConnections(List<String> connections) {
    this.connections = connections;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionMap regionMap = (RegionMap) o;
    return Objects.equals(this.regionName, regionMap.regionName) &&
        Objects.equals(this.districts, regionMap.districts) &&
        Objects.equals(this.zones, regionMap.zones) &&
        Objects.equals(this.dataFortresses, regionMap.dataFortresses) &&
        Objects.equals(this.questChains, regionMap.questChains) &&
        Objects.equals(this.connections, regionMap.connections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionName, districts, zones, dataFortresses, questChains, connections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionMap {\n");
    sb.append("    regionName: ").append(toIndentedString(regionName)).append("\n");
    sb.append("    districts: ").append(toIndentedString(districts)).append("\n");
    sb.append("    zones: ").append(toIndentedString(zones)).append("\n");
    sb.append("    dataFortresses: ").append(toIndentedString(dataFortresses)).append("\n");
    sb.append("    questChains: ").append(toIndentedString(questChains)).append("\n");
    sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
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

