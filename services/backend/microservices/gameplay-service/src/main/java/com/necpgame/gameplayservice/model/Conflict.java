package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Конфликт совместимости
 */

@Schema(name = "Conflict", description = "Конфликт совместимости")

public class Conflict {

  private UUID implantId;

  private String reason;

  /**
   * Серьезность конфликта
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

  public Conflict() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Conflict(UUID implantId, String reason, SeverityEnum severity) {
    this.implantId = implantId;
    this.reason = reason;
    this.severity = severity;
  }

  public Conflict implantId(UUID implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Идентификатор конфликтующего импланта
   * @return implantId
   */
  @NotNull @Valid 
  @Schema(name = "implant_id", description = "Идентификатор конфликтующего импланта", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public UUID getImplantId() {
    return implantId;
  }

  public void setImplantId(UUID implantId) {
    this.implantId = implantId;
  }

  public Conflict reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина конфликта
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", description = "Причина конфликта", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public Conflict severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Серьезность конфликта
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", description = "Серьезность конфликта", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Conflict conflict = (Conflict) o;
    return Objects.equals(this.implantId, conflict.implantId) &&
        Objects.equals(this.reason, conflict.reason) &&
        Objects.equals(this.severity, conflict.severity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, reason, severity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Conflict {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
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

