package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.net.URI;
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
 * CheatReportRequestEvidence
 */

@JsonTypeName("CheatReportRequest_evidence")

public class CheatReportRequestEvidence {

  private @Nullable String description;

  @Valid
  private List<String> logs = new ArrayList<>();

  @Valid
  private List<URI> screenshots = new ArrayList<>();

  public CheatReportRequestEvidence description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CheatReportRequestEvidence logs(List<String> logs) {
    this.logs = logs;
    return this;
  }

  public CheatReportRequestEvidence addLogsItem(String logsItem) {
    if (this.logs == null) {
      this.logs = new ArrayList<>();
    }
    this.logs.add(logsItem);
    return this;
  }

  /**
   * Get logs
   * @return logs
   */
  
  @Schema(name = "logs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("logs")
  public List<String> getLogs() {
    return logs;
  }

  public void setLogs(List<String> logs) {
    this.logs = logs;
  }

  public CheatReportRequestEvidence screenshots(List<URI> screenshots) {
    this.screenshots = screenshots;
    return this;
  }

  public CheatReportRequestEvidence addScreenshotsItem(URI screenshotsItem) {
    if (this.screenshots == null) {
      this.screenshots = new ArrayList<>();
    }
    this.screenshots.add(screenshotsItem);
    return this;
  }

  /**
   * Get screenshots
   * @return screenshots
   */
  @Valid 
  @Schema(name = "screenshots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("screenshots")
  public List<URI> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(List<URI> screenshots) {
    this.screenshots = screenshots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheatReportRequestEvidence cheatReportRequestEvidence = (CheatReportRequestEvidence) o;
    return Objects.equals(this.description, cheatReportRequestEvidence.description) &&
        Objects.equals(this.logs, cheatReportRequestEvidence.logs) &&
        Objects.equals(this.screenshots, cheatReportRequestEvidence.screenshots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, logs, screenshots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheatReportRequestEvidence {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    logs: ").append(toIndentedString(logs)).append("\n");
    sb.append("    screenshots: ").append(toIndentedString(screenshots)).append("\n");
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

