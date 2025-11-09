package com.necpgame.gameplayservice.model;

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
 * BattlePassRewardError
 */


public class BattlePassRewardError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    LEVEL_LOCKED("LEVEL_LOCKED"),
    
    ALREADY_CLAIMED("ALREADY_CLAIMED"),
    
    INSUFFICIENT_TOKEN("INSUFFICIENT_TOKEN"),
    
    CHALLENGE_NOT_FOUND("CHALLENGE_NOT_FOUND"),
    
    BOOST_ACTIVE("BOOST_ACTIVE"),
    
    REROLL_LIMIT("REROLL_LIMIT");

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

  private @Nullable String traceId;

  public BattlePassRewardError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BattlePassRewardError(CodeEnum code, String message) {
    this.code = code;
    this.message = message;
  }

  public BattlePassRewardError code(CodeEnum code) {
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

  public BattlePassRewardError message(String message) {
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

  public BattlePassRewardError traceId(@Nullable String traceId) {
    this.traceId = traceId;
    return this;
  }

  /**
   * Get traceId
   * @return traceId
   */
  
  @Schema(name = "traceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("traceId")
  public @Nullable String getTraceId() {
    return traceId;
  }

  public void setTraceId(@Nullable String traceId) {
    this.traceId = traceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassRewardError battlePassRewardError = (BattlePassRewardError) o;
    return Objects.equals(this.code, battlePassRewardError.code) &&
        Objects.equals(this.message, battlePassRewardError.message) &&
        Objects.equals(this.traceId, battlePassRewardError.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, traceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassRewardError {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
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

