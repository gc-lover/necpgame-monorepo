package com.necpgame.socialservice.model;

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
 * ApiErrorAllOfError
 */

@JsonTypeName("ApiError_allOf_error")

public class ApiErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_CHAT_MOD_REPORT_DUPLICATE("BIZ_CHAT_MOD_REPORT_DUPLICATE"),
    
    BIZ_CHAT_MOD_BAN_ALREADY_ACTIVE("BIZ_CHAT_MOD_BAN_ALREADY_ACTIVE"),
    
    BIZ_CHAT_MOD_ACCESS_DENIED("BIZ_CHAT_MOD_ACCESS_DENIED"),
    
    VAL_CHAT_MOD_INVALID_RULE("VAL_CHAT_MOD_INVALID_RULE"),
    
    VAL_CHAT_MOD_INVALID_REPORT("VAL_CHAT_MOD_INVALID_REPORT"),
    
    VAL_CHAT_MOD_EXCESSIVE_RATE("VAL_CHAT_MOD_EXCESSIVE_RATE"),
    
    INT_CHAT_MOD_PERSISTENCE_FAILURE("INT_CHAT_MOD_PERSISTENCE_FAILURE"),
    
    INT_CHAT_MOD_SERVICE_UNAVAILABLE("INT_CHAT_MOD_SERVICE_UNAVAILABLE");

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

  public ApiErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApiErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public ApiErrorAllOfError code(CodeEnum code) {
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
    ApiErrorAllOfError apiErrorAllOfError = (ApiErrorAllOfError) o;
    return Objects.equals(this.code, apiErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiErrorAllOfError {\n");
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

