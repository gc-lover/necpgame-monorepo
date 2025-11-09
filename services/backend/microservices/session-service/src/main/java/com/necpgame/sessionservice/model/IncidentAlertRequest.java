package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * IncidentAlertRequest
 */


public class IncidentAlertRequest {

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
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

  private Integer affectedPlayers;

  private @Nullable String suspectedCause;

  @Valid
  private List<String> linkedServices = new ArrayList<>();

  private @Nullable String notes;

  public IncidentAlertRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IncidentAlertRequest(SeverityEnum severity, Integer affectedPlayers) {
    this.severity = severity;
    this.affectedPlayers = affectedPlayers;
  }

  public IncidentAlertRequest severity(SeverityEnum severity) {
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

  public IncidentAlertRequest affectedPlayers(Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
    return this;
  }

  /**
   * Get affectedPlayers
   * @return affectedPlayers
   */
  @NotNull 
  @Schema(name = "affectedPlayers", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("affectedPlayers")
  public Integer getAffectedPlayers() {
    return affectedPlayers;
  }

  public void setAffectedPlayers(Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
  }

  public IncidentAlertRequest suspectedCause(@Nullable String suspectedCause) {
    this.suspectedCause = suspectedCause;
    return this;
  }

  /**
   * Get suspectedCause
   * @return suspectedCause
   */
  
  @Schema(name = "suspectedCause", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suspectedCause")
  public @Nullable String getSuspectedCause() {
    return suspectedCause;
  }

  public void setSuspectedCause(@Nullable String suspectedCause) {
    this.suspectedCause = suspectedCause;
  }

  public IncidentAlertRequest linkedServices(List<String> linkedServices) {
    this.linkedServices = linkedServices;
    return this;
  }

  public IncidentAlertRequest addLinkedServicesItem(String linkedServicesItem) {
    if (this.linkedServices == null) {
      this.linkedServices = new ArrayList<>();
    }
    this.linkedServices.add(linkedServicesItem);
    return this;
  }

  /**
   * Get linkedServices
   * @return linkedServices
   */
  
  @Schema(name = "linkedServices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linkedServices")
  public List<String> getLinkedServices() {
    return linkedServices;
  }

  public void setLinkedServices(List<String> linkedServices) {
    this.linkedServices = linkedServices;
  }

  public IncidentAlertRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncidentAlertRequest incidentAlertRequest = (IncidentAlertRequest) o;
    return Objects.equals(this.severity, incidentAlertRequest.severity) &&
        Objects.equals(this.affectedPlayers, incidentAlertRequest.affectedPlayers) &&
        Objects.equals(this.suspectedCause, incidentAlertRequest.suspectedCause) &&
        Objects.equals(this.linkedServices, incidentAlertRequest.linkedServices) &&
        Objects.equals(this.notes, incidentAlertRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(severity, affectedPlayers, suspectedCause, linkedServices, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncidentAlertRequest {\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    affectedPlayers: ").append(toIndentedString(affectedPlayers)).append("\n");
    sb.append("    suspectedCause: ").append(toIndentedString(suspectedCause)).append("\n");
    sb.append("    linkedServices: ").append(toIndentedString(linkedServices)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

