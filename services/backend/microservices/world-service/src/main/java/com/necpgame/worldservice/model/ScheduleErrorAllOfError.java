package com.necpgame.worldservice.model;

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
 * ScheduleErrorAllOfError
 */

@JsonTypeName("ScheduleError_allOf_error")

public class ScheduleErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_WORLD_EVENT_NOT_FOUND("BIZ_WORLD_EVENT_NOT_FOUND"),
    
    BIZ_WORLD_EVENT_CONFLICT("BIZ_WORLD_EVENT_CONFLICT"),
    
    BIZ_WORLD_EVENT_COOLDOWN("BIZ_WORLD_EVENT_COOLDOWN"),
    
    BIZ_WORLD_EVENT_TRIGGER_DISABLED("BIZ_WORLD_EVENT_TRIGGER_DISABLED"),
    
    VAL_WORLD_EVENT_INVALID_WINDOW("VAL_WORLD_EVENT_INVALID_WINDOW"),
    
    VAL_WORLD_EVENT_INVALID_LOCALE("VAL_WORLD_EVENT_INVALID_LOCALE"),
    
    VAL_WORLD_EVENT_CONDITION_FAILED("VAL_WORLD_EVENT_CONDITION_FAILED"),
    
    INT_WORLD_EVENT_ECONOMY_FAILURE("INT_WORLD_EVENT_ECONOMY_FAILURE"),
    
    INT_WORLD_EVENT_SOCIAL_FAILURE("INT_WORLD_EVENT_SOCIAL_FAILURE"),
    
    INT_WORLD_EVENT_ANALYTICS_FAILURE("INT_WORLD_EVENT_ANALYTICS_FAILURE");

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

  public ScheduleErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public ScheduleErrorAllOfError code(CodeEnum code) {
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
    ScheduleErrorAllOfError scheduleErrorAllOfError = (ScheduleErrorAllOfError) o;
    return Objects.equals(this.code, scheduleErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleErrorAllOfError {\n");
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

