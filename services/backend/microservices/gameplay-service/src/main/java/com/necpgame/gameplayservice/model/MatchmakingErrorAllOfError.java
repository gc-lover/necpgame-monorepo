package com.necpgame.gameplayservice.model;

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
 * MatchmakingErrorAllOfError
 */

@JsonTypeName("MatchmakingError_allOf_error")

public class MatchmakingErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_MATCH_NOT_FOUND("BIZ_MATCH_NOT_FOUND"),
    
    BIZ_MATCH_ALREADY_CONFIRMED("BIZ_MATCH_ALREADY_CONFIRMED"),
    
    BIZ_MATCH_READY_CHECK_ACTIVE("BIZ_MATCH_READY_CHECK_ACTIVE"),
    
    VAL_MATCH_ROLE_MISMATCH("VAL_MATCH_ROLE_MISMATCH"),
    
    VAL_MATCH_LATENCY_CAP_EXCEEDED("VAL_MATCH_LATENCY_CAP_EXCEEDED"),
    
    VAL_MATCH_INVALID_TOKEN("VAL_MATCH_INVALID_TOKEN"),
    
    INT_MATCH_SERVER_UNAVAILABLE("INT_MATCH_SERVER_UNAVAILABLE"),
    
    INT_MATCH_ANALYTICS_FAILURE("INT_MATCH_ANALYTICS_FAILURE");

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

  public MatchmakingErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchmakingErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public MatchmakingErrorAllOfError code(CodeEnum code) {
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
    MatchmakingErrorAllOfError matchmakingErrorAllOfError = (MatchmakingErrorAllOfError) o;
    return Objects.equals(this.code, matchmakingErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchmakingErrorAllOfError {\n");
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

