package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QueueSnapshotRequest
 */


public class QueueSnapshotRequest {

  private String reason;

  private Boolean includeHistory = false;

  public QueueSnapshotRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueSnapshotRequest(String reason) {
    this.reason = reason;
  }

  public QueueSnapshotRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull @Size(max = 200) 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public QueueSnapshotRequest includeHistory(Boolean includeHistory) {
    this.includeHistory = includeHistory;
    return this;
  }

  /**
   * Get includeHistory
   * @return includeHistory
   */
  
  @Schema(name = "includeHistory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeHistory")
  public Boolean getIncludeHistory() {
    return includeHistory;
  }

  public void setIncludeHistory(Boolean includeHistory) {
    this.includeHistory = includeHistory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueSnapshotRequest queueSnapshotRequest = (QueueSnapshotRequest) o;
    return Objects.equals(this.reason, queueSnapshotRequest.reason) &&
        Objects.equals(this.includeHistory, queueSnapshotRequest.includeHistory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, includeHistory);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueSnapshotRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    includeHistory: ").append(toIndentedString(includeHistory)).append("\n");
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

