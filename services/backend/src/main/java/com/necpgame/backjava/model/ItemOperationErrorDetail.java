package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemOperationErrorDetail
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ItemOperationErrorDetail {

  private @Nullable String errorCode;

  private @Nullable String message;

  @Valid
  private Map<String, Object> extra = new HashMap<>();

  public ItemOperationErrorDetail errorCode(@Nullable String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  /**
   * Get errorCode
   * @return errorCode
   */
  
  @Schema(name = "errorCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errorCode")
  public @Nullable String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(@Nullable String errorCode) {
    this.errorCode = errorCode;
  }

  public ItemOperationErrorDetail message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public ItemOperationErrorDetail extra(Map<String, Object> extra) {
    this.extra = extra;
    return this;
  }

  public ItemOperationErrorDetail putExtraItem(String key, Object extraItem) {
    if (this.extra == null) {
      this.extra = new HashMap<>();
    }
    this.extra.put(key, extraItem);
    return this;
  }

  /**
   * Get extra
   * @return extra
   */
  
  @Schema(name = "extra", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("extra")
  public Map<String, Object> getExtra() {
    return extra;
  }

  public void setExtra(Map<String, Object> extra) {
    this.extra = extra;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemOperationErrorDetail itemOperationErrorDetail = (ItemOperationErrorDetail) o;
    return Objects.equals(this.errorCode, itemOperationErrorDetail.errorCode) &&
        Objects.equals(this.message, itemOperationErrorDetail.message) &&
        Objects.equals(this.extra, itemOperationErrorDetail.extra);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorCode, message, extra);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemOperationErrorDetail {\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    extra: ").append(toIndentedString(extra)).append("\n");
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

