package com.necpgame.narrativeservice.model;

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
 * CompleteCorpoTowerRaidRequest
 */

@JsonTypeName("completeCorpoTowerRaid_request")

public class CompleteCorpoTowerRaidRequest {

  private String characterId;

  public CompleteCorpoTowerRaidRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompleteCorpoTowerRaidRequest(String characterId) {
    this.characterId = characterId;
  }

  public CompleteCorpoTowerRaidRequest characterId(String characterId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteCorpoTowerRaidRequest completeCorpoTowerRaidRequest = (CompleteCorpoTowerRaidRequest) o;
    return Objects.equals(this.characterId, completeCorpoTowerRaidRequest.characterId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteCorpoTowerRaidRequest {\n");
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

