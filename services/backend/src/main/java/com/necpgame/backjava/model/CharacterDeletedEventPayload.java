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
 * CharacterDeletedEventPayload
 */

@JsonTypeName("CharacterDeletedEvent_payload")

public class CharacterDeletedEventPayload {

  private UUID characterId;

  private UUID accountId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime deletedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime canRestoreUntil;

  public CharacterDeletedEventPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterDeletedEventPayload(UUID characterId, UUID accountId, OffsetDateTime deletedAt, OffsetDateTime canRestoreUntil) {
    this.characterId = characterId;
    this.accountId = accountId;
    this.deletedAt = deletedAt;
    this.canRestoreUntil = canRestoreUntil;
  }

  public CharacterDeletedEventPayload characterId(UUID characterId) {
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

  public CharacterDeletedEventPayload accountId(UUID accountId) {
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

  public CharacterDeletedEventPayload deletedAt(OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
    return this;
  }

  /**
   * Get deletedAt
   * @return deletedAt
   */
  @NotNull @Valid 
  @Schema(name = "deletedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deletedAt")
  public OffsetDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public CharacterDeletedEventPayload canRestoreUntil(OffsetDateTime canRestoreUntil) {
    this.canRestoreUntil = canRestoreUntil;
    return this;
  }

  /**
   * Get canRestoreUntil
   * @return canRestoreUntil
   */
  @NotNull @Valid 
  @Schema(name = "canRestoreUntil", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("canRestoreUntil")
  public OffsetDateTime getCanRestoreUntil() {
    return canRestoreUntil;
  }

  public void setCanRestoreUntil(OffsetDateTime canRestoreUntil) {
    this.canRestoreUntil = canRestoreUntil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterDeletedEventPayload characterDeletedEventPayload = (CharacterDeletedEventPayload) o;
    return Objects.equals(this.characterId, characterDeletedEventPayload.characterId) &&
        Objects.equals(this.accountId, characterDeletedEventPayload.accountId) &&
        Objects.equals(this.deletedAt, characterDeletedEventPayload.deletedAt) &&
        Objects.equals(this.canRestoreUntil, characterDeletedEventPayload.canRestoreUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, accountId, deletedAt, canRestoreUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterDeletedEventPayload {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    deletedAt: ").append(toIndentedString(deletedAt)).append("\n");
    sb.append("    canRestoreUntil: ").append(toIndentedString(canRestoreUntil)).append("\n");
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

