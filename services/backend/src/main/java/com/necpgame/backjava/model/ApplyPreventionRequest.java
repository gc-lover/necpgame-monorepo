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
 * Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ РїСЂРѕС„РёР»Р°РєС‚РёРєРё
 */

@Schema(name = "ApplyPreventionRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° РїСЂРёРјРµРЅРµРЅРёРµ РїСЂРѕС„РёР»Р°РєС‚РёРєРё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class ApplyPreventionRequest {

  /**
   * РњРµС‚РѕРґ РїСЂРѕС„РёР»Р°РєС‚РёРєРё
   */
  public enum MethodEnum {
    MAINTENANCE("maintenance"),
    
    MEDICATION("medication"),
    
    THERAPY("therapy"),
    
    CAREFUL_SELECTION("careful_selection");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MethodEnum method;

  private Float duration;

  public ApplyPreventionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplyPreventionRequest(MethodEnum method, Float duration) {
    this.method = method;
    this.duration = duration;
  }

  public ApplyPreventionRequest method(MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * РњРµС‚РѕРґ РїСЂРѕС„РёР»Р°РєС‚РёРєРё
   * @return method
   */
  @NotNull 
  @Schema(name = "method", description = "РњРµС‚РѕРґ РїСЂРѕС„РёР»Р°РєС‚РёРєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("method")
  public MethodEnum getMethod() {
    return method;
  }

  public void setMethod(MethodEnum method) {
    this.method = method;
  }

  public ApplyPreventionRequest duration(Float duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєРё РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return duration
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "duration", description = "Р”Р»РёС‚РµР»СЊРЅРѕСЃС‚СЊ РїСЂРѕС„РёР»Р°РєС‚РёРєРё РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration")
  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyPreventionRequest applyPreventionRequest = (ApplyPreventionRequest) o;
    return Objects.equals(this.method, applyPreventionRequest.method) &&
        Objects.equals(this.duration, applyPreventionRequest.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(method, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyPreventionRequest {\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

