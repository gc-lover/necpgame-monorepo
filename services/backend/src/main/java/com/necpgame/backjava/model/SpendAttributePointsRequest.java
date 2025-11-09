package com.necpgame.backjava.model;

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
 * SpendAttributePointsRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SpendAttributePointsRequest {

  @Valid
  private Map<String, Integer> distributions = new HashMap<>();

  public SpendAttributePointsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SpendAttributePointsRequest(Map<String, Integer> distributions) {
    this.distributions = distributions;
  }

  public SpendAttributePointsRequest distributions(Map<String, Integer> distributions) {
    this.distributions = distributions;
    return this;
  }

  public SpendAttributePointsRequest putDistributionsItem(String key, Integer distributionsItem) {
    if (this.distributions == null) {
      this.distributions = new HashMap<>();
    }
    this.distributions.put(key, distributionsItem);
    return this;
  }

  /**
   * Get distributions
   * @return distributions
   */
  @NotNull 
  @Schema(name = "distributions", example = "{\"BODY\":2,\"REFLEXES\":1}", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("distributions")
  public Map<String, Integer> getDistributions() {
    return distributions;
  }

  public void setDistributions(Map<String, Integer> distributions) {
    this.distributions = distributions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpendAttributePointsRequest spendAttributePointsRequest = (SpendAttributePointsRequest) o;
    return Objects.equals(this.distributions, spendAttributePointsRequest.distributions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(distributions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SpendAttributePointsRequest {\n");
    sb.append("    distributions: ").append(toIndentedString(distributions)).append("\n");
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

