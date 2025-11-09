package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ErrorErrorDetailsInner
 */

@JsonTypeName("Error_error_details_inner")

public class ErrorErrorDetailsInner {

  private @Nullable String field;

  private @Nullable String message;

  private @Nullable String code;

  public ErrorErrorDetailsInner field(@Nullable String field) {
    this.field = field;
    return this;
  }

  /**
   * Поле с ошибкой
   * @return field
   */
  
  @Schema(name = "field", description = "Поле с ошибкой", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public @Nullable String getField() {
    return field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public ErrorErrorDetailsInner message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Сообщение об ошибке для поля
   * @return message
   */
  
  @Schema(name = "message", description = "Сообщение об ошибке для поля", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public ErrorErrorDetailsInner code(@Nullable String code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки для поля
   * @return code
   */
  
  @Schema(name = "code", description = "Код ошибки для поля", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code")
  public @Nullable String getCode() {
    return code;
  }

  public void setCode(@Nullable String code) {
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
    ErrorErrorDetailsInner errorErrorDetailsInner = (ErrorErrorDetailsInner) o;
    return Objects.equals(this.field, errorErrorDetailsInner.field) &&
        Objects.equals(this.message, errorErrorDetailsInner.message) &&
        Objects.equals(this.code, errorErrorDetailsInner.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, message, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorErrorDetailsInner {\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

