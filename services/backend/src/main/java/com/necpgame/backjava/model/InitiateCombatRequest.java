package com.necpgame.backjava.model;

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
 * InitiateCombatRequest
 */

@JsonTypeName("initiateCombat_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:00.452540100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class InitiateCombatRequest {

  private UUID characterId;

  private String targetId;

  private @Nullable String locationId;

  public InitiateCombatRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InitiateCombatRequest(UUID characterId, String targetId) {
    this.characterId = characterId;
    this.targetId = targetId;
  }

  public InitiateCombatRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public InitiateCombatRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * ID РІСЂР°РіР°/NPC
   * @return targetId
   */
  @NotNull 
  @Schema(name = "targetId", description = "ID РІСЂР°РіР°/NPC", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetId")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public InitiateCombatRequest locationId(@Nullable String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  
  @Schema(name = "locationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locationId")
  public @Nullable String getLocationId() {
    return locationId;
  }

  public void setLocationId(@Nullable String locationId) {
    this.locationId = locationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitiateCombatRequest initiateCombatRequest = (InitiateCombatRequest) o;
    return Objects.equals(this.characterId, initiateCombatRequest.characterId) &&
        Objects.equals(this.targetId, initiateCombatRequest.targetId) &&
        Objects.equals(this.locationId, initiateCombatRequest.locationId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, locationId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitiateCombatRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
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

