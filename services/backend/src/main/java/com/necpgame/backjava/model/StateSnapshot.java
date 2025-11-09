package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterState;
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
 * StateSnapshot
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class StateSnapshot {

  private @Nullable UUID snapshotId;

  private @Nullable UUID characterId;

  private @Nullable CharacterState state;

  private @Nullable String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Boolean canRestore;

  public StateSnapshot snapshotId(@Nullable UUID snapshotId) {
    this.snapshotId = snapshotId;
    return this;
  }

  /**
   * Get snapshotId
   * @return snapshotId
   */
  @Valid 
  @Schema(name = "snapshot_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("snapshot_id")
  public @Nullable UUID getSnapshotId() {
    return snapshotId;
  }

  public void setSnapshotId(@Nullable UUID snapshotId) {
    this.snapshotId = snapshotId;
  }

  public StateSnapshot characterId(@Nullable UUID characterId) {
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

  public StateSnapshot state(@Nullable CharacterState state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @Valid 
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable CharacterState getState() {
    return state;
  }

  public void setState(@Nullable CharacterState state) {
    this.state = state;
  }

  public StateSnapshot description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public StateSnapshot createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public StateSnapshot canRestore(@Nullable Boolean canRestore) {
    this.canRestore = canRestore;
    return this;
  }

  /**
   * Get canRestore
   * @return canRestore
   */
  
  @Schema(name = "can_restore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_restore")
  public @Nullable Boolean getCanRestore() {
    return canRestore;
  }

  public void setCanRestore(@Nullable Boolean canRestore) {
    this.canRestore = canRestore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateSnapshot stateSnapshot = (StateSnapshot) o;
    return Objects.equals(this.snapshotId, stateSnapshot.snapshotId) &&
        Objects.equals(this.characterId, stateSnapshot.characterId) &&
        Objects.equals(this.state, stateSnapshot.state) &&
        Objects.equals(this.description, stateSnapshot.description) &&
        Objects.equals(this.createdAt, stateSnapshot.createdAt) &&
        Objects.equals(this.canRestore, stateSnapshot.canRestore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(snapshotId, characterId, state, description, createdAt, canRestore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateSnapshot {\n");
    sb.append("    snapshotId: ").append(toIndentedString(snapshotId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    canRestore: ").append(toIndentedString(canRestore)).append("\n");
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

