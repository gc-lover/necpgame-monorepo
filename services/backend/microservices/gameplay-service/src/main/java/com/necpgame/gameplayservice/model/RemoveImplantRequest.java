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
 * RemoveImplantRequest
 */

@JsonTypeName("removeImplant_request")

public class RemoveImplantRequest {

  private String characterId;

  private String implantId;

  public RemoveImplantRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RemoveImplantRequest(String characterId, String implantId) {
    this.characterId = characterId;
    this.implantId = implantId;
  }

  public RemoveImplantRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public RemoveImplantRequest implantId(String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  @NotNull 
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("implant_id")
  public String getImplantId() {
    return implantId;
  }

  public void setImplantId(String implantId) {
    this.implantId = implantId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoveImplantRequest removeImplantRequest = (RemoveImplantRequest) o;
    return Objects.equals(this.characterId, removeImplantRequest.characterId) &&
        Objects.equals(this.implantId, removeImplantRequest.implantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, implantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoveImplantRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
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

