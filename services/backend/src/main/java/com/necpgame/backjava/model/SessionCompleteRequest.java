package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SessionCompleteRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SessionCompleteRequest {

  private @Nullable String winningTeamId;

  private @Nullable String reason;

  @Valid
  private Map<String, Object> telemetry = new HashMap<>();

  public SessionCompleteRequest winningTeamId(@Nullable String winningTeamId) {
    this.winningTeamId = winningTeamId;
    return this;
  }

  /**
   * Get winningTeamId
   * @return winningTeamId
   */
  
  @Schema(name = "winningTeamId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winningTeamId")
  public @Nullable String getWinningTeamId() {
    return winningTeamId;
  }

  public void setWinningTeamId(@Nullable String winningTeamId) {
    this.winningTeamId = winningTeamId;
  }

  public SessionCompleteRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public SessionCompleteRequest telemetry(Map<String, Object> telemetry) {
    this.telemetry = telemetry;
    return this;
  }

  public SessionCompleteRequest putTelemetryItem(String key, Object telemetryItem) {
    if (this.telemetry == null) {
      this.telemetry = new HashMap<>();
    }
    this.telemetry.put(key, telemetryItem);
    return this;
  }

  /**
   * Get telemetry
   * @return telemetry
   */
  
  @Schema(name = "telemetry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetry")
  public Map<String, Object> getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(Map<String, Object> telemetry) {
    this.telemetry = telemetry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionCompleteRequest sessionCompleteRequest = (SessionCompleteRequest) o;
    return Objects.equals(this.winningTeamId, sessionCompleteRequest.winningTeamId) &&
        Objects.equals(this.reason, sessionCompleteRequest.reason) &&
        Objects.equals(this.telemetry, sessionCompleteRequest.telemetry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(winningTeamId, reason, telemetry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionCompleteRequest {\n");
    sb.append("    winningTeamId: ").append(toIndentedString(winningTeamId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
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

