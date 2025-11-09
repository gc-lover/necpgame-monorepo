package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * AnalyticsErrorAllOfError
 */

@JsonTypeName("AnalyticsError_allOf_error")

public class AnalyticsErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_ANALYTICS_METRIC_NOT_FOUND("BIZ_ANALYTICS_METRIC_NOT_FOUND"),
    
    BIZ_ANALYTICS_ALERT_NOT_FOUND("BIZ_ANALYTICS_ALERT_NOT_FOUND"),
    
    BIZ_ANALYTICS_AUTOTUNE_CONFLICT("BIZ_ANALYTICS_AUTOTUNE_CONFLICT"),
    
    VAL_ANALYTICS_INVALID_PERIOD("VAL_ANALYTICS_INVALID_PERIOD"),
    
    VAL_ANALYTICS_SANDBOX_ONLY("VAL_ANALYTICS_SANDBOX_ONLY"),
    
    VAL_ANALYTICS_VERSION_MISMATCH("VAL_ANALYTICS_VERSION_MISMATCH"),
    
    INT_ANALYTICS_JOB_FAILURE("INT_ANALYTICS_JOB_FAILURE"),
    
    INT_ANALYTICS_STREAM_FAILURE("INT_ANALYTICS_STREAM_FAILURE");

    private final String value;

    CodeEnum(String value) {
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
    public static CodeEnum fromValue(String value) {
      for (CodeEnum b : CodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CodeEnum code;

  public AnalyticsErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AnalyticsErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public AnalyticsErrorAllOfError code(CodeEnum code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public CodeEnum getCode() {
    return code;
  }

  public void setCode(CodeEnum code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsErrorAllOfError analyticsErrorAllOfError = (AnalyticsErrorAllOfError) o;
    return Objects.equals(this.code, analyticsErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsErrorAllOfError {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

