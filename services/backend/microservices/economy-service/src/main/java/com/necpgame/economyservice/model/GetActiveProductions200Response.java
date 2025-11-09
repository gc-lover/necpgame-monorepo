package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.ProductionJob;
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
 * GetActiveProductions200Response
 */

@JsonTypeName("getActiveProductions_200_response")

public class GetActiveProductions200Response {

  @Valid
  private List<@Valid ProductionJob> activeJobs = new ArrayList<>();

  private @Nullable Integer maxConcurrentJobs;

  public GetActiveProductions200Response activeJobs(List<@Valid ProductionJob> activeJobs) {
    this.activeJobs = activeJobs;
    return this;
  }

  public GetActiveProductions200Response addActiveJobsItem(ProductionJob activeJobsItem) {
    if (this.activeJobs == null) {
      this.activeJobs = new ArrayList<>();
    }
    this.activeJobs.add(activeJobsItem);
    return this;
  }

  /**
   * Get activeJobs
   * @return activeJobs
   */
  @Valid 
  @Schema(name = "active_jobs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_jobs")
  public List<@Valid ProductionJob> getActiveJobs() {
    return activeJobs;
  }

  public void setActiveJobs(List<@Valid ProductionJob> activeJobs) {
    this.activeJobs = activeJobs;
  }

  public GetActiveProductions200Response maxConcurrentJobs(@Nullable Integer maxConcurrentJobs) {
    this.maxConcurrentJobs = maxConcurrentJobs;
    return this;
  }

  /**
   * Get maxConcurrentJobs
   * @return maxConcurrentJobs
   */
  
  @Schema(name = "max_concurrent_jobs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_concurrent_jobs")
  public @Nullable Integer getMaxConcurrentJobs() {
    return maxConcurrentJobs;
  }

  public void setMaxConcurrentJobs(@Nullable Integer maxConcurrentJobs) {
    this.maxConcurrentJobs = maxConcurrentJobs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveProductions200Response getActiveProductions200Response = (GetActiveProductions200Response) o;
    return Objects.equals(this.activeJobs, getActiveProductions200Response.activeJobs) &&
        Objects.equals(this.maxConcurrentJobs, getActiveProductions200Response.maxConcurrentJobs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeJobs, maxConcurrentJobs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveProductions200Response {\n");
    sb.append("    activeJobs: ").append(toIndentedString(activeJobs)).append("\n");
    sb.append("    maxConcurrentJobs: ").append(toIndentedString(maxConcurrentJobs)).append("\n");
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

