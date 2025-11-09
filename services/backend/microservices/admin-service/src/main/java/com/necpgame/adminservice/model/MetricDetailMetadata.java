package com.necpgame.adminservice.model;

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
 * MetricDetailMetadata
 */

@JsonTypeName("MetricDetail_metadata")

public class MetricDetailMetadata {

  private @Nullable String calculationMethod;

  @Valid
  private List<String> dataSources = new ArrayList<>();

  private @Nullable Integer lastJobLatencyMs;

  public MetricDetailMetadata calculationMethod(@Nullable String calculationMethod) {
    this.calculationMethod = calculationMethod;
    return this;
  }

  /**
   * Get calculationMethod
   * @return calculationMethod
   */
  
  @Schema(name = "calculationMethod", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("calculationMethod")
  public @Nullable String getCalculationMethod() {
    return calculationMethod;
  }

  public void setCalculationMethod(@Nullable String calculationMethod) {
    this.calculationMethod = calculationMethod;
  }

  public MetricDetailMetadata dataSources(List<String> dataSources) {
    this.dataSources = dataSources;
    return this;
  }

  public MetricDetailMetadata addDataSourcesItem(String dataSourcesItem) {
    if (this.dataSources == null) {
      this.dataSources = new ArrayList<>();
    }
    this.dataSources.add(dataSourcesItem);
    return this;
  }

  /**
   * Get dataSources
   * @return dataSources
   */
  
  @Schema(name = "dataSources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dataSources")
  public List<String> getDataSources() {
    return dataSources;
  }

  public void setDataSources(List<String> dataSources) {
    this.dataSources = dataSources;
  }

  public MetricDetailMetadata lastJobLatencyMs(@Nullable Integer lastJobLatencyMs) {
    this.lastJobLatencyMs = lastJobLatencyMs;
    return this;
  }

  /**
   * Get lastJobLatencyMs
   * minimum: 0
   * @return lastJobLatencyMs
   */
  @Min(value = 0) 
  @Schema(name = "lastJobLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastJobLatencyMs")
  public @Nullable Integer getLastJobLatencyMs() {
    return lastJobLatencyMs;
  }

  public void setLastJobLatencyMs(@Nullable Integer lastJobLatencyMs) {
    this.lastJobLatencyMs = lastJobLatencyMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetricDetailMetadata metricDetailMetadata = (MetricDetailMetadata) o;
    return Objects.equals(this.calculationMethod, metricDetailMetadata.calculationMethod) &&
        Objects.equals(this.dataSources, metricDetailMetadata.dataSources) &&
        Objects.equals(this.lastJobLatencyMs, metricDetailMetadata.lastJobLatencyMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calculationMethod, dataSources, lastJobLatencyMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetricDetailMetadata {\n");
    sb.append("    calculationMethod: ").append(toIndentedString(calculationMethod)).append("\n");
    sb.append("    dataSources: ").append(toIndentedString(dataSources)).append("\n");
    sb.append("    lastJobLatencyMs: ").append(toIndentedString(lastJobLatencyMs)).append("\n");
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

