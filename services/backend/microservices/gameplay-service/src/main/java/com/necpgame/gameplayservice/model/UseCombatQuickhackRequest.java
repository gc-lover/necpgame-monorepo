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
 * UseCombatQuickhackRequest
 */

@JsonTypeName("useCombatQuickhack_request")

public class UseCombatQuickhackRequest {

  private String characterId;

  private String targetId;

  private String quickhackId;

  public UseCombatQuickhackRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UseCombatQuickhackRequest(String characterId, String targetId, String quickhackId) {
    this.characterId = characterId;
    this.targetId = targetId;
    this.quickhackId = quickhackId;
  }

  public UseCombatQuickhackRequest characterId(String characterId) {
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

  public UseCombatQuickhackRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_id")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public UseCombatQuickhackRequest quickhackId(String quickhackId) {
    this.quickhackId = quickhackId;
    return this;
  }

  /**
   * Get quickhackId
   * @return quickhackId
   */
  @NotNull 
  @Schema(name = "quickhack_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quickhack_id")
  public String getQuickhackId() {
    return quickhackId;
  }

  public void setQuickhackId(String quickhackId) {
    this.quickhackId = quickhackId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UseCombatQuickhackRequest useCombatQuickhackRequest = (UseCombatQuickhackRequest) o;
    return Objects.equals(this.characterId, useCombatQuickhackRequest.characterId) &&
        Objects.equals(this.targetId, useCombatQuickhackRequest.targetId) &&
        Objects.equals(this.quickhackId, useCombatQuickhackRequest.quickhackId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, quickhackId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UseCombatQuickhackRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    quickhackId: ").append(toIndentedString(quickhackId)).append("\n");
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

