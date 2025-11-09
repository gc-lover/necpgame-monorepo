package com.necpgame.worldservice.model;

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
 * OutcomeRequestAnalytics
 */

@JsonTypeName("OutcomeRequest_analytics")

public class OutcomeRequestAnalytics {

  private @Nullable Integer completionTimeSeconds;

  private @Nullable String branchPath;

  @Valid
  private List<String> anomalies = new ArrayList<>();

  public OutcomeRequestAnalytics completionTimeSeconds(@Nullable Integer completionTimeSeconds) {
    this.completionTimeSeconds = completionTimeSeconds;
    return this;
  }

  /**
   * Get completionTimeSeconds
   * minimum: 0
   * @return completionTimeSeconds
   */
  @Min(value = 0) 
  @Schema(name = "completionTimeSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completionTimeSeconds")
  public @Nullable Integer getCompletionTimeSeconds() {
    return completionTimeSeconds;
  }

  public void setCompletionTimeSeconds(@Nullable Integer completionTimeSeconds) {
    this.completionTimeSeconds = completionTimeSeconds;
  }

  public OutcomeRequestAnalytics branchPath(@Nullable String branchPath) {
    this.branchPath = branchPath;
    return this;
  }

  /**
   * Get branchPath
   * @return branchPath
   */
  
  @Schema(name = "branchPath", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branchPath")
  public @Nullable String getBranchPath() {
    return branchPath;
  }

  public void setBranchPath(@Nullable String branchPath) {
    this.branchPath = branchPath;
  }

  public OutcomeRequestAnalytics anomalies(List<String> anomalies) {
    this.anomalies = anomalies;
    return this;
  }

  public OutcomeRequestAnalytics addAnomaliesItem(String anomaliesItem) {
    if (this.anomalies == null) {
      this.anomalies = new ArrayList<>();
    }
    this.anomalies.add(anomaliesItem);
    return this;
  }

  /**
   * Get anomalies
   * @return anomalies
   */
  
  @Schema(name = "anomalies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("anomalies")
  public List<String> getAnomalies() {
    return anomalies;
  }

  public void setAnomalies(List<String> anomalies) {
    this.anomalies = anomalies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutcomeRequestAnalytics outcomeRequestAnalytics = (OutcomeRequestAnalytics) o;
    return Objects.equals(this.completionTimeSeconds, outcomeRequestAnalytics.completionTimeSeconds) &&
        Objects.equals(this.branchPath, outcomeRequestAnalytics.branchPath) &&
        Objects.equals(this.anomalies, outcomeRequestAnalytics.anomalies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(completionTimeSeconds, branchPath, anomalies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutcomeRequestAnalytics {\n");
    sb.append("    completionTimeSeconds: ").append(toIndentedString(completionTimeSeconds)).append("\n");
    sb.append("    branchPath: ").append(toIndentedString(branchPath)).append("\n");
    sb.append("    anomalies: ").append(toIndentedString(anomalies)).append("\n");
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

