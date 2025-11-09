package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderValidationIssue
 */


public class PlayerOrderValidationIssue {

  private String code;

  private String message;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
    ERROR("error"),
    
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

  /**
   * Сервис, инициировавший ошибку.
   */
  public enum SourceEnum {
    SOCIAL("social"),
    
    ECONOMY("economy"),
    
    WORLD("world"),
    
    FACTIONS("factions"),
    
    CONTENT("content"),
    
    TELEMETRY("telemetry");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SourceEnum source;

  private @Nullable String field;

  private @Nullable String remediation;

  public PlayerOrderValidationIssue() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderValidationIssue(String code, String message, SeverityEnum severity, SourceEnum source) {
    this.code = code;
    this.message = message;
    this.severity = severity;
    this.source = source;
  }

  public PlayerOrderValidationIssue code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки валидации (например, `VAL_MISSING_OBJECTIVES`).
   * @return code
   */
  @NotNull 
  @Schema(name = "code", description = "Код ошибки валидации (например, `VAL_MISSING_OBJECTIVES`).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public PlayerOrderValidationIssue message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Человекочитаемое сообщение для UI.
   * @return message
   */
  @NotNull 
  @Schema(name = "message", description = "Человекочитаемое сообщение для UI.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public PlayerOrderValidationIssue severity(SeverityEnum severity) {
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

  public PlayerOrderValidationIssue source(SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Сервис, инициировавший ошибку.
   * @return source
   */
  @NotNull 
  @Schema(name = "source", description = "Сервис, инициировавший ошибку.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public PlayerOrderValidationIssue field(@Nullable String field) {
    this.field = field;
    return this;
  }

  /**
   * Поле брифа или бюджета, вызвавшее ошибку.
   * @return field
   */
  
  @Schema(name = "field", description = "Поле брифа или бюджета, вызвавшее ошибку.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public @Nullable String getField() {
    return field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public PlayerOrderValidationIssue remediation(@Nullable String remediation) {
    this.remediation = remediation;
    return this;
  }

  /**
   * Рекомендация по исправлению.
   * @return remediation
   */
  
  @Schema(name = "remediation", description = "Рекомендация по исправлению.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remediation")
  public @Nullable String getRemediation() {
    return remediation;
  }

  public void setRemediation(@Nullable String remediation) {
    this.remediation = remediation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderValidationIssue playerOrderValidationIssue = (PlayerOrderValidationIssue) o;
    return Objects.equals(this.code, playerOrderValidationIssue.code) &&
        Objects.equals(this.message, playerOrderValidationIssue.message) &&
        Objects.equals(this.severity, playerOrderValidationIssue.severity) &&
        Objects.equals(this.source, playerOrderValidationIssue.source) &&
        Objects.equals(this.field, playerOrderValidationIssue.field) &&
        Objects.equals(this.remediation, playerOrderValidationIssue.remediation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, severity, source, field, remediation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderValidationIssue {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    remediation: ").append(toIndentedString(remediation)).append("\n");
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

