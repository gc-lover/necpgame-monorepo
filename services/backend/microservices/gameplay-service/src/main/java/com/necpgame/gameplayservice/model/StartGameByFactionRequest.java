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
 * StartGameByFactionRequest
 */

@JsonTypeName("startGameByFaction_request")

public class StartGameByFactionRequest {

  private String characterId;

  private String factionId;

  public StartGameByFactionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartGameByFactionRequest(String characterId, String factionId) {
    this.characterId = characterId;
    this.factionId = factionId;
  }

  public StartGameByFactionRequest characterId(String characterId) {
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

  public StartGameByFactionRequest factionId(String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @NotNull 
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("faction_id")
  public String getFactionId() {
    return factionId;
  }

  public void setFactionId(String factionId) {
    this.factionId = factionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartGameByFactionRequest startGameByFactionRequest = (StartGameByFactionRequest) o;
    return Objects.equals(this.characterId, startGameByFactionRequest.characterId) &&
        Objects.equals(this.factionId, startGameByFactionRequest.factionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, factionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartGameByFactionRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
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

