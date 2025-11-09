package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReturnToGameRequest
 */

@JsonTypeName("returnToGame_request")

public class ReturnToGameRequest {

  private UUID characterId;

  public ReturnToGameRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReturnToGameRequest(UUID characterId) {
    this.characterId = characterId;
  }

  public ReturnToGameRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", example = "550e8400-e29b-41d4-a716-446655440000", description = "ID персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReturnToGameRequest returnToGameRequest = (ReturnToGameRequest) o;
    return Objects.equals(this.characterId, returnToGameRequest.characterId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReturnToGameRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
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

