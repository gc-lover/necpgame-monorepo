package com.necpgame.gameplayservice.model;

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
 * CombatMetricsResponse
 */


public class CombatMetricsResponse {

  private @Nullable String sessionId;

  @Valid
  private Map<String, Object> metrics = new HashMap<>();

  public CombatMetricsResponse sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public CombatMetricsResponse metrics(Map<String, Object> metrics) {
    this.metrics = metrics;
    return this;
  }

  public CombatMetricsResponse putMetricsItem(String key, Object metricsItem) {
    if (this.metrics == null) {
      this.metrics = new HashMap<>();
    }
    this.metrics.put(key, metricsItem);
    return this;
  }

  /**
   * Get metrics
   * @return metrics
   */
  
  @Schema(name = "metrics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metrics")
  public Map<String, Object> getMetrics() {
    return metrics;
  }

  public void setMetrics(Map<String, Object> metrics) {
    this.metrics = metrics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CombatMetricsResponse combatMetricsResponse = (CombatMetricsResponse) o;
    return Objects.equals(this.sessionId, combatMetricsResponse.sessionId) &&
        Objects.equals(this.metrics, combatMetricsResponse.metrics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, metrics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CombatMetricsResponse {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    metrics: ").append(toIndentedString(metrics)).append("\n");
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

