package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * DeletePlayerCharacter200Response
 */

@JsonTypeName("deletePlayerCharacter_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class DeletePlayerCharacter200Response {

  private @Nullable String characterId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime deletedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime restoreDeadline;

  private @Nullable String message;

  public DeletePlayerCharacter200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public DeletePlayerCharacter200Response deletedAt(@Nullable OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
    return this;
  }

  /**
   * Get deletedAt
   * @return deletedAt
   */
  @Valid 
  @Schema(name = "deleted_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deleted_at")
  public @Nullable OffsetDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(@Nullable OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public DeletePlayerCharacter200Response restoreDeadline(@Nullable OffsetDateTime restoreDeadline) {
    this.restoreDeadline = restoreDeadline;
    return this;
  }

  /**
   * Get restoreDeadline
   * @return restoreDeadline
   */
  @Valid 
  @Schema(name = "restore_deadline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restore_deadline")
  public @Nullable OffsetDateTime getRestoreDeadline() {
    return restoreDeadline;
  }

  public void setRestoreDeadline(@Nullable OffsetDateTime restoreDeadline) {
    this.restoreDeadline = restoreDeadline;
  }

  public DeletePlayerCharacter200Response message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeletePlayerCharacter200Response deletePlayerCharacter200Response = (DeletePlayerCharacter200Response) o;
    return Objects.equals(this.characterId, deletePlayerCharacter200Response.characterId) &&
        Objects.equals(this.deletedAt, deletePlayerCharacter200Response.deletedAt) &&
        Objects.equals(this.restoreDeadline, deletePlayerCharacter200Response.restoreDeadline) &&
        Objects.equals(this.message, deletePlayerCharacter200Response.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, deletedAt, restoreDeadline, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeletePlayerCharacter200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    deletedAt: ").append(toIndentedString(deletedAt)).append("\n");
    sb.append("    restoreDeadline: ").append(toIndentedString(restoreDeadline)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

