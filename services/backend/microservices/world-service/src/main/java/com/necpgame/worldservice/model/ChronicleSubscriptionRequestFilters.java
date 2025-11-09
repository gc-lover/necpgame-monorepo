package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChronicleSubscriptionRequestFilters
 */

@JsonTypeName("ChronicleSubscriptionRequest_filters")

public class ChronicleSubscriptionRequestFilters {

  @Valid
  private List<UUID> regions = new ArrayList<>();

  @Valid
  private List<String> eventTypes = new ArrayList<>();

  @Valid
  private List<UUID> routeIds = new ArrayList<>();

  @Valid
  private List<UUID> factionIds = new ArrayList<>();

  public ChronicleSubscriptionRequestFilters regions(List<UUID> regions) {
    this.regions = regions;
    return this;
  }

  public ChronicleSubscriptionRequestFilters addRegionsItem(UUID regionsItem) {
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
  public List<UUID> getRegions() {
    return regions;
  }

  public void setRegions(List<UUID> regions) {
    this.regions = regions;
  }

  public ChronicleSubscriptionRequestFilters eventTypes(List<String> eventTypes) {
    this.eventTypes = eventTypes;
    return this;
  }

  public ChronicleSubscriptionRequestFilters addEventTypesItem(String eventTypesItem) {
    if (this.eventTypes == null) {
      this.eventTypes = new ArrayList<>();
    }
    this.eventTypes.add(eventTypesItem);
    return this;
  }

  /**
   * Get eventTypes
   * @return eventTypes
   */
  
  @Schema(name = "eventTypes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventTypes")
  public List<String> getEventTypes() {
    return eventTypes;
  }

  public void setEventTypes(List<String> eventTypes) {
    this.eventTypes = eventTypes;
  }

  public ChronicleSubscriptionRequestFilters routeIds(List<UUID> routeIds) {
    this.routeIds = routeIds;
    return this;
  }

  public ChronicleSubscriptionRequestFilters addRouteIdsItem(UUID routeIdsItem) {
    if (this.routeIds == null) {
      this.routeIds = new ArrayList<>();
    }
    this.routeIds.add(routeIdsItem);
    return this;
  }

  /**
   * Get routeIds
   * @return routeIds
   */
  @Valid 
  @Schema(name = "routeIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeIds")
  public List<UUID> getRouteIds() {
    return routeIds;
  }

  public void setRouteIds(List<UUID> routeIds) {
    this.routeIds = routeIds;
  }

  public ChronicleSubscriptionRequestFilters factionIds(List<UUID> factionIds) {
    this.factionIds = factionIds;
    return this;
  }

  public ChronicleSubscriptionRequestFilters addFactionIdsItem(UUID factionIdsItem) {
    if (this.factionIds == null) {
      this.factionIds = new ArrayList<>();
    }
    this.factionIds.add(factionIdsItem);
    return this;
  }

  /**
   * Get factionIds
   * @return factionIds
   */
  @Valid 
  @Schema(name = "factionIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionIds")
  public List<UUID> getFactionIds() {
    return factionIds;
  }

  public void setFactionIds(List<UUID> factionIds) {
    this.factionIds = factionIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChronicleSubscriptionRequestFilters chronicleSubscriptionRequestFilters = (ChronicleSubscriptionRequestFilters) o;
    return Objects.equals(this.regions, chronicleSubscriptionRequestFilters.regions) &&
        Objects.equals(this.eventTypes, chronicleSubscriptionRequestFilters.eventTypes) &&
        Objects.equals(this.routeIds, chronicleSubscriptionRequestFilters.routeIds) &&
        Objects.equals(this.factionIds, chronicleSubscriptionRequestFilters.factionIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regions, eventTypes, routeIds, factionIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleSubscriptionRequestFilters {\n");
    sb.append("    regions: ").append(toIndentedString(regions)).append("\n");
    sb.append("    eventTypes: ").append(toIndentedString(eventTypes)).append("\n");
    sb.append("    routeIds: ").append(toIndentedString(routeIds)).append("\n");
    sb.append("    factionIds: ").append(toIndentedString(factionIds)).append("\n");
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

