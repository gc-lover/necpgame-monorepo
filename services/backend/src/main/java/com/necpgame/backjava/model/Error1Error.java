package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Error1ErrorDetailsInner;
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
 * Error1Error
 */

@JsonTypeName("Error_1_error")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class Error1Error {

  private String code;

  private String message;

  @Valid
  private List<@Valid Error1ErrorDetailsInner> details = new ArrayList<>();

  public Error1Error() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Error1Error(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public Error1Error code(String code) {
    this.code = code;
    return this;
  }

  /**
   * РљРѕРґ РѕС€РёР±РєРё РґР»СЏ РїСЂРѕРіСЂР°РјРјРЅРѕР№ РѕР±СЂР°Р±РѕС‚РєРё
   * @return code
   */
  @NotNull 
  @Schema(name = "code", example = "VALIDATION_ERROR", description = "РљРѕРґ РѕС€РёР±РєРё РґР»СЏ РїСЂРѕРіСЂР°РјРјРЅРѕР№ РѕР±СЂР°Р±РѕС‚РєРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Error1Error message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Р§РµР»РѕРІРµРєРѕС‡РёС‚Р°РµРјРѕРµ СЃРѕРѕР±С‰РµРЅРёРµ РѕР± РѕС€РёР±РєРµ
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "РќРµРІРµСЂРЅС‹Рµ РїР°СЂР°РјРµС‚СЂС‹ Р·Р°РїСЂРѕСЃР°", description = "Р§РµР»РѕРІРµРєРѕС‡РёС‚Р°РµРјРѕРµ СЃРѕРѕР±С‰РµРЅРёРµ РѕР± РѕС€РёР±РєРµ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Error1Error details(List<@Valid Error1ErrorDetailsInner> details) {
    this.details = details;
    return this;
  }

  public Error1Error addDetailsItem(Error1ErrorDetailsInner detailsItem) {
    if (this.details == null) {
      this.details = new ArrayList<>();
    }
    this.details.add(detailsItem);
    return this;
  }

  /**
   * Р”РµС‚Р°Р»СЊРЅР°СЏ РёРЅС„РѕСЂРјР°С†РёСЏ РѕР± РѕС€РёР±РєРµ (РѕРїС†РёРѕРЅР°Р»СЊРЅРѕ)
   * @return details
   */
  @Valid 
  @Schema(name = "details", description = "Р”РµС‚Р°Р»СЊРЅР°СЏ РёРЅС„РѕСЂРјР°С†РёСЏ РѕР± РѕС€РёР±РєРµ (РѕРїС†РёРѕРЅР°Р»СЊРЅРѕ)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public List<@Valid Error1ErrorDetailsInner> getDetails() {
    return details;
  }

  public void setDetails(List<@Valid Error1ErrorDetailsInner> details) {
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
    Error1Error error1Error = (Error1Error) o;
    return Objects.equals(this.code, error1Error.code) &&
        Objects.equals(this.message, error1Error.message) &&
        Objects.equals(this.details, error1Error.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, message, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error1Error {\n");
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

