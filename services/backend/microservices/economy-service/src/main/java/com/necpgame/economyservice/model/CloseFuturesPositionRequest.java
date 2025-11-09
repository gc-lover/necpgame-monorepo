package com.necpgame.economyservice.model;

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
 * CloseFuturesPositionRequest
 */

@JsonTypeName("closeFuturesPosition_request")

public class CloseFuturesPositionRequest {

  private String characterId;

  private String positionId;

  public CloseFuturesPositionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CloseFuturesPositionRequest(String characterId, String positionId) {
    this.characterId = characterId;
    this.positionId = positionId;
  }

  public CloseFuturesPositionRequest characterId(String characterId) {
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

  public CloseFuturesPositionRequest positionId(String positionId) {
    this.positionId = positionId;
    return this;
  }

  /**
   * Get positionId
   * @return positionId
   */
  @NotNull 
  @Schema(name = "position_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("position_id")
  public String getPositionId() {
    return positionId;
  }

  public void setPositionId(String positionId) {
    this.positionId = positionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CloseFuturesPositionRequest closeFuturesPositionRequest = (CloseFuturesPositionRequest) o;
    return Objects.equals(this.characterId, closeFuturesPositionRequest.characterId) &&
        Objects.equals(this.positionId, closeFuturesPositionRequest.positionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, positionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseFuturesPositionRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    positionId: ").append(toIndentedString(positionId)).append("\n");
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

