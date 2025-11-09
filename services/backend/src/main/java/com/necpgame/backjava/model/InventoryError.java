package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ItemOperationErrorDetail;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InventoryError
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class InventoryError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    CONTAINER_FULL("CONTAINER_FULL"),
    
    SLOT_LOCKED("SLOT_LOCKED"),
    
    ITEM_NOT_FOUND("ITEM_NOT_FOUND"),
    
    WEIGHT_LIMIT("WEIGHT_LIMIT"),
    
    REQUIREMENT_FAILED("REQUIREMENT_FAILED"),
    
    RESERVATION_CONFLICT("RESERVATION_CONFLICT"),
    
    RESERVATION_NOT_FOUND("RESERVATION_NOT_FOUND"),
    
    AUTO_SORT_DISABLED("AUTO_SORT_DISABLED");

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

  private @Nullable ItemOperationErrorDetail details;

  public InventoryError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InventoryError(CodeEnum code, String message) {
    this.code = code;
    this.message = message;
  }

  public InventoryError code(CodeEnum code) {
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

  public InventoryError message(String message) {
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

  public InventoryError traceId(@Nullable String traceId) {
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

  public InventoryError details(@Nullable ItemOperationErrorDetail details) {
    this.details = details;
    return this;
  }

  /**
   * Get details
   * @return details
   */
  @Valid 
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public @Nullable ItemOperationErrorDetail getDetails() {
    return details;
  }

  public void setDetails(@Nullable ItemOperationErrorDetail details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InventoryError inventoryError = (InventoryError) o;
    return Objects.equals(this.code, inventoryError.code) &&
        Objects.equals(this.message, inventoryError.message) &&
        Objects.equals(this.traceId, inventoryError.traceId) &&
        Objects.equals(this.details, inventoryError.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, traceId, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InventoryError {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    traceId: ").append(toIndentedString(traceId)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

