package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LootGenerationContext;
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
 * SimulationRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SimulationRequest {

  private Integer iterations;

  private @Nullable LootGenerationContext context;

  /**
   * Gets or Sets metrics
   */
  public enum MetricsEnum {
    RARITY_DISTRIBUTION("RARITY_DISTRIBUTION"),
    
    CURRENCY_EXPECTATION("CURRENCY_EXPECTATION"),
    
    TOKEN_DROP_RATE("TOKEN_DROP_RATE"),
    
    PITY_TRIGGER("PITY_TRIGGER");

    private final String value;

    MetricsEnum(String value) {
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
    public static MetricsEnum fromValue(String value) {
      for (MetricsEnum b : MetricsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<MetricsEnum> metrics = new ArrayList<>();

  public SimulationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SimulationRequest(Integer iterations) {
    this.iterations = iterations;
  }

  public SimulationRequest iterations(Integer iterations) {
    this.iterations = iterations;
    return this;
  }

  /**
   * Get iterations
   * minimum: 10
   * maximum: 5000
   * @return iterations
   */
  @NotNull @Min(value = 10) @Max(value = 5000) 
  @Schema(name = "iterations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("iterations")
  public Integer getIterations() {
    return iterations;
  }

  public void setIterations(Integer iterations) {
    this.iterations = iterations;
  }

  public SimulationRequest context(@Nullable LootGenerationContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable LootGenerationContext getContext() {
    return context;
  }

  public void setContext(@Nullable LootGenerationContext context) {
    this.context = context;
  }

  public SimulationRequest metrics(List<MetricsEnum> metrics) {
    this.metrics = metrics;
    return this;
  }

  public SimulationRequest addMetricsItem(MetricsEnum metricsItem) {
    if (this.metrics == null) {
      this.metrics = new ArrayList<>();
    }
    this.metrics.add(metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public List<MetricsEnum> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<MetricsEnum> metrics) {
    this.metrics = metrics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimulationRequest simulationRequest = (SimulationRequest) o;
    return Objects.equals(this.iterations, simulationRequest.iterations) &&
        Objects.equals(this.context, simulationRequest.context) &&
        Objects.equals(this.metrics, simulationRequest.metrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iterations, context, metrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationRequest {\n");
    sb.append("    iterations: ").append(toIndentedString(iterations)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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

