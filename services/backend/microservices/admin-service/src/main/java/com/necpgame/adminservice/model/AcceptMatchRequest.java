package com.necpgame.adminservice.model;

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
 * AcceptMatchRequest
 */

@JsonTypeName("acceptMatch_request")

public class AcceptMatchRequest {

  private String characterId;

  private String matchId;

  public AcceptMatchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AcceptMatchRequest(String characterId, String matchId) {
    this.characterId = characterId;
    this.matchId = matchId;
  }

  public AcceptMatchRequest characterId(String characterId) {
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

  public AcceptMatchRequest matchId(String matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  @NotNull 
  @Schema(name = "match_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("match_id")
  public String getMatchId() {
    return matchId;
  }

  public void setMatchId(String matchId) {
    this.matchId = matchId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptMatchRequest acceptMatchRequest = (AcceptMatchRequest) o;
    return Objects.equals(this.characterId, acceptMatchRequest.characterId) &&
        Objects.equals(this.matchId, acceptMatchRequest.matchId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, matchId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptMatchRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
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

