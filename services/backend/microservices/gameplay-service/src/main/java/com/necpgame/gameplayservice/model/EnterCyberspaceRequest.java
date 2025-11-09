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
 * EnterCyberspaceRequest
 */

@JsonTypeName("enterCyberspace_request")

public class EnterCyberspaceRequest {

  private String characterId;

  private @Nullable String entryPoint;

  public EnterCyberspaceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EnterCyberspaceRequest(String characterId) {
    this.characterId = characterId;
  }

  public EnterCyberspaceRequest characterId(String characterId) {
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

  public EnterCyberspaceRequest entryPoint(@Nullable String entryPoint) {
    this.entryPoint = entryPoint;
    return this;
  }

  /**
   * Точка входа (location в реальном мире)
   * @return entryPoint
   */
  
  @Schema(name = "entry_point", description = "Точка входа (location в реальном мире)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entry_point")
  public @Nullable String getEntryPoint() {
    return entryPoint;
  }

  public void setEntryPoint(@Nullable String entryPoint) {
    this.entryPoint = entryPoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterCyberspaceRequest enterCyberspaceRequest = (EnterCyberspaceRequest) o;
    return Objects.equals(this.characterId, enterCyberspaceRequest.characterId) &&
        Objects.equals(this.entryPoint, enterCyberspaceRequest.entryPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, entryPoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterCyberspaceRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    entryPoint: ").append(toIndentedString(entryPoint)).append("\n");
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

