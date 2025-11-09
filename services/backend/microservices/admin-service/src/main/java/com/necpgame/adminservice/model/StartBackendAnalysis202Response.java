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
 * StartBackendAnalysis202Response
 */

@JsonTypeName("startBackendAnalysis_202_response")

public class StartBackendAnalysis202Response {

  private @Nullable String auditId;

  private @Nullable String status;

  private @Nullable Integer estimatedTime;

  public StartBackendAnalysis202Response auditId(@Nullable String auditId) {
    this.auditId = auditId;
    return this;
  }

  /**
   * Get auditId
   * @return auditId
   */
  
  @Schema(name = "audit_id", example = "audit_2025_11_07_001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audit_id")
  public @Nullable String getAuditId() {
    return auditId;
  }

  public void setAuditId(@Nullable String auditId) {
    this.auditId = auditId;
  }

  public StartBackendAnalysis202Response status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", example = "in_progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public StartBackendAnalysis202Response estimatedTime(@Nullable Integer estimatedTime) {
    this.estimatedTime = estimatedTime;
    return this;
  }

  /**
   * Оценка времени в секундах
   * @return estimatedTime
   */
  
  @Schema(name = "estimated_time", example = "300", description = "Оценка времени в секундах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time")
  public @Nullable Integer getEstimatedTime() {
    return estimatedTime;
  }

  public void setEstimatedTime(@Nullable Integer estimatedTime) {
    this.estimatedTime = estimatedTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartBackendAnalysis202Response startBackendAnalysis202Response = (StartBackendAnalysis202Response) o;
    return Objects.equals(this.auditId, startBackendAnalysis202Response.auditId) &&
        Objects.equals(this.status, startBackendAnalysis202Response.status) &&
        Objects.equals(this.estimatedTime, startBackendAnalysis202Response.estimatedTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auditId, status, estimatedTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartBackendAnalysis202Response {\n");
    sb.append("    auditId: ").append(toIndentedString(auditId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    estimatedTime: ").append(toIndentedString(estimatedTime)).append("\n");
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

