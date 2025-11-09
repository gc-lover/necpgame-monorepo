package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * CosmeticIncidentReport
 */


public class CosmeticIncidentReport {

  private String incidentId;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SeverityEnum severity;

  private @Nullable String reason;

  @Valid
  private Map<String, Object> payload = new HashMap<>();

  private @Nullable String detectedBy;

  public CosmeticIncidentReport() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CosmeticIncidentReport(String incidentId, SeverityEnum severity, Map<String, Object> payload) {
    this.incidentId = incidentId;
    this.severity = severity;
    this.payload = payload;
  }

  public CosmeticIncidentReport incidentId(String incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  @NotNull 
  @Schema(name = "incidentId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("incidentId")
  public String getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(String incidentId) {
    this.incidentId = incidentId;
  }

  public CosmeticIncidentReport severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public CosmeticIncidentReport reason(@Nullable String reason) {
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

  public CosmeticIncidentReport payload(Map<String, Object> payload) {
    this.payload = payload;
    return this;
  }

  public CosmeticIncidentReport putPayloadItem(String key, Object payloadItem) {
    if (this.payload == null) {
      this.payload = new HashMap<>();
    }
    this.payload.put(key, payloadItem);
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  @NotNull 
  @Schema(name = "payload", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("payload")
  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

  public CosmeticIncidentReport detectedBy(@Nullable String detectedBy) {
    this.detectedBy = detectedBy;
    return this;
  }

  /**
   * Get detectedBy
   * @return detectedBy
   */
  
  @Schema(name = "detectedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detectedBy")
  public @Nullable String getDetectedBy() {
    return detectedBy;
  }

  public void setDetectedBy(@Nullable String detectedBy) {
    this.detectedBy = detectedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticIncidentReport cosmeticIncidentReport = (CosmeticIncidentReport) o;
    return Objects.equals(this.incidentId, cosmeticIncidentReport.incidentId) &&
        Objects.equals(this.severity, cosmeticIncidentReport.severity) &&
        Objects.equals(this.reason, cosmeticIncidentReport.reason) &&
        Objects.equals(this.payload, cosmeticIncidentReport.payload) &&
        Objects.equals(this.detectedBy, cosmeticIncidentReport.detectedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incidentId, severity, reason, payload, detectedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticIncidentReport {\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
    sb.append("    detectedBy: ").append(toIndentedString(detectedBy)).append("\n");
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

