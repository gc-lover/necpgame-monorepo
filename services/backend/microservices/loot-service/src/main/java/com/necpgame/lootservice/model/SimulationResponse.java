package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.CurrencyExpectation;
import com.necpgame.lootservice.model.SimulationBucket;
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
 * SimulationResponse
 */


public class SimulationResponse {

  private Integer iterations;

  @Valid
  private List<@Valid SimulationBucket> results = new ArrayList<>();

  @Valid
  private List<@Valid CurrencyExpectation> totalCurrency = new ArrayList<>();

  public SimulationResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SimulationResponse(Integer iterations, List<@Valid SimulationBucket> results) {
    this.iterations = iterations;
    this.results = results;
  }

  public SimulationResponse iterations(Integer iterations) {
    this.iterations = iterations;
    return this;
  }

  /**
   * Get iterations
   * @return iterations
   */
  @NotNull 
  @Schema(name = "iterations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("iterations")
  public Integer getIterations() {
    return iterations;
  }

  public void setIterations(Integer iterations) {
    this.iterations = iterations;
  }

  public SimulationResponse results(List<@Valid SimulationBucket> results) {
    this.results = results;
    return this;
  }

  public SimulationResponse addResultsItem(SimulationBucket resultsItem) {
    if (this.results == null) {
      this.results = new ArrayList<>();
    }
    this.results.add(resultsItem);
    return this;
  }

  /**
   * Get results
   * @return results
   */
  @NotNull @Valid 
  @Schema(name = "results", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("results")
  public List<@Valid SimulationBucket> getResults() {
    return results;
  }

  public void setResults(List<@Valid SimulationBucket> results) {
    this.results = results;
  }

  public SimulationResponse totalCurrency(List<@Valid CurrencyExpectation> totalCurrency) {
    this.totalCurrency = totalCurrency;
    return this;
  }

  public SimulationResponse addTotalCurrencyItem(CurrencyExpectation totalCurrencyItem) {
    if (this.totalCurrency == null) {
      this.totalCurrency = new ArrayList<>();
    }
    this.totalCurrency.add(totalCurrencyItem);
    return this;
  }

  /**
   * Get totalCurrency
   * @return totalCurrency
   */
  @Valid 
  @Schema(name = "totalCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalCurrency")
  public List<@Valid CurrencyExpectation> getTotalCurrency() {
    return totalCurrency;
  }

  public void setTotalCurrency(List<@Valid CurrencyExpectation> totalCurrency) {
    this.totalCurrency = totalCurrency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimulationResponse simulationResponse = (SimulationResponse) o;
    return Objects.equals(this.iterations, simulationResponse.iterations) &&
        Objects.equals(this.results, simulationResponse.results) &&
        Objects.equals(this.totalCurrency, simulationResponse.totalCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iterations, results, totalCurrency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimulationResponse {\n");
    sb.append("    iterations: ").append(toIndentedString(iterations)).append("\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
    sb.append("    totalCurrency: ").append(toIndentedString(totalCurrency)).append("\n");
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

