package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.WarReportRequestMetrics;
import com.necpgame.socialservice.model.WarReward;
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
 * WarReportRequest
 */


public class WarReportRequest {

  private String siegeId;

  /**
   * Gets or Sets reportType
   */
  public enum ReportTypeEnum {
    BATTLE("battle"),
    
    INFILTRATION("infiltration"),
    
    DEFENSE("defense");

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

  private WarReportRequestMetrics metrics;

  @Valid
  private List<@Valid WarReward> rewardsPreview = new ArrayList<>();

  public WarReportRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WarReportRequest(String siegeId, ReportTypeEnum reportType, WarReportRequestMetrics metrics) {
    this.siegeId = siegeId;
    this.reportType = reportType;
    this.metrics = metrics;
  }

  public WarReportRequest siegeId(String siegeId) {
    this.siegeId = siegeId;
    return this;
  }

  /**
   * Get siegeId
   * @return siegeId
   */
  @NotNull 
  @Schema(name = "siegeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("siegeId")
  public String getSiegeId() {
    return siegeId;
  }

  public void setSiegeId(String siegeId) {
    this.siegeId = siegeId;
  }

  public WarReportRequest reportType(ReportTypeEnum reportType) {
    this.reportType = reportType;
    return this;
  }

  /**
   * Get reportType
   * @return reportType
   */
  @NotNull 
  @Schema(name = "reportType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reportType")
  public ReportTypeEnum getReportType() {
    return reportType;
  }

  public void setReportType(ReportTypeEnum reportType) {
    this.reportType = reportType;
  }

  public WarReportRequest metrics(WarReportRequestMetrics metrics) {
    this.metrics = metrics;
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  @NotNull @Valid 
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("metrics")
  public WarReportRequestMetrics getMetrics() {
    return metrics;
  }

  public void setMetrics(WarReportRequestMetrics metrics) {
    this.metrics = metrics;
  }

  public WarReportRequest rewardsPreview(List<@Valid WarReward> rewardsPreview) {
    this.rewardsPreview = rewardsPreview;
    return this;
  }

  public WarReportRequest addRewardsPreviewItem(WarReward rewardsPreviewItem) {
    if (this.rewardsPreview == null) {
      this.rewardsPreview = new ArrayList<>();
    }
    this.rewardsPreview.add(rewardsPreviewItem);
    return this;
  }

  /**
   * Get rewardsPreview
   * @return rewardsPreview
   */
  @Valid 
  @Schema(name = "rewardsPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardsPreview")
  public List<@Valid WarReward> getRewardsPreview() {
    return rewardsPreview;
  }

  public void setRewardsPreview(List<@Valid WarReward> rewardsPreview) {
    this.rewardsPreview = rewardsPreview;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarReportRequest warReportRequest = (WarReportRequest) o;
    return Objects.equals(this.siegeId, warReportRequest.siegeId) &&
        Objects.equals(this.reportType, warReportRequest.reportType) &&
        Objects.equals(this.metrics, warReportRequest.metrics) &&
        Objects.equals(this.rewardsPreview, warReportRequest.rewardsPreview);
  }

  @Override
  public int hashCode() {
    return Objects.hash(siegeId, reportType, metrics, rewardsPreview);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarReportRequest {\n");
    sb.append("    siegeId: ").append(toIndentedString(siegeId)).append("\n");
    sb.append("    reportType: ").append(toIndentedString(reportType)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
    sb.append("    rewardsPreview: ").append(toIndentedString(rewardsPreview)).append("\n");
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

