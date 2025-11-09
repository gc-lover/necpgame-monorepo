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
 * QueueErrorAllOfError
 */

@JsonTypeName("QueueError_allOf_error")

public class QueueErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_QUEUE_ALREADY_ACTIVE("BIZ_QUEUE_ALREADY_ACTIVE"),
    
    BIZ_QUEUE_NOT_FOUND("BIZ_QUEUE_NOT_FOUND"),
    
    BIZ_QUEUE_MATCH_LOCKED("BIZ_QUEUE_MATCH_LOCKED"),
    
    BIZ_QUEUE_PRIORITY_LIMIT("BIZ_QUEUE_PRIORITY_LIMIT"),
    
    VAL_QUEUE_INVALID_LEVEL_RANGE("VAL_QUEUE_INVALID_LEVEL_RANGE"),
    
    VAL_QUEUE_PARTY_OFFLINE("VAL_QUEUE_PARTY_OFFLINE"),
    
    VAL_QUEUE_RATE_LIMIT("VAL_QUEUE_RATE_LIMIT"),
    
    INT_QUEUE_STORAGE_FAILURE("INT_QUEUE_STORAGE_FAILURE"),
    
    INT_QUEUE_EVENT_BUS_FAILURE("INT_QUEUE_EVENT_BUS_FAILURE");

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

  public QueueErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public QueueErrorAllOfError code(CodeEnum code) {
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
    QueueErrorAllOfError queueErrorAllOfError = (QueueErrorAllOfError) o;
    return Objects.equals(this.code, queueErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueErrorAllOfError {\n");
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

