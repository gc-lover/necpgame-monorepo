package com.necpgame.backjava.model;

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
 * Error1ErrorDetailsInner
 */

@JsonTypeName("Error_1_error_details_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Error1ErrorDetailsInner {

  private @Nullable String field;

  private @Nullable String message;

  private @Nullable String code;

  public Error1ErrorDetailsInner field(@Nullable String field) {
    this.field = field;
    return this;
  }

  /**
   * РџРѕР»Рµ СЃ РѕС€РёР±РєРѕР№
   * @return field
   */
  
  @Schema(name = "field", description = "РџРѕР»Рµ СЃ РѕС€РёР±РєРѕР№", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public @Nullable String getField() {
    return field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public Error1ErrorDetailsInner message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * РЎРѕРѕР±С‰РµРЅРёРµ РѕР± РѕС€РёР±РєРµ РґР»СЏ РїРѕР»СЏ
   * @return message
   */
  
  @Schema(name = "message", description = "РЎРѕРѕР±С‰РµРЅРёРµ РѕР± РѕС€РёР±РєРµ РґР»СЏ РїРѕР»СЏ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public Error1ErrorDetailsInner code(@Nullable String code) {
    this.code = code;
    return this;
  }

  /**
   * РљРѕРґ РѕС€РёР±РєРё РґР»СЏ РїРѕР»СЏ
   * @return code
   */
  
  @Schema(name = "code", description = "РљРѕРґ РѕС€РёР±РєРё РґР»СЏ РїРѕР»СЏ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    Error1ErrorDetailsInner error1ErrorDetailsInner = (Error1ErrorDetailsInner) o;
    return Objects.equals(this.field, error1ErrorDetailsInner.field) &&
        Objects.equals(this.message, error1ErrorDetailsInner.message) &&
        Objects.equals(this.code, error1ErrorDetailsInner.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, message, code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error1ErrorDetailsInner {\n");
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

