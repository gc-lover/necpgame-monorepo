package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.HealthStatusResponseSummary;
import com.necpgame.adminservice.model.ServiceHealth;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * HealthStatusResponse
 */


public class HealthStatusResponse {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    HEALTHY("healthy"),
    
    DEGRADED("degraded"),
    
    UNHEALTHY("unhealthy");

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

  private StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  @Valid
  private List<@Valid ServiceHealth> servicesStatus = new ArrayList<>();

  private @Nullable HealthStatusResponseSummary summary;

  public HealthStatusResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HealthStatusResponse(StatusEnum status, OffsetDateTime timestamp) {
    this.status = status;
    this.timestamp = timestamp;
  }

  public HealthStatusResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public HealthStatusResponse timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public HealthStatusResponse servicesStatus(List<@Valid ServiceHealth> servicesStatus) {
    this.servicesStatus = servicesStatus;
    return this;
  }

  public HealthStatusResponse addServicesStatusItem(ServiceHealth servicesStatusItem) {
    if (this.servicesStatus == null) {
      this.servicesStatus = new ArrayList<>();
    }
    this.servicesStatus.add(servicesStatusItem);
    return this;
  }

  /**
   * Get servicesStatus
   * @return servicesStatus
   */
  @Valid 
  @Schema(name = "services_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("services_status")
  public List<@Valid ServiceHealth> getServicesStatus() {
    return servicesStatus;
  }

  public void setServicesStatus(List<@Valid ServiceHealth> servicesStatus) {
    this.servicesStatus = servicesStatus;
  }

  public HealthStatusResponse summary(@Nullable HealthStatusResponseSummary summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @Valid 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public @Nullable HealthStatusResponseSummary getSummary() {
    return summary;
  }

  public void setSummary(@Nullable HealthStatusResponseSummary summary) {
    this.summary = summary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HealthStatusResponse healthStatusResponse = (HealthStatusResponse) o;
    return Objects.equals(this.status, healthStatusResponse.status) &&
        Objects.equals(this.timestamp, healthStatusResponse.timestamp) &&
        Objects.equals(this.servicesStatus, healthStatusResponse.servicesStatus) &&
        Objects.equals(this.summary, healthStatusResponse.summary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, timestamp, servicesStatus, summary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HealthStatusResponse {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    servicesStatus: ").append(toIndentedString(servicesStatus)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
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

