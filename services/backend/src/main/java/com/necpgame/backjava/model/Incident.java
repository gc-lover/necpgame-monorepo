package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.IncidentCargoLostInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * Incident
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Incident {

  private @Nullable UUID incidentId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    AMBUSH("AMBUSH"),
    
    WEATHER_DELAY("WEATHER_DELAY"),
    
    MECHANICAL_FAILURE("MECHANICAL_FAILURE"),
    
    ACCIDENT("ACCIDENT"),
    
    THEFT("THEFT");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String severity;

  private @Nullable String description;

  private @Nullable Boolean resolved;

  @Valid
  private List<@Valid IncidentCargoLostInner> cargoLost = new ArrayList<>();

  private @Nullable Boolean insuranceClaim;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public Incident incidentId(@Nullable UUID incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  @Valid 
  @Schema(name = "incident_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incident_id")
  public @Nullable UUID getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(@Nullable UUID incidentId) {
    this.incidentId = incidentId;
  }

  public Incident type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public Incident severity(@Nullable String severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable String getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable String severity) {
    this.severity = severity;
  }

  public Incident description(@Nullable String description) {
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

  public Incident resolved(@Nullable Boolean resolved) {
    this.resolved = resolved;
    return this;
  }

  /**
   * Get resolved
   * @return resolved
   */
  
  @Schema(name = "resolved", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved")
  public @Nullable Boolean getResolved() {
    return resolved;
  }

  public void setResolved(@Nullable Boolean resolved) {
    this.resolved = resolved;
  }

  public Incident cargoLost(List<@Valid IncidentCargoLostInner> cargoLost) {
    this.cargoLost = cargoLost;
    return this;
  }

  public Incident addCargoLostItem(IncidentCargoLostInner cargoLostItem) {
    if (this.cargoLost == null) {
      this.cargoLost = new ArrayList<>();
    }
    this.cargoLost.add(cargoLostItem);
    return this;
  }

  /**
   * Get cargoLost
   * @return cargoLost
   */
  @Valid 
  @Schema(name = "cargo_lost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cargo_lost")
  public List<@Valid IncidentCargoLostInner> getCargoLost() {
    return cargoLost;
  }

  public void setCargoLost(List<@Valid IncidentCargoLostInner> cargoLost) {
    this.cargoLost = cargoLost;
  }

  public Incident insuranceClaim(@Nullable Boolean insuranceClaim) {
    this.insuranceClaim = insuranceClaim;
    return this;
  }

  /**
   * Get insuranceClaim
   * @return insuranceClaim
   */
  
  @Schema(name = "insurance_claim", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insurance_claim")
  public @Nullable Boolean getInsuranceClaim() {
    return insuranceClaim;
  }

  public void setInsuranceClaim(@Nullable Boolean insuranceClaim) {
    this.insuranceClaim = insuranceClaim;
  }

  public Incident occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurred_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurred_at")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Incident incident = (Incident) o;
    return Objects.equals(this.incidentId, incident.incidentId) &&
        Objects.equals(this.type, incident.type) &&
        Objects.equals(this.severity, incident.severity) &&
        Objects.equals(this.description, incident.description) &&
        Objects.equals(this.resolved, incident.resolved) &&
        Objects.equals(this.cargoLost, incident.cargoLost) &&
        Objects.equals(this.insuranceClaim, incident.insuranceClaim) &&
        Objects.equals(this.occurredAt, incident.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incidentId, type, severity, description, resolved, cargoLost, insuranceClaim, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Incident {\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    resolved: ").append(toIndentedString(resolved)).append("\n");
    sb.append("    cargoLost: ").append(toIndentedString(cargoLost)).append("\n");
    sb.append("    insuranceClaim: ").append(toIndentedString(insuranceClaim)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

