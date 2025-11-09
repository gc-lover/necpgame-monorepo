package com.necpgame.adminservice.model;

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
 * CalculateRomanceCompatibilityRequest
 */

@JsonTypeName("calculateRomanceCompatibility_request")

public class CalculateRomanceCompatibilityRequest {

  private @Nullable UUID characterId;

  private @Nullable String npcId;

  private @Nullable Object characterAttributes;

  private @Nullable Object npcPersonality;

  public CalculateRomanceCompatibilityRequest characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public CalculateRomanceCompatibilityRequest npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public CalculateRomanceCompatibilityRequest characterAttributes(@Nullable Object characterAttributes) {
    this.characterAttributes = characterAttributes;
    return this;
  }

  /**
   * Get characterAttributes
   * @return characterAttributes
   */
  
  @Schema(name = "character_attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_attributes")
  public @Nullable Object getCharacterAttributes() {
    return characterAttributes;
  }

  public void setCharacterAttributes(@Nullable Object characterAttributes) {
    this.characterAttributes = characterAttributes;
  }

  public CalculateRomanceCompatibilityRequest npcPersonality(@Nullable Object npcPersonality) {
    this.npcPersonality = npcPersonality;
    return this;
  }

  /**
   * Get npcPersonality
   * @return npcPersonality
   */
  
  @Schema(name = "npc_personality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_personality")
  public @Nullable Object getNpcPersonality() {
    return npcPersonality;
  }

  public void setNpcPersonality(@Nullable Object npcPersonality) {
    this.npcPersonality = npcPersonality;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateRomanceCompatibilityRequest calculateRomanceCompatibilityRequest = (CalculateRomanceCompatibilityRequest) o;
    return Objects.equals(this.characterId, calculateRomanceCompatibilityRequest.characterId) &&
        Objects.equals(this.npcId, calculateRomanceCompatibilityRequest.npcId) &&
        Objects.equals(this.characterAttributes, calculateRomanceCompatibilityRequest.characterAttributes) &&
        Objects.equals(this.npcPersonality, calculateRomanceCompatibilityRequest.npcPersonality);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, npcId, characterAttributes, npcPersonality);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateRomanceCompatibilityRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    characterAttributes: ").append(toIndentedString(characterAttributes)).append("\n");
    sb.append("    npcPersonality: ").append(toIndentedString(npcPersonality)).append("\n");
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

