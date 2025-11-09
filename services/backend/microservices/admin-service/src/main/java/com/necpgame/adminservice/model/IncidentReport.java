package com.necpgame.adminservice.model;

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
 * IncidentReport
 */


public class IncidentReport {

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

  private @Nullable Integer affectedPlayers;

  @Valid
  private List<String> ticketIds = new ArrayList<>();

  private @Nullable String description;

  private @Nullable String actionsTaken;

  public IncidentReport() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IncidentReport(String incidentId, SeverityEnum severity) {
    this.incidentId = incidentId;
    this.severity = severity;
  }

  public IncidentReport incidentId(String incidentId) {
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

  public IncidentReport severity(SeverityEnum severity) {
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

  public IncidentReport affectedPlayers(@Nullable Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
    return this;
  }

  /**
   * Get affectedPlayers
   * @return affectedPlayers
   */
  
  @Schema(name = "affectedPlayers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedPlayers")
  public @Nullable Integer getAffectedPlayers() {
    return affectedPlayers;
  }

  public void setAffectedPlayers(@Nullable Integer affectedPlayers) {
    this.affectedPlayers = affectedPlayers;
  }

  public IncidentReport ticketIds(List<String> ticketIds) {
    this.ticketIds = ticketIds;
    return this;
  }

  public IncidentReport addTicketIdsItem(String ticketIdsItem) {
    if (this.ticketIds == null) {
      this.ticketIds = new ArrayList<>();
    }
    this.ticketIds.add(ticketIdsItem);
    return this;
  }

  /**
   * Get ticketIds
   * @return ticketIds
   */
  
  @Schema(name = "ticketIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticketIds")
  public List<String> getTicketIds() {
    return ticketIds;
  }

  public void setTicketIds(List<String> ticketIds) {
    this.ticketIds = ticketIds;
  }

  public IncidentReport description(@Nullable String description) {
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

  public IncidentReport actionsTaken(@Nullable String actionsTaken) {
    this.actionsTaken = actionsTaken;
    return this;
  }

  /**
   * Get actionsTaken
   * @return actionsTaken
   */
  
  @Schema(name = "actionsTaken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionsTaken")
  public @Nullable String getActionsTaken() {
    return actionsTaken;
  }

  public void setActionsTaken(@Nullable String actionsTaken) {
    this.actionsTaken = actionsTaken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IncidentReport incidentReport = (IncidentReport) o;
    return Objects.equals(this.incidentId, incidentReport.incidentId) &&
        Objects.equals(this.severity, incidentReport.severity) &&
        Objects.equals(this.affectedPlayers, incidentReport.affectedPlayers) &&
        Objects.equals(this.ticketIds, incidentReport.ticketIds) &&
        Objects.equals(this.description, incidentReport.description) &&
        Objects.equals(this.actionsTaken, incidentReport.actionsTaken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incidentId, severity, affectedPlayers, ticketIds, description, actionsTaken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IncidentReport {\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    affectedPlayers: ").append(toIndentedString(affectedPlayers)).append("\n");
    sb.append("    ticketIds: ").append(toIndentedString(ticketIds)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    actionsTaken: ").append(toIndentedString(actionsTaken)).append("\n");
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

