package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * CharacterSwitchRequestSessionContext
 */

@JsonTypeName("CharacterSwitchRequest_sessionContext")

public class CharacterSwitchRequestSessionContext {

  private UUID sessionId;

  private UUID currentCharacterId;

  private @Nullable String location;

  private @Nullable Boolean inCombat;

  @Valid
  private List<String> pendingQuests = new ArrayList<>();

  public CharacterSwitchRequestSessionContext() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSwitchRequestSessionContext(UUID sessionId, UUID currentCharacterId) {
    this.sessionId = sessionId;
    this.currentCharacterId = currentCharacterId;
  }

  public CharacterSwitchRequestSessionContext sessionId(UUID sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @NotNull @Valid 
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sessionId")
  public UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(UUID sessionId) {
    this.sessionId = sessionId;
  }

  public CharacterSwitchRequestSessionContext currentCharacterId(UUID currentCharacterId) {
    this.currentCharacterId = currentCharacterId;
    return this;
  }

  /**
   * Get currentCharacterId
   * @return currentCharacterId
   */
  @NotNull @Valid 
  @Schema(name = "currentCharacterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentCharacterId")
  public UUID getCurrentCharacterId() {
    return currentCharacterId;
  }

  public void setCurrentCharacterId(UUID currentCharacterId) {
    this.currentCharacterId = currentCharacterId;
  }

  public CharacterSwitchRequestSessionContext location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public CharacterSwitchRequestSessionContext inCombat(@Nullable Boolean inCombat) {
    this.inCombat = inCombat;
    return this;
  }

  /**
   * Get inCombat
   * @return inCombat
   */
  
  @Schema(name = "inCombat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inCombat")
  public @Nullable Boolean getInCombat() {
    return inCombat;
  }

  public void setInCombat(@Nullable Boolean inCombat) {
    this.inCombat = inCombat;
  }

  public CharacterSwitchRequestSessionContext pendingQuests(List<String> pendingQuests) {
    this.pendingQuests = pendingQuests;
    return this;
  }

  public CharacterSwitchRequestSessionContext addPendingQuestsItem(String pendingQuestsItem) {
    if (this.pendingQuests == null) {
      this.pendingQuests = new ArrayList<>();
    }
    this.pendingQuests.add(pendingQuestsItem);
    return this;
  }

  /**
   * Get pendingQuests
   * @return pendingQuests
   */
  
  @Schema(name = "pendingQuests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pendingQuests")
  public List<String> getPendingQuests() {
    return pendingQuests;
  }

  public void setPendingQuests(List<String> pendingQuests) {
    this.pendingQuests = pendingQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSwitchRequestSessionContext characterSwitchRequestSessionContext = (CharacterSwitchRequestSessionContext) o;
    return Objects.equals(this.sessionId, characterSwitchRequestSessionContext.sessionId) &&
        Objects.equals(this.currentCharacterId, characterSwitchRequestSessionContext.currentCharacterId) &&
        Objects.equals(this.location, characterSwitchRequestSessionContext.location) &&
        Objects.equals(this.inCombat, characterSwitchRequestSessionContext.inCombat) &&
        Objects.equals(this.pendingQuests, characterSwitchRequestSessionContext.pendingQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, currentCharacterId, location, inCombat, pendingQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSwitchRequestSessionContext {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    currentCharacterId: ").append(toIndentedString(currentCharacterId)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    inCombat: ").append(toIndentedString(inCombat)).append("\n");
    sb.append("    pendingQuests: ").append(toIndentedString(pendingQuests)).append("\n");
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

