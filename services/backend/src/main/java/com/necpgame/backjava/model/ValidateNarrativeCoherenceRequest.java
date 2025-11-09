package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ValidateNarrativeCoherenceRequest
 */

@JsonTypeName("validateNarrativeCoherence_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ValidateNarrativeCoherenceRequest {

  private @Nullable UUID characterId;

  private @Nullable String proposedAction;

  private JsonNullable<String> questId = JsonNullable.<String>undefined();

  public ValidateNarrativeCoherenceRequest characterId(@Nullable UUID characterId) {
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

  public ValidateNarrativeCoherenceRequest proposedAction(@Nullable String proposedAction) {
    this.proposedAction = proposedAction;
    return this;
  }

  /**
   * Get proposedAction
   * @return proposedAction
   */
  
  @Schema(name = "proposed_action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("proposed_action")
  public @Nullable String getProposedAction() {
    return proposedAction;
  }

  public void setProposedAction(@Nullable String proposedAction) {
    this.proposedAction = proposedAction;
  }

  public ValidateNarrativeCoherenceRequest questId(String questId) {
    this.questId = JsonNullable.of(questId);
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public JsonNullable<String> getQuestId() {
    return questId;
  }

  public void setQuestId(JsonNullable<String> questId) {
    this.questId = questId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidateNarrativeCoherenceRequest validateNarrativeCoherenceRequest = (ValidateNarrativeCoherenceRequest) o;
    return Objects.equals(this.characterId, validateNarrativeCoherenceRequest.characterId) &&
        Objects.equals(this.proposedAction, validateNarrativeCoherenceRequest.proposedAction) &&
        equalsNullable(this.questId, validateNarrativeCoherenceRequest.questId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, proposedAction, hashCodeNullable(questId));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidateNarrativeCoherenceRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    proposedAction: ").append(toIndentedString(proposedAction)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
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

