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
 * StartBackendAnalysisRequest
 */

@JsonTypeName("startBackendAnalysis_request")

public class StartBackendAnalysisRequest {

  @Valid
  private List<String> components = new ArrayList<>();

  private Boolean includeTests = true;

  private Boolean includePerformance = true;

  public StartBackendAnalysisRequest components(List<String> components) {
    this.components = components;
    return this;
  }

  public StartBackendAnalysisRequest addComponentsItem(String componentsItem) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(componentsItem);
    return this;
  }

  /**
   * Список компонентов для анализа (пусто = все)
   * @return components
   */
  
  @Schema(name = "components", description = "Список компонентов для анализа (пусто = все)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components")
  public List<String> getComponents() {
    return components;
  }

  public void setComponents(List<String> components) {
    this.components = components;
  }

  public StartBackendAnalysisRequest includeTests(Boolean includeTests) {
    this.includeTests = includeTests;
    return this;
  }

  /**
   * Включить анализ тестов
   * @return includeTests
   */
  
  @Schema(name = "include_tests", description = "Включить анализ тестов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("include_tests")
  public Boolean getIncludeTests() {
    return includeTests;
  }

  public void setIncludeTests(Boolean includeTests) {
    this.includeTests = includeTests;
  }

  public StartBackendAnalysisRequest includePerformance(Boolean includePerformance) {
    this.includePerformance = includePerformance;
    return this;
  }

  /**
   * Включить performance benchmarks
   * @return includePerformance
   */
  
  @Schema(name = "include_performance", description = "Включить performance benchmarks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("include_performance")
  public Boolean getIncludePerformance() {
    return includePerformance;
  }

  public void setIncludePerformance(Boolean includePerformance) {
    this.includePerformance = includePerformance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartBackendAnalysisRequest startBackendAnalysisRequest = (StartBackendAnalysisRequest) o;
    return Objects.equals(this.components, startBackendAnalysisRequest.components) &&
        Objects.equals(this.includeTests, startBackendAnalysisRequest.includeTests) &&
        Objects.equals(this.includePerformance, startBackendAnalysisRequest.includePerformance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(components, includeTests, includePerformance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartBackendAnalysisRequest {\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("    includeTests: ").append(toIndentedString(includeTests)).append("\n");
    sb.append("    includePerformance: ").append(toIndentedString(includePerformance)).append("\n");
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

