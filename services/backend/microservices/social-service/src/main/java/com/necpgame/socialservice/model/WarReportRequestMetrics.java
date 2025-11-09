package com.necpgame.socialservice.model;

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
 * WarReportRequestMetrics
 */

@JsonTypeName("WarReportRequest_metrics")

public class WarReportRequestMetrics {

  private @Nullable Integer killsAttacker;

  private @Nullable Integer killsDefender;

  private @Nullable Integer objectivesSecured;

  private @Nullable BigDecimal captureProgress;

  public WarReportRequestMetrics killsAttacker(@Nullable Integer killsAttacker) {
    this.killsAttacker = killsAttacker;
    return this;
  }

  /**
   * Get killsAttacker
   * @return killsAttacker
   */
  
  @Schema(name = "killsAttacker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("killsAttacker")
  public @Nullable Integer getKillsAttacker() {
    return killsAttacker;
  }

  public void setKillsAttacker(@Nullable Integer killsAttacker) {
    this.killsAttacker = killsAttacker;
  }

  public WarReportRequestMetrics killsDefender(@Nullable Integer killsDefender) {
    this.killsDefender = killsDefender;
    return this;
  }

  /**
   * Get killsDefender
   * @return killsDefender
   */
  
  @Schema(name = "killsDefender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("killsDefender")
  public @Nullable Integer getKillsDefender() {
    return killsDefender;
  }

  public void setKillsDefender(@Nullable Integer killsDefender) {
    this.killsDefender = killsDefender;
  }

  public WarReportRequestMetrics objectivesSecured(@Nullable Integer objectivesSecured) {
    this.objectivesSecured = objectivesSecured;
    return this;
  }

  /**
   * Get objectivesSecured
   * @return objectivesSecured
   */
  
  @Schema(name = "objectivesSecured", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectivesSecured")
  public @Nullable Integer getObjectivesSecured() {
    return objectivesSecured;
  }

  public void setObjectivesSecured(@Nullable Integer objectivesSecured) {
    this.objectivesSecured = objectivesSecured;
  }

  public WarReportRequestMetrics captureProgress(@Nullable BigDecimal captureProgress) {
    this.captureProgress = captureProgress;
    return this;
  }

  /**
   * Get captureProgress
   * @return captureProgress
   */
  @Valid 
  @Schema(name = "captureProgress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("captureProgress")
  public @Nullable BigDecimal getCaptureProgress() {
    return captureProgress;
  }

  public void setCaptureProgress(@Nullable BigDecimal captureProgress) {
    this.captureProgress = captureProgress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarReportRequestMetrics warReportRequestMetrics = (WarReportRequestMetrics) o;
    return Objects.equals(this.killsAttacker, warReportRequestMetrics.killsAttacker) &&
        Objects.equals(this.killsDefender, warReportRequestMetrics.killsDefender) &&
        Objects.equals(this.objectivesSecured, warReportRequestMetrics.objectivesSecured) &&
        Objects.equals(this.captureProgress, warReportRequestMetrics.captureProgress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(killsAttacker, killsDefender, objectivesSecured, captureProgress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarReportRequestMetrics {\n");
    sb.append("    killsAttacker: ").append(toIndentedString(killsAttacker)).append("\n");
    sb.append("    killsDefender: ").append(toIndentedString(killsDefender)).append("\n");
    sb.append("    objectivesSecured: ").append(toIndentedString(objectivesSecured)).append("\n");
    sb.append("    captureProgress: ").append(toIndentedString(captureProgress)).append("\n");
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

