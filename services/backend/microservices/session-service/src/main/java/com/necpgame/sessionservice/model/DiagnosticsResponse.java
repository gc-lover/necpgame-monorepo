package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DiagnosticsResponse
 */


public class DiagnosticsResponse {

  private @Nullable String diagnosticsId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    SCHEDULED("scheduled"),
    
    RUNNING("running"),
    
    COMPLETED("completed");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedReadyAt;

  public DiagnosticsResponse diagnosticsId(@Nullable String diagnosticsId) {
    this.diagnosticsId = diagnosticsId;
    return this;
  }

  /**
   * Get diagnosticsId
   * @return diagnosticsId
   */
  
  @Schema(name = "diagnosticsId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("diagnosticsId")
  public @Nullable String getDiagnosticsId() {
    return diagnosticsId;
  }

  public void setDiagnosticsId(@Nullable String diagnosticsId) {
    this.diagnosticsId = diagnosticsId;
  }

  public DiagnosticsResponse status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public DiagnosticsResponse estimatedReadyAt(@Nullable OffsetDateTime estimatedReadyAt) {
    this.estimatedReadyAt = estimatedReadyAt;
    return this;
  }

  /**
   * Get estimatedReadyAt
   * @return estimatedReadyAt
   */
  @Valid 
  @Schema(name = "estimatedReadyAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedReadyAt")
  public @Nullable OffsetDateTime getEstimatedReadyAt() {
    return estimatedReadyAt;
  }

  public void setEstimatedReadyAt(@Nullable OffsetDateTime estimatedReadyAt) {
    this.estimatedReadyAt = estimatedReadyAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiagnosticsResponse diagnosticsResponse = (DiagnosticsResponse) o;
    return Objects.equals(this.diagnosticsId, diagnosticsResponse.diagnosticsId) &&
        Objects.equals(this.status, diagnosticsResponse.status) &&
        Objects.equals(this.estimatedReadyAt, diagnosticsResponse.estimatedReadyAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(diagnosticsId, status, estimatedReadyAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DiagnosticsResponse {\n");
    sb.append("    diagnosticsId: ").append(toIndentedString(diagnosticsId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    estimatedReadyAt: ").append(toIndentedString(estimatedReadyAt)).append("\n");
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

