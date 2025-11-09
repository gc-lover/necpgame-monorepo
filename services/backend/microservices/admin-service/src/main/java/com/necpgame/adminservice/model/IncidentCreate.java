package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Исходные данные по инциденту от alerting системы или on-call инженера.
 */

@Schema(name = "IncidentCreate", description = "Исходные данные по инциденту от alerting системы или on-call инженера.")

public class IncidentCreate {

  private String title;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    CRITICAL("critical"),
    
    HIGH("high"),
    
    MEDIUM("medium"),
    
    LOW("low");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime detectedAt;

  private @Nullable String detectedBy;

  @Valid
  private List<String> affectedServices = new ArrayList<>();

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime slaBreachAt;

  private @Nullable String commander;

  public IncidentCreate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IncidentCreate(String title, SeverityEnum severity, OffsetDateTime detectedAt) {
    this.title = title;
    this.severity = severity;
    this.detectedAt = detectedAt;
  }

  public IncidentCreate title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public IncidentCreate severity(SeverityEnum severity) {
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

  public IncidentCreate detectedAt(OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
    return this;
  }

  /**
   * Get detectedAt
   * @return detectedAt
   */
  @NotNull @Valid 
  @Schema(name = "detected_at", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("detected_at")
  public OffsetDateTime getDetectedAt() {
    return detectedAt;
  }

  public void setDetectedAt(OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
  }

  public IncidentCreate detectedBy(@Nullable String detectedBy) {
    this.detectedBy = detectedBy;
    return this;
  }

  /**
   * Get detectedBy
   * @return detectedBy
   */
  
  @Schema(name = "detected_by", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detected_by")
  public @Nullable String getDetectedBy() {
    return detectedBy;
  }

  public void setDetectedBy(@Nullable String detectedBy) {
    this.detectedBy = detectedBy;
  }

  public IncidentCreate affectedServices(List<String> affectedServices) {
    this.affectedServices = affectedServices;
    return this;
  }

  public IncidentCreate addAffectedServicesItem(String affectedServicesItem) {
    if (this.affectedServices == null) {
      this.affectedServices = new ArrayList<>();
    }
    this.affectedServices.add(affectedServicesItem);
    return this;
  }

  /**
   * Get affectedServices
   * @return affectedServices
   */
  
  @Schema(name = "affected_services", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_services")
  public List<String> getAffectedServices() {
    return affectedServices;
  }

  public void setAffectedServices(List<String> affectedServices) {
    this.affectedServices = affectedServices;
  }

  public IncidentCreate description(@Nullable String description) {
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

  public IncidentCreate slaBreachAt(@Nullable OffsetDateTime slaBreachAt) {
    this.slaBreachAt = slaBreachAt;
    return this;
  }

  /**
   * Get slaBreachAt
   * @return slaBreachAt
   */
  @Valid 
  @Schema(name = "sla_breach_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sla_breach_at")
  public @Nullable OffsetDateTime getSlaBreachAt() {
    return slaBreachAt;
  }

  public void setSlaBreachAt(@Nullable OffsetDateTime slaBreachAt) {
    this.slaBreachAt = slaBreachAt;
  }

  public IncidentCreate commander(@Nullable String commander) {
    this.commander = commander;
    return this;
  }

  /**
   * Get commander
   * @return commander
   */
  
  @Schema(name = "commander", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commander")
  public @Nullable String getCommander() {
    return commander;
  }

  public void setCommander(@Nullable String commander) {
    this.commander = commander;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncidentCreate incidentCreate = (IncidentCreate) o;
    return Objects.equals(this.title, incidentCreate.title) &&
        Objects.equals(this.severity, incidentCreate.severity) &&
        Objects.equals(this.detectedAt, incidentCreate.detectedAt) &&
        Objects.equals(this.detectedBy, incidentCreate.detectedBy) &&
        Objects.equals(this.affectedServices, incidentCreate.affectedServices) &&
        Objects.equals(this.description, incidentCreate.description) &&
        Objects.equals(this.slaBreachAt, incidentCreate.slaBreachAt) &&
        Objects.equals(this.commander, incidentCreate.commander);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, severity, detectedAt, detectedBy, affectedServices, description, slaBreachAt, commander);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncidentCreate {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
    sb.append("    detectedBy: ").append(toIndentedString(detectedBy)).append("\n");
    sb.append("    affectedServices: ").append(toIndentedString(affectedServices)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    slaBreachAt: ").append(toIndentedString(slaBreachAt)).append("\n");
    sb.append("    commander: ").append(toIndentedString(commander)).append("\n");
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

