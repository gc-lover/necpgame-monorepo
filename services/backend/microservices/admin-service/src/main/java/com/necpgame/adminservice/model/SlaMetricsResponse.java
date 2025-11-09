package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.SlaMetricsResponseBreachesInner;
import com.necpgame.adminservice.model.SlaMetricsResponseByPriorityInner;
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
 * SlaMetricsResponse
 */


public class SlaMetricsResponse {

  private @Nullable String range;

  private @Nullable BigDecimal overallCompliancePercent;

  @Valid
  private List<@Valid SlaMetricsResponseByPriorityInner> byPriority = new ArrayList<>();

  @Valid
  private List<@Valid SlaMetricsResponseBreachesInner> breaches = new ArrayList<>();

  public SlaMetricsResponse range(@Nullable String range) {
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

  public SlaMetricsResponse overallCompliancePercent(@Nullable BigDecimal overallCompliancePercent) {
    this.overallCompliancePercent = overallCompliancePercent;
    return this;
  }

  /**
   * Get overallCompliancePercent
   * @return overallCompliancePercent
   */
  @Valid 
  @Schema(name = "overallCompliancePercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overallCompliancePercent")
  public @Nullable BigDecimal getOverallCompliancePercent() {
    return overallCompliancePercent;
  }

  public void setOverallCompliancePercent(@Nullable BigDecimal overallCompliancePercent) {
    this.overallCompliancePercent = overallCompliancePercent;
  }

  public SlaMetricsResponse byPriority(List<@Valid SlaMetricsResponseByPriorityInner> byPriority) {
    this.byPriority = byPriority;
    return this;
  }

  public SlaMetricsResponse addByPriorityItem(SlaMetricsResponseByPriorityInner byPriorityItem) {
    if (this.byPriority == null) {
      this.byPriority = new ArrayList<>();
    }
    this.byPriority.add(byPriorityItem);
    return this;
  }

  /**
   * Get byPriority
   * @return byPriority
   */
  @Valid 
  @Schema(name = "byPriority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("byPriority")
  public List<@Valid SlaMetricsResponseByPriorityInner> getByPriority() {
    return byPriority;
  }

  public void setByPriority(List<@Valid SlaMetricsResponseByPriorityInner> byPriority) {
    this.byPriority = byPriority;
  }

  public SlaMetricsResponse breaches(List<@Valid SlaMetricsResponseBreachesInner> breaches) {
    this.breaches = breaches;
    return this;
  }

  public SlaMetricsResponse addBreachesItem(SlaMetricsResponseBreachesInner breachesItem) {
    if (this.breaches == null) {
      this.breaches = new ArrayList<>();
    }
    this.breaches.add(breachesItem);
    return this;
  }

  /**
   * Get breaches
   * @return breaches
   */
  @Valid 
  @Schema(name = "breaches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breaches")
  public List<@Valid SlaMetricsResponseBreachesInner> getBreaches() {
    return breaches;
  }

  public void setBreaches(List<@Valid SlaMetricsResponseBreachesInner> breaches) {
    this.breaches = breaches;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlaMetricsResponse slaMetricsResponse = (SlaMetricsResponse) o;
    return Objects.equals(this.range, slaMetricsResponse.range) &&
        Objects.equals(this.overallCompliancePercent, slaMetricsResponse.overallCompliancePercent) &&
        Objects.equals(this.byPriority, slaMetricsResponse.byPriority) &&
        Objects.equals(this.breaches, slaMetricsResponse.breaches);
  }

  @Override
  public int hashCode() {
    return Objects.hash(range, overallCompliancePercent, byPriority, breaches);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SlaMetricsResponse {\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    overallCompliancePercent: ").append(toIndentedString(overallCompliancePercent)).append("\n");
    sb.append("    byPriority: ").append(toIndentedString(byPriority)).append("\n");
    sb.append("    breaches: ").append(toIndentedString(breaches)).append("\n");
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

