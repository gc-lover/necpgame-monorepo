package com.necpgame.inventoryservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.inventoryservice.model.ErrorErrorDetailsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ErrorError
 */

@JsonTypeName("Error_error")

public class ErrorError {

  private String code;

  private String message;

  @Valid
  private List<@Valid ErrorErrorDetailsInner> details = new ArrayList<>();

  public ErrorError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ErrorError(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public ErrorError code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки для программной обработки
   * @return code
   */
  @NotNull 
  @Schema(name = "code", example = "VALIDATION_ERROR", description = "Код ошибки для программной обработки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorError message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Человекочитаемое сообщение об ошибке
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "Неверные параметры запроса", description = "Человекочитаемое сообщение об ошибке", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ErrorError details(List<@Valid ErrorErrorDetailsInner> details) {
    this.details = details;
    return this;
  }

  public ErrorError addDetailsItem(ErrorErrorDetailsInner detailsItem) {
    if (this.details == null) {
      this.details = new ArrayList<>();
    }
    this.details.add(detailsItem);
    return this;
  }

  /**
   * Детальная информация об ошибке (опционально)
   * @return details
   */
  @Valid 
  @Schema(name = "details", description = "Детальная информация об ошибке (опционально)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public List<@Valid ErrorErrorDetailsInner> getDetails() {
    return details;
  }

  public void setDetails(List<@Valid ErrorErrorDetailsInner> details) {
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
    ErrorError errorError = (ErrorError) o;
    return Objects.equals(this.code, errorError.code) &&
        Objects.equals(this.message, errorError.message) &&
        Objects.equals(this.details, errorError.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorError {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

