package com.necpgame.gameplayservice.model;

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
 * Ошибка валидации
 */

@Schema(name = "Error_1", description = "Ошибка валидации")
@JsonTypeName("Error_1")

public class Error1 {

  private String message;

  private String code;

  public Error1() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Error1(String message, String code) {
    this.message = message;
    this.code = code;
  }

  public Error1 message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Текст ошибки
   * @return message
   */
  @NotNull 
  @Schema(name = "message", description = "Текст ошибки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Error1 code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки
   * @return code
   */
  @NotNull 
  @Schema(name = "code", description = "Код ошибки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
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
    Error1 error1 = (Error1) o;
    return Objects.equals(this.message, error1.message) &&
        Objects.equals(this.code, error1.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error1 {\n");
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

