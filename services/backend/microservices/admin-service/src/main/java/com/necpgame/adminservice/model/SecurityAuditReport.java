package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.SecurityAuditReportCategories;
import com.necpgame.adminservice.model.SecurityAuditReportVulnerabilitiesSummary;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SecurityAuditReport
 */


public class SecurityAuditReport {

  private @Nullable String auditId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable Integer overallSecurityScore;

  private @Nullable SecurityAuditReportCategories categories;

  private @Nullable SecurityAuditReportVulnerabilitiesSummary vulnerabilitiesSummary;

  public SecurityAuditReport auditId(@Nullable String auditId) {
    this.auditId = auditId;
    return this;
  }

  /**
   * Get auditId
   * @return auditId
   */
  
  @Schema(name = "audit_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audit_id")
  public @Nullable String getAuditId() {
    return auditId;
  }

  public void setAuditId(@Nullable String auditId) {
    this.auditId = auditId;
  }

  public SecurityAuditReport timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public SecurityAuditReport overallSecurityScore(@Nullable Integer overallSecurityScore) {
    this.overallSecurityScore = overallSecurityScore;
    return this;
  }

  /**
   * Security score (0-100)
   * minimum: 0
   * maximum: 100
   * @return overallSecurityScore
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "overall_security_score", description = "Security score (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overall_security_score")
  public @Nullable Integer getOverallSecurityScore() {
    return overallSecurityScore;
  }

  public void setOverallSecurityScore(@Nullable Integer overallSecurityScore) {
    this.overallSecurityScore = overallSecurityScore;
  }

  public SecurityAuditReport categories(@Nullable SecurityAuditReportCategories categories) {
    this.categories = categories;
    return this;
  }

  /**
   * Get categories
   * @return categories
   */
  @Valid 
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public @Nullable SecurityAuditReportCategories getCategories() {
    return categories;
  }

  public void setCategories(@Nullable SecurityAuditReportCategories categories) {
    this.categories = categories;
  }

  public SecurityAuditReport vulnerabilitiesSummary(@Nullable SecurityAuditReportVulnerabilitiesSummary vulnerabilitiesSummary) {
    this.vulnerabilitiesSummary = vulnerabilitiesSummary;
    return this;
  }

  /**
   * Get vulnerabilitiesSummary
   * @return vulnerabilitiesSummary
   */
  @Valid 
  @Schema(name = "vulnerabilities_summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vulnerabilities_summary")
  public @Nullable SecurityAuditReportVulnerabilitiesSummary getVulnerabilitiesSummary() {
    return vulnerabilitiesSummary;
  }

  public void setVulnerabilitiesSummary(@Nullable SecurityAuditReportVulnerabilitiesSummary vulnerabilitiesSummary) {
    this.vulnerabilitiesSummary = vulnerabilitiesSummary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityAuditReport securityAuditReport = (SecurityAuditReport) o;
    return Objects.equals(this.auditId, securityAuditReport.auditId) &&
        Objects.equals(this.timestamp, securityAuditReport.timestamp) &&
        Objects.equals(this.overallSecurityScore, securityAuditReport.overallSecurityScore) &&
        Objects.equals(this.categories, securityAuditReport.categories) &&
        Objects.equals(this.vulnerabilitiesSummary, securityAuditReport.vulnerabilitiesSummary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auditId, timestamp, overallSecurityScore, categories, vulnerabilitiesSummary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityAuditReport {\n");
    sb.append("    auditId: ").append(toIndentedString(auditId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    overallSecurityScore: ").append(toIndentedString(overallSecurityScore)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    vulnerabilitiesSummary: ").append(toIndentedString(vulnerabilitiesSummary)).append("\n");
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

