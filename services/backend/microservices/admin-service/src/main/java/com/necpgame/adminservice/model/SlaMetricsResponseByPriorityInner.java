package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SlaMetricsResponseByPriorityInner
 */

@JsonTypeName("SlaMetricsResponse_byPriority_inner")

public class SlaMetricsResponseByPriorityInner {

  private @Nullable String priority;

  private @Nullable BigDecimal compliancePercent;

  public SlaMetricsResponseByPriorityInner priority(@Nullable String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable String getPriority() {
    return priority;
  }

  public void setPriority(@Nullable String priority) {
    this.priority = priority;
  }

  public SlaMetricsResponseByPriorityInner compliancePercent(@Nullable BigDecimal compliancePercent) {
    this.compliancePercent = compliancePercent;
    return this;
  }

  /**
   * Get compliancePercent
   * @return compliancePercent
   */
  @Valid 
  @Schema(name = "compliancePercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compliancePercent")
  public @Nullable BigDecimal getCompliancePercent() {
    return compliancePercent;
  }

  public void setCompliancePercent(@Nullable BigDecimal compliancePercent) {
    this.compliancePercent = compliancePercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlaMetricsResponseByPriorityInner slaMetricsResponseByPriorityInner = (SlaMetricsResponseByPriorityInner) o;
    return Objects.equals(this.priority, slaMetricsResponseByPriorityInner.priority) &&
        Objects.equals(this.compliancePercent, slaMetricsResponseByPriorityInner.compliancePercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(priority, compliancePercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SlaMetricsResponseByPriorityInner {\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    compliancePercent: ").append(toIndentedString(compliancePercent)).append("\n");
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

