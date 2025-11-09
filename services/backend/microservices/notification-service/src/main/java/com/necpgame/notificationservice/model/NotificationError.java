package com.necpgame.notificationservice.model;

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
 * NotificationError
 */


public class NotificationError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    CHANNEL_DISABLED("CHANNEL_DISABLED"),
    
    DEVICE_INVALID("DEVICE_INVALID"),
    
    QUIET_HOURS("QUIET_HOURS"),
    
    TEMPLATE_MISSING("TEMPLATE_MISSING"),
    
    DELIVERY_FAILED("DELIVERY_FAILED"),
    
    PAYLOAD_INVALID("PAYLOAD_INVALID"),
    
    RATE_LIMITED("RATE_LIMITED"),
    
    WEBHOOK_ERROR("WEBHOOK_ERROR");

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

  public NotificationError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationError(CodeEnum code, String message) {
    this.code = code;
    this.message = message;
  }

  public NotificationError code(CodeEnum code) {
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

  public NotificationError message(String message) {
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

  public NotificationError traceId(@Nullable String traceId) {
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
    NotificationError notificationError = (NotificationError) o;
    return Objects.equals(this.code, notificationError.code) &&
        Objects.equals(this.message, notificationError.message) &&
        Objects.equals(this.traceId, notificationError.traceId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, traceId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationError {\n");
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

