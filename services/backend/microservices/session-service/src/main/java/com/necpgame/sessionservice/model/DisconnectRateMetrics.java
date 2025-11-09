package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.sessionservice.model.DisconnectRateMetricsByIspInner;
import com.necpgame.sessionservice.model.DisconnectRateMetricsByRegionInner;
import java.math.BigDecimal;
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
 * DisconnectRateMetrics
 */


public class DisconnectRateMetrics {

  private @Nullable String range;

  private @Nullable Integer totalDisconnects;

  private @Nullable BigDecimal disconnectPerMinute;

  @Valid
  private List<@Valid DisconnectRateMetricsByRegionInner> byRegion = new ArrayList<>();

  @Valid
  private List<@Valid DisconnectRateMetricsByIspInner> byIsp = new ArrayList<>();

  @Valid
  private List<String> thresholdBreaches = new ArrayList<>();

  public DisconnectRateMetrics range(@Nullable String range) {
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

  public DisconnectRateMetrics totalDisconnects(@Nullable Integer totalDisconnects) {
    this.totalDisconnects = totalDisconnects;
    return this;
  }

  /**
   * Get totalDisconnects
   * @return totalDisconnects
   */
  
  @Schema(name = "totalDisconnects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalDisconnects")
  public @Nullable Integer getTotalDisconnects() {
    return totalDisconnects;
  }

  public void setTotalDisconnects(@Nullable Integer totalDisconnects) {
    this.totalDisconnects = totalDisconnects;
  }

  public DisconnectRateMetrics disconnectPerMinute(@Nullable BigDecimal disconnectPerMinute) {
    this.disconnectPerMinute = disconnectPerMinute;
    return this;
  }

  /**
   * Get disconnectPerMinute
   * @return disconnectPerMinute
   */
  @Valid 
  @Schema(name = "disconnectPerMinute", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disconnectPerMinute")
  public @Nullable BigDecimal getDisconnectPerMinute() {
    return disconnectPerMinute;
  }

  public void setDisconnectPerMinute(@Nullable BigDecimal disconnectPerMinute) {
    this.disconnectPerMinute = disconnectPerMinute;
  }

  public DisconnectRateMetrics byRegion(List<@Valid DisconnectRateMetricsByRegionInner> byRegion) {
    this.byRegion = byRegion;
    return this;
  }

  public DisconnectRateMetrics addByRegionItem(DisconnectRateMetricsByRegionInner byRegionItem) {
    if (this.byRegion == null) {
      this.byRegion = new ArrayList<>();
    }
    this.byRegion.add(byRegionItem);
    return this;
  }

  /**
   * Get byRegion
   * @return byRegion
   */
  @Valid 
  @Schema(name = "byRegion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("byRegion")
  public List<@Valid DisconnectRateMetricsByRegionInner> getByRegion() {
    return byRegion;
  }

  public void setByRegion(List<@Valid DisconnectRateMetricsByRegionInner> byRegion) {
    this.byRegion = byRegion;
  }

  public DisconnectRateMetrics byIsp(List<@Valid DisconnectRateMetricsByIspInner> byIsp) {
    this.byIsp = byIsp;
    return this;
  }

  public DisconnectRateMetrics addByIspItem(DisconnectRateMetricsByIspInner byIspItem) {
    if (this.byIsp == null) {
      this.byIsp = new ArrayList<>();
    }
    this.byIsp.add(byIspItem);
    return this;
  }

  /**
   * Get byIsp
   * @return byIsp
   */
  @Valid 
  @Schema(name = "byIsp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("byIsp")
  public List<@Valid DisconnectRateMetricsByIspInner> getByIsp() {
    return byIsp;
  }

  public void setByIsp(List<@Valid DisconnectRateMetricsByIspInner> byIsp) {
    this.byIsp = byIsp;
  }

  public DisconnectRateMetrics thresholdBreaches(List<String> thresholdBreaches) {
    this.thresholdBreaches = thresholdBreaches;
    return this;
  }

  public DisconnectRateMetrics addThresholdBreachesItem(String thresholdBreachesItem) {
    if (this.thresholdBreaches == null) {
      this.thresholdBreaches = new ArrayList<>();
    }
    this.thresholdBreaches.add(thresholdBreachesItem);
    return this;
  }

  /**
   * Get thresholdBreaches
   * @return thresholdBreaches
   */
  
  @Schema(name = "thresholdBreaches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thresholdBreaches")
  public List<String> getThresholdBreaches() {
    return thresholdBreaches;
  }

  public void setThresholdBreaches(List<String> thresholdBreaches) {
    this.thresholdBreaches = thresholdBreaches;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisconnectRateMetrics disconnectRateMetrics = (DisconnectRateMetrics) o;
    return Objects.equals(this.range, disconnectRateMetrics.range) &&
        Objects.equals(this.totalDisconnects, disconnectRateMetrics.totalDisconnects) &&
        Objects.equals(this.disconnectPerMinute, disconnectRateMetrics.disconnectPerMinute) &&
        Objects.equals(this.byRegion, disconnectRateMetrics.byRegion) &&
        Objects.equals(this.byIsp, disconnectRateMetrics.byIsp) &&
        Objects.equals(this.thresholdBreaches, disconnectRateMetrics.thresholdBreaches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(range, totalDisconnects, disconnectPerMinute, byRegion, byIsp, thresholdBreaches);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisconnectRateMetrics {\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    totalDisconnects: ").append(toIndentedString(totalDisconnects)).append("\n");
    sb.append("    disconnectPerMinute: ").append(toIndentedString(disconnectPerMinute)).append("\n");
    sb.append("    byRegion: ").append(toIndentedString(byRegion)).append("\n");
    sb.append("    byIsp: ").append(toIndentedString(byIsp)).append("\n");
    sb.append("    thresholdBreaches: ").append(toIndentedString(thresholdBreaches)).append("\n");
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

