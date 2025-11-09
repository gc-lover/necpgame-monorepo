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
 * UnlockCodexEntryRequest
 */

@JsonTypeName("unlockCodexEntry_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class UnlockCodexEntryRequest {

  private @Nullable UUID characterId;

  private @Nullable String entryId;

  public UnlockCodexEntryRequest characterId(@Nullable UUID characterId) {
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

  public UnlockCodexEntryRequest entryId(@Nullable String entryId) {
    this.entryId = entryId;
    return this;
  }

  /**
   * Get entryId
   * @return entryId
   */
  
  @Schema(name = "entry_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entry_id")
  public @Nullable String getEntryId() {
    return entryId;
  }

  public void setEntryId(@Nullable String entryId) {
    this.entryId = entryId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnlockCodexEntryRequest unlockCodexEntryRequest = (UnlockCodexEntryRequest) o;
    return Objects.equals(this.characterId, unlockCodexEntryRequest.characterId) &&
        Objects.equals(this.entryId, unlockCodexEntryRequest.entryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, entryId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnlockCodexEntryRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    entryId: ").append(toIndentedString(entryId)).append("\n");
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

