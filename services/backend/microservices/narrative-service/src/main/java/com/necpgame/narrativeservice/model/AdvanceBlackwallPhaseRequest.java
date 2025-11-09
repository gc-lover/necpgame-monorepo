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
 * AdvanceBlackwallPhaseRequest
 */

@JsonTypeName("advanceBlackwallPhase_request")

public class AdvanceBlackwallPhaseRequest {

  private String characterId;

  public AdvanceBlackwallPhaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AdvanceBlackwallPhaseRequest(String characterId) {
    this.characterId = characterId;
  }

  public AdvanceBlackwallPhaseRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID лидера группы
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", description = "ID лидера группы", requiredMode = Schema.RequiredMode.REQUIRED)
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
    AdvanceBlackwallPhaseRequest advanceBlackwallPhaseRequest = (AdvanceBlackwallPhaseRequest) o;
    return Objects.equals(this.characterId, advanceBlackwallPhaseRequest.characterId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdvanceBlackwallPhaseRequest {\n");
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

