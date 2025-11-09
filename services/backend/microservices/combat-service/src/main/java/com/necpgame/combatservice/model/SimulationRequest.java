package com.necpgame.combatservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
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


public class SimulationRequest {

  private @Nullable Integer iterations;

  @Valid
  private Map<String, Object> parameters = new HashMap<>();

  public SimulationRequest iterations(@Nullable Integer iterations) {
    this.iterations = iterations;
    return this;
  }

  /**
   * Get iterations
   * minimum: 1
   * @return iterations
   */
  @Min(value = 1) 
  @Schema(name = "iterations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("iterations")
  public @Nullable Integer getIterations() {
    return iterations;
  }

  public void setIterations(@Nullable Integer iterations) {
    this.iterations = iterations;
  }

  public SimulationRequest parameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return this;
  }

  public SimulationRequest putParametersItem(String key, Object parametersItem) {
    if (this.parameters == null) {
      this.parameters = new HashMap<>();
    }
    this.parameters.put(key, parametersItem);
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
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
        Objects.equals(this.parameters, simulationRequest.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iterations, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationRequest {\n");
    sb.append("    iterations: ").append(toIndentedString(iterations)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

