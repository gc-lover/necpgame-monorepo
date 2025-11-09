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
 * ClanWarError
 */


public class ClanWarError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    ALREADY_IN_WAR("ALREADY_IN_WAR"),
    
    INVALID_TERRITORY("INVALID_TERRITORY"),
    
    INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS"),
    
    PROTECTED_PERIOD("PROTECTED_PERIOD"),
    
    ALLY_LIMIT_REACHED("ALLY_LIMIT_REACHED"),
    
    REPORT_DUPLICATE("REPORT_DUPLICATE");

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

  private String message;

  private @Nullable Integer retryAfterSeconds;

  public ClanWarError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ClanWarError(CodeEnum code, String message) {
    this.code = code;
    this.message = message;
  }

  public ClanWarError code(CodeEnum code) {
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

  public ClanWarError message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ClanWarError retryAfterSeconds(@Nullable Integer retryAfterSeconds) {
    this.retryAfterSeconds = retryAfterSeconds;
    return this;
  }

  /**
   * Get retryAfterSeconds
   * @return retryAfterSeconds
   */
  
  @Schema(name = "retryAfterSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("retryAfterSeconds")
  public @Nullable Integer getRetryAfterSeconds() {
    return retryAfterSeconds;
  }

  public void setRetryAfterSeconds(@Nullable Integer retryAfterSeconds) {
    this.retryAfterSeconds = retryAfterSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClanWarError clanWarError = (ClanWarError) o;
    return Objects.equals(this.code, clanWarError.code) &&
        Objects.equals(this.message, clanWarError.message) &&
        Objects.equals(this.retryAfterSeconds, clanWarError.retryAfterSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, retryAfterSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClanWarError {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    retryAfterSeconds: ").append(toIndentedString(retryAfterSeconds)).append("\n");
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

