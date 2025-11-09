package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterSwitchRequestSessionContext;
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
 * CharacterSwitchRequest
 */


public class CharacterSwitchRequest {

  private UUID characterId;

  private Boolean suppressNotifications = false;

  private CharacterSwitchRequestSessionContext sessionContext;

  public CharacterSwitchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSwitchRequest(UUID characterId, CharacterSwitchRequestSessionContext sessionContext) {
    this.characterId = characterId;
    this.sessionContext = sessionContext;
  }

  public CharacterSwitchRequest characterId(UUID characterId) {
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

  public CharacterSwitchRequest suppressNotifications(Boolean suppressNotifications) {
    this.suppressNotifications = suppressNotifications;
    return this;
  }

  /**
   * Get suppressNotifications
   * @return suppressNotifications
   */
  
  @Schema(name = "suppressNotifications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suppressNotifications")
  public Boolean getSuppressNotifications() {
    return suppressNotifications;
  }

  public void setSuppressNotifications(Boolean suppressNotifications) {
    this.suppressNotifications = suppressNotifications;
  }

  public CharacterSwitchRequest sessionContext(CharacterSwitchRequestSessionContext sessionContext) {
    this.sessionContext = sessionContext;
    return this;
  }

  /**
   * Get sessionContext
   * @return sessionContext
   */
  @NotNull @Valid 
  @Schema(name = "sessionContext", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sessionContext")
  public CharacterSwitchRequestSessionContext getSessionContext() {
    return sessionContext;
  }

  public void setSessionContext(CharacterSwitchRequestSessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSwitchRequest characterSwitchRequest = (CharacterSwitchRequest) o;
    return Objects.equals(this.characterId, characterSwitchRequest.characterId) &&
        Objects.equals(this.suppressNotifications, characterSwitchRequest.suppressNotifications) &&
        Objects.equals(this.sessionContext, characterSwitchRequest.sessionContext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, suppressNotifications, sessionContext);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSwitchRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    suppressNotifications: ").append(toIndentedString(suppressNotifications)).append("\n");
    sb.append("    sessionContext: ").append(toIndentedString(sessionContext)).append("\n");
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

