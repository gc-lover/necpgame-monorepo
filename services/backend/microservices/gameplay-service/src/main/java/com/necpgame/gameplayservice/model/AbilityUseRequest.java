package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.AbilityUseRequestTargetPosition;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AbilityUseRequest
 */


public class AbilityUseRequest {

  private String characterId;

  private String abilityId;

  private @Nullable String targetId;

  private @Nullable AbilityUseRequestTargetPosition targetPosition;

  public AbilityUseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AbilityUseRequest(String characterId, String abilityId) {
    this.characterId = characterId;
    this.abilityId = abilityId;
  }

  public AbilityUseRequest characterId(String characterId) {
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

  public AbilityUseRequest abilityId(String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  @NotNull 
  @Schema(name = "ability_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ability_id")
  public String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(String abilityId) {
    this.abilityId = abilityId;
  }

  public AbilityUseRequest targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * ID цели (если применимо)
   * @return targetId
   */
  
  @Schema(name = "target_id", description = "ID цели (если применимо)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_id")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public AbilityUseRequest targetPosition(@Nullable AbilityUseRequestTargetPosition targetPosition) {
    this.targetPosition = targetPosition;
    return this;
  }

  /**
   * Get targetPosition
   * @return targetPosition
   */
  @Valid 
  @Schema(name = "target_position", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_position")
  public @Nullable AbilityUseRequestTargetPosition getTargetPosition() {
    return targetPosition;
  }

  public void setTargetPosition(@Nullable AbilityUseRequestTargetPosition targetPosition) {
    this.targetPosition = targetPosition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityUseRequest abilityUseRequest = (AbilityUseRequest) o;
    return Objects.equals(this.characterId, abilityUseRequest.characterId) &&
        Objects.equals(this.abilityId, abilityUseRequest.abilityId) &&
        Objects.equals(this.targetId, abilityUseRequest.targetId) &&
        Objects.equals(this.targetPosition, abilityUseRequest.targetPosition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, abilityId, targetId, targetPosition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityUseRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    targetPosition: ").append(toIndentedString(targetPosition)).append("\n");
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

