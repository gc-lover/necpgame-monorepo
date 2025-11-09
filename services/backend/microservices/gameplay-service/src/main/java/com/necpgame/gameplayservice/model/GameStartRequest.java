package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * GameStartRequest
 */


public class GameStartRequest {

  private UUID characterId;

  private Boolean skipTutorial = false;

  public GameStartRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameStartRequest(UUID characterId) {
    this.characterId = characterId;
  }

  public GameStartRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID созданного персонажа
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", example = "550e8400-e29b-41d4-a716-446655440000", description = "ID созданного персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public GameStartRequest skipTutorial(Boolean skipTutorial) {
    this.skipTutorial = skipTutorial;
    return this;
  }

  /**
   * Пропустить туториал
   * @return skipTutorial
   */
  
  @Schema(name = "skipTutorial", example = "false", description = "Пропустить туториал", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skipTutorial")
  public Boolean getSkipTutorial() {
    return skipTutorial;
  }

  public void setSkipTutorial(Boolean skipTutorial) {
    this.skipTutorial = skipTutorial;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameStartRequest gameStartRequest = (GameStartRequest) o;
    return Objects.equals(this.characterId, gameStartRequest.characterId) &&
        Objects.equals(this.skipTutorial, gameStartRequest.skipTutorial);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skipTutorial);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameStartRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skipTutorial: ").append(toIndentedString(skipTutorial)).append("\n");
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

