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
 * CharacterRestoredEventPayload
 */

@JsonTypeName("CharacterRestoredEvent_payload")

public class CharacterRestoredEventPayload {

  private UUID characterId;

  private UUID accountId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime restoredAt;

  public CharacterRestoredEventPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterRestoredEventPayload(UUID characterId, UUID accountId, OffsetDateTime restoredAt) {
    this.characterId = characterId;
    this.accountId = accountId;
    this.restoredAt = restoredAt;
  }

  public CharacterRestoredEventPayload characterId(UUID characterId) {
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

  public CharacterRestoredEventPayload accountId(UUID accountId) {
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

  public CharacterRestoredEventPayload restoredAt(OffsetDateTime restoredAt) {
    this.restoredAt = restoredAt;
    return this;
  }

  /**
   * Get restoredAt
   * @return restoredAt
   */
  @NotNull @Valid 
  @Schema(name = "restoredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("restoredAt")
  public OffsetDateTime getRestoredAt() {
    return restoredAt;
  }

  public void setRestoredAt(OffsetDateTime restoredAt) {
    this.restoredAt = restoredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterRestoredEventPayload characterRestoredEventPayload = (CharacterRestoredEventPayload) o;
    return Objects.equals(this.characterId, characterRestoredEventPayload.characterId) &&
        Objects.equals(this.accountId, characterRestoredEventPayload.accountId) &&
        Objects.equals(this.restoredAt, characterRestoredEventPayload.restoredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, accountId, restoredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterRestoredEventPayload {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    restoredAt: ").append(toIndentedString(restoredAt)).append("\n");
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

