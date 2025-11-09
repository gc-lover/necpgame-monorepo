package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.DistributeProfits200ResponseDistributionsInner;
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
 * DistributeProfits200Response
 */

@JsonTypeName("distributeProfits_200_response")

public class DistributeProfits200Response {

  private @Nullable UUID distributionId;

  private @Nullable Integer totalDistributed;

  @Valid
  private List<@Valid DistributeProfits200ResponseDistributionsInner> distributions = new ArrayList<>();

  public DistributeProfits200Response distributionId(@Nullable UUID distributionId) {
    this.distributionId = distributionId;
    return this;
  }

  /**
   * Get distributionId
   * @return distributionId
   */
  @Valid 
  @Schema(name = "distribution_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distribution_id")
  public @Nullable UUID getDistributionId() {
    return distributionId;
  }

  public void setDistributionId(@Nullable UUID distributionId) {
    this.distributionId = distributionId;
  }

  public DistributeProfits200Response totalDistributed(@Nullable Integer totalDistributed) {
    this.totalDistributed = totalDistributed;
    return this;
  }

  /**
   * Get totalDistributed
   * @return totalDistributed
   */
  
  @Schema(name = "total_distributed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_distributed")
  public @Nullable Integer getTotalDistributed() {
    return totalDistributed;
  }

  public void setTotalDistributed(@Nullable Integer totalDistributed) {
    this.totalDistributed = totalDistributed;
  }

  public DistributeProfits200Response distributions(List<@Valid DistributeProfits200ResponseDistributionsInner> distributions) {
    this.distributions = distributions;
    return this;
  }

  public DistributeProfits200Response addDistributionsItem(DistributeProfits200ResponseDistributionsInner distributionsItem) {
    if (this.distributions == null) {
      this.distributions = new ArrayList<>();
    }
    this.distributions.add(distributionsItem);
    return this;
  }

  /**
   * Get distributions
   * @return distributions
   */
  @Valid 
  @Schema(name = "distributions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distributions")
  public List<@Valid DistributeProfits200ResponseDistributionsInner> getDistributions() {
    return distributions;
  }

  public void setDistributions(List<@Valid DistributeProfits200ResponseDistributionsInner> distributions) {
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
    DistributeProfits200Response distributeProfits200Response = (DistributeProfits200Response) o;
    return Objects.equals(this.distributionId, distributeProfits200Response.distributionId) &&
        Objects.equals(this.totalDistributed, distributeProfits200Response.totalDistributed) &&
        Objects.equals(this.distributions, distributeProfits200Response.distributions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(distributionId, totalDistributed, distributions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DistributeProfits200Response {\n");
    sb.append("    distributionId: ").append(toIndentedString(distributionId)).append("\n");
    sb.append("    totalDistributed: ").append(toIndentedString(totalDistributed)).append("\n");
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

