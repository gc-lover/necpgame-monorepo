package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DiagnosticsRequest
 */


public class DiagnosticsRequest {

  /**
   * Gets or Sets reason
   */
  public enum ReasonEnum {
    PLAYER_REPORT("player_report"),
    
    OPS_MANUAL("ops_manual"),
    
    AUTO_MONITORING("auto_monitoring");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReasonEnum reason;

  private Boolean includeLatencyHistory = true;

  private Boolean includeEvents = true;

  public DiagnosticsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DiagnosticsRequest(ReasonEnum reason) {
    this.reason = reason;
  }

  public DiagnosticsRequest reason(ReasonEnum reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public ReasonEnum getReason() {
    return reason;
  }

  public void setReason(ReasonEnum reason) {
    this.reason = reason;
  }

  public DiagnosticsRequest includeLatencyHistory(Boolean includeLatencyHistory) {
    this.includeLatencyHistory = includeLatencyHistory;
    return this;
  }

  /**
   * Get includeLatencyHistory
   * @return includeLatencyHistory
   */
  
  @Schema(name = "includeLatencyHistory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeLatencyHistory")
  public Boolean getIncludeLatencyHistory() {
    return includeLatencyHistory;
  }

  public void setIncludeLatencyHistory(Boolean includeLatencyHistory) {
    this.includeLatencyHistory = includeLatencyHistory;
  }

  public DiagnosticsRequest includeEvents(Boolean includeEvents) {
    this.includeEvents = includeEvents;
    return this;
  }

  /**
   * Get includeEvents
   * @return includeEvents
   */
  
  @Schema(name = "includeEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeEvents")
  public Boolean getIncludeEvents() {
    return includeEvents;
  }

  public void setIncludeEvents(Boolean includeEvents) {
    this.includeEvents = includeEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiagnosticsRequest diagnosticsRequest = (DiagnosticsRequest) o;
    return Objects.equals(this.reason, diagnosticsRequest.reason) &&
        Objects.equals(this.includeLatencyHistory, diagnosticsRequest.includeLatencyHistory) &&
        Objects.equals(this.includeEvents, diagnosticsRequest.includeEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, includeLatencyHistory, includeEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DiagnosticsRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    includeLatencyHistory: ").append(toIndentedString(includeLatencyHistory)).append("\n");
    sb.append("    includeEvents: ").append(toIndentedString(includeEvents)).append("\n");
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

