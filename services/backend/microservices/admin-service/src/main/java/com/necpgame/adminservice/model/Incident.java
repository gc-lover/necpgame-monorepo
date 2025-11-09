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
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Полная информация об инциденте.
 */

@Schema(name = "Incident", description = "Полная информация об инциденте.")

public class Incident {

  private String id;

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

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    NEW("new"),
    
    ACKNOWLEDGED("acknowledged"),
    
    INVESTIGATING("investigating"),
    
    MITIGATED("mitigated"),
    
    RESOLVED("resolved"),
    
    CLOSED("closed");

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
  private OffsetDateTime detectedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> resolvedAt = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable String commander;

  @Valid
  private List<String> affectedServices = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> slaBreachAt = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable Integer mttrMinutes;

  private @Nullable Integer mttaMinutes;

  public Incident() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Incident(String id, String title, SeverityEnum severity, StatusEnum status, OffsetDateTime detectedAt) {
    this.id = id;
    this.title = title;
    this.severity = severity;
    this.status = status;
    this.detectedAt = detectedAt;
  }

  public Incident id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Incident title(String title) {
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

  public Incident severity(SeverityEnum severity) {
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

  public Incident status(StatusEnum status) {
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

  public Incident detectedAt(OffsetDateTime detectedAt) {
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

  public Incident resolvedAt(OffsetDateTime resolvedAt) {
    this.resolvedAt = JsonNullable.of(resolvedAt);
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolved_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved_at")
  public JsonNullable<OffsetDateTime> getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(JsonNullable<OffsetDateTime> resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  public Incident commander(@Nullable String commander) {
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

  public Incident affectedServices(List<String> affectedServices) {
    this.affectedServices = affectedServices;
    return this;
  }

  public Incident addAffectedServicesItem(String affectedServicesItem) {
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

  public Incident slaBreachAt(OffsetDateTime slaBreachAt) {
    this.slaBreachAt = JsonNullable.of(slaBreachAt);
    return this;
  }

  /**
   * Get slaBreachAt
   * @return slaBreachAt
   */
  @Valid 
  @Schema(name = "sla_breach_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sla_breach_at")
  public JsonNullable<OffsetDateTime> getSlaBreachAt() {
    return slaBreachAt;
  }

  public void setSlaBreachAt(JsonNullable<OffsetDateTime> slaBreachAt) {
    this.slaBreachAt = slaBreachAt;
  }

  public Incident mttrMinutes(@Nullable Integer mttrMinutes) {
    this.mttrMinutes = mttrMinutes;
    return this;
  }

  /**
   * Get mttrMinutes
   * @return mttrMinutes
   */
  
  @Schema(name = "mttr_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mttr_minutes")
  public @Nullable Integer getMttrMinutes() {
    return mttrMinutes;
  }

  public void setMttrMinutes(@Nullable Integer mttrMinutes) {
    this.mttrMinutes = mttrMinutes;
  }

  public Incident mttaMinutes(@Nullable Integer mttaMinutes) {
    this.mttaMinutes = mttaMinutes;
    return this;
  }

  /**
   * Get mttaMinutes
   * @return mttaMinutes
   */
  
  @Schema(name = "mtta_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mtta_minutes")
  public @Nullable Integer getMttaMinutes() {
    return mttaMinutes;
  }

  public void setMttaMinutes(@Nullable Integer mttaMinutes) {
    this.mttaMinutes = mttaMinutes;
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
    return Objects.equals(this.id, incident.id) &&
        Objects.equals(this.title, incident.title) &&
        Objects.equals(this.severity, incident.severity) &&
        Objects.equals(this.status, incident.status) &&
        Objects.equals(this.detectedAt, incident.detectedAt) &&
        equalsNullable(this.resolvedAt, incident.resolvedAt) &&
        Objects.equals(this.commander, incident.commander) &&
        Objects.equals(this.affectedServices, incident.affectedServices) &&
        equalsNullable(this.slaBreachAt, incident.slaBreachAt) &&
        Objects.equals(this.mttrMinutes, incident.mttrMinutes) &&
        Objects.equals(this.mttaMinutes, incident.mttaMinutes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, severity, status, detectedAt, hashCodeNullable(resolvedAt), commander, affectedServices, hashCodeNullable(slaBreachAt), mttrMinutes, mttaMinutes);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Incident {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
    sb.append("    commander: ").append(toIndentedString(commander)).append("\n");
    sb.append("    affectedServices: ").append(toIndentedString(affectedServices)).append("\n");
    sb.append("    slaBreachAt: ").append(toIndentedString(slaBreachAt)).append("\n");
    sb.append("    mttrMinutes: ").append(toIndentedString(mttrMinutes)).append("\n");
    sb.append("    mttaMinutes: ").append(toIndentedString(mttaMinutes)).append("\n");
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

