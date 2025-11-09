package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterSlotState;
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
 * CharacterDeleteResponse
 */


public class CharacterDeleteResponse {

  private UUID characterId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime deletedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime canRestoreUntil;

  private @Nullable CharacterSlotState slots;

  public CharacterDeleteResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterDeleteResponse(UUID characterId, OffsetDateTime deletedAt, OffsetDateTime canRestoreUntil) {
    this.characterId = characterId;
    this.deletedAt = deletedAt;
    this.canRestoreUntil = canRestoreUntil;
  }

  public CharacterDeleteResponse characterId(UUID characterId) {
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

  public CharacterDeleteResponse deletedAt(OffsetDateTime deletedAt) {
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

  public CharacterDeleteResponse canRestoreUntil(OffsetDateTime canRestoreUntil) {
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

  public CharacterDeleteResponse slots(@Nullable CharacterSlotState slots) {
    this.slots = slots;
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slots")
  public @Nullable CharacterSlotState getSlots() {
    return slots;
  }

  public void setSlots(@Nullable CharacterSlotState slots) {
    this.slots = slots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterDeleteResponse characterDeleteResponse = (CharacterDeleteResponse) o;
    return Objects.equals(this.characterId, characterDeleteResponse.characterId) &&
        Objects.equals(this.deletedAt, characterDeleteResponse.deletedAt) &&
        Objects.equals(this.canRestoreUntil, characterDeleteResponse.canRestoreUntil) &&
        Objects.equals(this.slots, characterDeleteResponse.slots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, deletedAt, canRestoreUntil, slots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterDeleteResponse {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    deletedAt: ").append(toIndentedString(deletedAt)).append("\n");
    sb.append("    canRestoreUntil: ").append(toIndentedString(canRestoreUntil)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
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

