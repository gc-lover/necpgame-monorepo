package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * RiskIncident
 */


public class RiskIncident {

  private UUID incidentId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PAYMENT_DELAY("payment_delay"),
    
    CONTRACT_BREACH("contract_breach"),
    
    DISPUTE_LOSS("dispute_loss"),
    
    FRAUD_ATTEMPT("fraud_attempt"),
    
    ARBITRATION("arbitration");

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

  private TypeEnum type;

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

  private @Nullable SeverityEnum severity;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime occurredAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime resolvedAt;

  private @Nullable String notes;

  public RiskIncident() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskIncident(UUID incidentId, TypeEnum type, OffsetDateTime occurredAt) {
    this.incidentId = incidentId;
    this.type = type;
    this.occurredAt = occurredAt;
  }

  public RiskIncident incidentId(UUID incidentId) {
    this.incidentId = incidentId;
    return this;
  }

  /**
   * Get incidentId
   * @return incidentId
   */
  @NotNull @Valid 
  @Schema(name = "incidentId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("incidentId")
  public UUID getIncidentId() {
    return incidentId;
  }

  public void setIncidentId(UUID incidentId) {
    this.incidentId = incidentId;
  }

  public RiskIncident type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public RiskIncident severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public RiskIncident occurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @NotNull @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("occurredAt")
  public OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public RiskIncident resolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolvedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolvedAt")
  public @Nullable OffsetDateTime getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(@Nullable OffsetDateTime resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  public RiskIncident notes(@Nullable String notes) {
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
    RiskIncident riskIncident = (RiskIncident) o;
    return Objects.equals(this.incidentId, riskIncident.incidentId) &&
        Objects.equals(this.type, riskIncident.type) &&
        Objects.equals(this.severity, riskIncident.severity) &&
        Objects.equals(this.occurredAt, riskIncident.occurredAt) &&
        Objects.equals(this.resolvedAt, riskIncident.resolvedAt) &&
        Objects.equals(this.notes, riskIncident.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(incidentId, type, severity, occurredAt, resolvedAt, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskIncident {\n");
    sb.append("    incidentId: ").append(toIndentedString(incidentId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
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

