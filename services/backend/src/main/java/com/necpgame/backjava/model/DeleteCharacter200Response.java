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
 * DeleteCharacter200Response
 */

@JsonTypeName("deleteCharacter_200_response")

public class DeleteCharacter200Response {

  private String message;

  public DeleteCharacter200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DeleteCharacter200Response(String message) {
    this.message = message;
  }

  public DeleteCharacter200Response message(String message) {
    this.message = message;
    return this;
  }

  /**
   * РЎРѕРѕР±С‰РµРЅРёРµ РѕР± СѓСЃРїРµС€РЅРѕРј СѓРґР°Р»РµРЅРёРё
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "Character deleted successfully", description = "РЎРѕРѕР±С‰РµРЅРёРµ РѕР± СѓСЃРїРµС€РЅРѕРј СѓРґР°Р»РµРЅРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeleteCharacter200Response deleteCharacter200Response = (DeleteCharacter200Response) o;
    return Objects.equals(this.message, deleteCharacter200Response.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeleteCharacter200Response {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

