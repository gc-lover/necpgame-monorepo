package com.necpgame.backjava.model;

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
 * РџСЂРёС‡РёРЅР° РѕС‚РєР°Р·Р° РІ СѓСЃС‚Р°РЅРѕРІРєРµ
 */

@Schema(name = "Reason", description = "РџСЂРёС‡РёРЅР° РѕС‚РєР°Р·Р° РІ СѓСЃС‚Р°РЅРѕРІРєРµ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Reason {

  private String message;

  /**
   * РўРёРї РїСЂРёС‡РёРЅС‹
   */
  public enum TypeEnum {
    SLOT("slot"),
    
    COMPATIBILITY("compatibility"),
    
    LIMIT("limit"),
    
    ENERGY("energy"),
    
    QUALITY("quality");

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
   * РЎРµСЂСЊРµР·РЅРѕСЃС‚СЊ РїСЂРёС‡РёРЅС‹
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

  public Reason() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Reason(String message, TypeEnum type, SeverityEnum severity) {
    this.message = message;
    this.type = type;
    this.severity = severity;
  }

  public Reason message(String message) {
    this.message = message;
    return this;
  }

  /**
   * РўРµРєСЃС‚ РїСЂРёС‡РёРЅС‹
   * @return message
   */
  @NotNull 
  @Schema(name = "message", description = "РўРµРєСЃС‚ РїСЂРёС‡РёРЅС‹", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Reason type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * РўРёРї РїСЂРёС‡РёРЅС‹
   * @return type
   */
  @NotNull 
  @Schema(name = "type", description = "РўРёРї РїСЂРёС‡РёРЅС‹", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Reason severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * РЎРµСЂСЊРµР·РЅРѕСЃС‚СЊ РїСЂРёС‡РёРЅС‹
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", description = "РЎРµСЂСЊРµР·РЅРѕСЃС‚СЊ РїСЂРёС‡РёРЅС‹", requiredMode = Schema.RequiredMode.REQUIRED)
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
    Reason reason = (Reason) o;
    return Objects.equals(this.message, reason.message) &&
        Objects.equals(this.type, reason.type) &&
        Objects.equals(this.severity, reason.severity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, type, severity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Reason {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

