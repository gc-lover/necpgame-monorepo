package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SecurityAuditReportVulnerabilitiesSummary
 */

@JsonTypeName("SecurityAuditReport_vulnerabilities_summary")

public class SecurityAuditReportVulnerabilitiesSummary {

  private @Nullable Integer critical;

  private @Nullable Integer high;

  private @Nullable Integer medium;

  private @Nullable Integer low;

  public SecurityAuditReportVulnerabilitiesSummary critical(@Nullable Integer critical) {
    this.critical = critical;
    return this;
  }

  /**
   * Get critical
   * @return critical
   */
  
  @Schema(name = "critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical")
  public @Nullable Integer getCritical() {
    return critical;
  }

  public void setCritical(@Nullable Integer critical) {
    this.critical = critical;
  }

  public SecurityAuditReportVulnerabilitiesSummary high(@Nullable Integer high) {
    this.high = high;
    return this;
  }

  /**
   * Get high
   * @return high
   */
  
  @Schema(name = "high", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high")
  public @Nullable Integer getHigh() {
    return high;
  }

  public void setHigh(@Nullable Integer high) {
    this.high = high;
  }

  public SecurityAuditReportVulnerabilitiesSummary medium(@Nullable Integer medium) {
    this.medium = medium;
    return this;
  }

  /**
   * Get medium
   * @return medium
   */
  
  @Schema(name = "medium", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medium")
  public @Nullable Integer getMedium() {
    return medium;
  }

  public void setMedium(@Nullable Integer medium) {
    this.medium = medium;
  }

  public SecurityAuditReportVulnerabilitiesSummary low(@Nullable Integer low) {
    this.low = low;
    return this;
  }

  /**
   * Get low
   * @return low
   */
  
  @Schema(name = "low", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low")
  public @Nullable Integer getLow() {
    return low;
  }

  public void setLow(@Nullable Integer low) {
    this.low = low;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityAuditReportVulnerabilitiesSummary securityAuditReportVulnerabilitiesSummary = (SecurityAuditReportVulnerabilitiesSummary) o;
    return Objects.equals(this.critical, securityAuditReportVulnerabilitiesSummary.critical) &&
        Objects.equals(this.high, securityAuditReportVulnerabilitiesSummary.high) &&
        Objects.equals(this.medium, securityAuditReportVulnerabilitiesSummary.medium) &&
        Objects.equals(this.low, securityAuditReportVulnerabilitiesSummary.low);
  }

  @Override
  public int hashCode() {
    return Objects.hash(critical, high, medium, low);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityAuditReportVulnerabilitiesSummary {\n");
    sb.append("    critical: ").append(toIndentedString(critical)).append("\n");
    sb.append("    high: ").append(toIndentedString(high)).append("\n");
    sb.append("    medium: ").append(toIndentedString(medium)).append("\n");
    sb.append("    low: ").append(toIndentedString(low)).append("\n");
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

