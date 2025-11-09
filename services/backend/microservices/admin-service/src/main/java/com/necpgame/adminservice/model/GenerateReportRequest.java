package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.GenerateReportRequestTimeRange;
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
 * GenerateReportRequest
 */

@JsonTypeName("generateReport_request")

public class GenerateReportRequest {

  /**
   * Gets or Sets reportType
   */
  public enum ReportTypeEnum {
    USER_BEHAVIOR("user_behavior"),
    
    REVENUE("revenue"),
    
    RETENTION("retention"),
    
    ENGAGEMENT("engagement"),
    
    CUSTOM("custom");

    private final String value;

    ReportTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReportTypeEnum fromValue(String value) {
      for (ReportTypeEnum b : ReportTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReportTypeEnum reportType;

  private @Nullable GenerateReportRequestTimeRange timeRange;

  @Valid
  private Map<String, Object> filters = new HashMap<>();

  public GenerateReportRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GenerateReportRequest(ReportTypeEnum reportType) {
    this.reportType = reportType;
  }

  public GenerateReportRequest reportType(ReportTypeEnum reportType) {
    this.reportType = reportType;
    return this;
  }

  /**
   * Get reportType
   * @return reportType
   */
  @NotNull 
  @Schema(name = "report_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("report_type")
  public ReportTypeEnum getReportType() {
    return reportType;
  }

  public void setReportType(ReportTypeEnum reportType) {
    this.reportType = reportType;
  }

  public GenerateReportRequest timeRange(@Nullable GenerateReportRequestTimeRange timeRange) {
    this.timeRange = timeRange;
    return this;
  }

  /**
   * Get timeRange
   * @return timeRange
   */
  @Valid 
  @Schema(name = "time_range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_range")
  public @Nullable GenerateReportRequestTimeRange getTimeRange() {
    return timeRange;
  }

  public void setTimeRange(@Nullable GenerateReportRequestTimeRange timeRange) {
    this.timeRange = timeRange;
  }

  public GenerateReportRequest filters(Map<String, Object> filters) {
    this.filters = filters;
    return this;
  }

  public GenerateReportRequest putFiltersItem(String key, Object filtersItem) {
    if (this.filters == null) {
      this.filters = new HashMap<>();
    }
    this.filters.put(key, filtersItem);
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public Map<String, Object> getFilters() {
    return filters;
  }

  public void setFilters(Map<String, Object> filters) {
    this.filters = filters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenerateReportRequest generateReportRequest = (GenerateReportRequest) o;
    return Objects.equals(this.reportType, generateReportRequest.reportType) &&
        Objects.equals(this.timeRange, generateReportRequest.timeRange) &&
        Objects.equals(this.filters, generateReportRequest.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reportType, timeRange, filters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GenerateReportRequest {\n");
    sb.append("    reportType: ").append(toIndentedString(reportType)).append("\n");
    sb.append("    timeRange: ").append(toIndentedString(timeRange)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
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

