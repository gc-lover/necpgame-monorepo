package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.HousingAnalyticsResponsePopularLocationsInner;
import com.necpgame.gameplayservice.model.HousingAnalyticsResponseTopPrestigeApartmentsInner;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HousingAnalyticsResponse
 */


public class HousingAnalyticsResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  private @Nullable String range;

  private @Nullable BigDecimal visitsPerDay;

  @Valid
  private List<@Valid HousingAnalyticsResponsePopularLocationsInner> popularLocations = new ArrayList<>();

  @Valid
  private List<@Valid HousingAnalyticsResponseTopPrestigeApartmentsInner> topPrestigeApartments = new ArrayList<>();

  private @Nullable BigDecimal dwellTime;

  private @Nullable Integer housingRevenue;

  public HousingAnalyticsResponse generatedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
   */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public @Nullable OffsetDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  public HousingAnalyticsResponse range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public HousingAnalyticsResponse visitsPerDay(@Nullable BigDecimal visitsPerDay) {
    this.visitsPerDay = visitsPerDay;
    return this;
  }

  /**
   * Get visitsPerDay
   * @return visitsPerDay
   */
  @Valid 
  @Schema(name = "visitsPerDay", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visitsPerDay")
  public @Nullable BigDecimal getVisitsPerDay() {
    return visitsPerDay;
  }

  public void setVisitsPerDay(@Nullable BigDecimal visitsPerDay) {
    this.visitsPerDay = visitsPerDay;
  }

  public HousingAnalyticsResponse popularLocations(List<@Valid HousingAnalyticsResponsePopularLocationsInner> popularLocations) {
    this.popularLocations = popularLocations;
    return this;
  }

  public HousingAnalyticsResponse addPopularLocationsItem(HousingAnalyticsResponsePopularLocationsInner popularLocationsItem) {
    if (this.popularLocations == null) {
      this.popularLocations = new ArrayList<>();
    }
    this.popularLocations.add(popularLocationsItem);
    return this;
  }

  /**
   * Get popularLocations
   * @return popularLocations
   */
  @Valid 
  @Schema(name = "popularLocations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("popularLocations")
  public List<@Valid HousingAnalyticsResponsePopularLocationsInner> getPopularLocations() {
    return popularLocations;
  }

  public void setPopularLocations(List<@Valid HousingAnalyticsResponsePopularLocationsInner> popularLocations) {
    this.popularLocations = popularLocations;
  }

  public HousingAnalyticsResponse topPrestigeApartments(List<@Valid HousingAnalyticsResponseTopPrestigeApartmentsInner> topPrestigeApartments) {
    this.topPrestigeApartments = topPrestigeApartments;
    return this;
  }

  public HousingAnalyticsResponse addTopPrestigeApartmentsItem(HousingAnalyticsResponseTopPrestigeApartmentsInner topPrestigeApartmentsItem) {
    if (this.topPrestigeApartments == null) {
      this.topPrestigeApartments = new ArrayList<>();
    }
    this.topPrestigeApartments.add(topPrestigeApartmentsItem);
    return this;
  }

  /**
   * Get topPrestigeApartments
   * @return topPrestigeApartments
   */
  @Valid 
  @Schema(name = "topPrestigeApartments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("topPrestigeApartments")
  public List<@Valid HousingAnalyticsResponseTopPrestigeApartmentsInner> getTopPrestigeApartments() {
    return topPrestigeApartments;
  }

  public void setTopPrestigeApartments(List<@Valid HousingAnalyticsResponseTopPrestigeApartmentsInner> topPrestigeApartments) {
    this.topPrestigeApartments = topPrestigeApartments;
  }

  public HousingAnalyticsResponse dwellTime(@Nullable BigDecimal dwellTime) {
    this.dwellTime = dwellTime;
    return this;
  }

  /**
   * Get dwellTime
   * @return dwellTime
   */
  @Valid 
  @Schema(name = "dwellTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dwellTime")
  public @Nullable BigDecimal getDwellTime() {
    return dwellTime;
  }

  public void setDwellTime(@Nullable BigDecimal dwellTime) {
    this.dwellTime = dwellTime;
  }

  public HousingAnalyticsResponse housingRevenue(@Nullable Integer housingRevenue) {
    this.housingRevenue = housingRevenue;
    return this;
  }

  /**
   * Get housingRevenue
   * @return housingRevenue
   */
  
  @Schema(name = "housingRevenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("housingRevenue")
  public @Nullable Integer getHousingRevenue() {
    return housingRevenue;
  }

  public void setHousingRevenue(@Nullable Integer housingRevenue) {
    this.housingRevenue = housingRevenue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HousingAnalyticsResponse housingAnalyticsResponse = (HousingAnalyticsResponse) o;
    return Objects.equals(this.generatedAt, housingAnalyticsResponse.generatedAt) &&
        Objects.equals(this.range, housingAnalyticsResponse.range) &&
        Objects.equals(this.visitsPerDay, housingAnalyticsResponse.visitsPerDay) &&
        Objects.equals(this.popularLocations, housingAnalyticsResponse.popularLocations) &&
        Objects.equals(this.topPrestigeApartments, housingAnalyticsResponse.topPrestigeApartments) &&
        Objects.equals(this.dwellTime, housingAnalyticsResponse.dwellTime) &&
        Objects.equals(this.housingRevenue, housingAnalyticsResponse.housingRevenue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedAt, range, visitsPerDay, popularLocations, topPrestigeApartments, dwellTime, housingRevenue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HousingAnalyticsResponse {\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    visitsPerDay: ").append(toIndentedString(visitsPerDay)).append("\n");
    sb.append("    popularLocations: ").append(toIndentedString(popularLocations)).append("\n");
    sb.append("    topPrestigeApartments: ").append(toIndentedString(topPrestigeApartments)).append("\n");
    sb.append("    dwellTime: ").append(toIndentedString(dwellTime)).append("\n");
    sb.append("    housingRevenue: ").append(toIndentedString(housingRevenue)).append("\n");
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

