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
 * JoinFactionWarRequest
 */

@JsonTypeName("joinFactionWar_request")

public class JoinFactionWarRequest {

  private String characterId;

  private String side;

  public JoinFactionWarRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public JoinFactionWarRequest(String characterId, String side) {
    this.characterId = characterId;
    this.side = side;
  }

  public JoinFactionWarRequest characterId(String characterId) {
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

  public JoinFactionWarRequest side(String side) {
    this.side = side;
    return this;
  }

  /**
   * Get side
   * @return side
   */
  @NotNull 
  @Schema(name = "side", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("side")
  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JoinFactionWarRequest joinFactionWarRequest = (JoinFactionWarRequest) o;
    return Objects.equals(this.characterId, joinFactionWarRequest.characterId) &&
        Objects.equals(this.side, joinFactionWarRequest.side);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, side);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JoinFactionWarRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
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

