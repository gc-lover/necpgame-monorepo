package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterCreatedEventPayload
 */

@JsonTypeName("CharacterCreatedEvent_payload")

public class CharacterCreatedEventPayload {

  private UUID characterId;

  private UUID accountId;

  private String name;

  private String origin;

  private String characterClass;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  public CharacterCreatedEventPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterCreatedEventPayload(UUID characterId, UUID accountId, String name, String origin, String characterClass, OffsetDateTime createdAt) {
    this.characterId = characterId;
    this.accountId = accountId;
    this.name = name;
    this.origin = origin;
    this.characterClass = characterClass;
    this.createdAt = createdAt;
  }

  public CharacterCreatedEventPayload characterId(UUID characterId) {
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

  public CharacterCreatedEventPayload accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @NotNull @Valid 
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accountId")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public CharacterCreatedEventPayload name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CharacterCreatedEventPayload origin(String origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  @NotNull 
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("origin")
  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public CharacterCreatedEventPayload characterClass(String characterClass) {
    this.characterClass = characterClass;
    return this;
  }

  /**
   * Get characterClass
   * @return characterClass
   */
  @NotNull 
  @Schema(name = "characterClass", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterClass")
  public String getCharacterClass() {
    return characterClass;
  }

  public void setCharacterClass(String characterClass) {
    this.characterClass = characterClass;
  }

  public CharacterCreatedEventPayload createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterCreatedEventPayload characterCreatedEventPayload = (CharacterCreatedEventPayload) o;
    return Objects.equals(this.characterId, characterCreatedEventPayload.characterId) &&
        Objects.equals(this.accountId, characterCreatedEventPayload.accountId) &&
        Objects.equals(this.name, characterCreatedEventPayload.name) &&
        Objects.equals(this.origin, characterCreatedEventPayload.origin) &&
        Objects.equals(this.characterClass, characterCreatedEventPayload.characterClass) &&
        Objects.equals(this.createdAt, characterCreatedEventPayload.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, accountId, name, origin, characterClass, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterCreatedEventPayload {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    characterClass: ").append(toIndentedString(characterClass)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

