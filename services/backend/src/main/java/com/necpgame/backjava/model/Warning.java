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
 * РџСЂРµРґСѓРїСЂРµР¶РґРµРЅРёРµ
 */

@Schema(name = "Warning", description = "РџСЂРµРґСѓРїСЂРµР¶РґРµРЅРёРµ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Warning {

  private String message;

  /**
   * РўРёРї РїСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ
   */
  public enum TypeEnum {
    COMPATIBILITY("compatibility"),
    
    ENERGY("energy"),
    
    LIMIT("limit"),
    
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

  public Warning() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Warning(String message, TypeEnum type) {
    this.message = message;
    this.type = type;
  }

  public Warning message(String message) {
    this.message = message;
    return this;
  }

  /**
   * РўРµРєСЃС‚ РїСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ
   * @return message
   */
  @NotNull 
  @Schema(name = "message", description = "РўРµРєСЃС‚ РїСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Warning type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * РўРёРї РїСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ
   * @return type
   */
  @NotNull 
  @Schema(name = "type", description = "РўРёРї РїСЂРµРґСѓРїСЂРµР¶РґРµРЅРёСЏ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Warning warning = (Warning) o;
    return Objects.equals(this.message, warning.message) &&
        Objects.equals(this.type, warning.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Warning {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

