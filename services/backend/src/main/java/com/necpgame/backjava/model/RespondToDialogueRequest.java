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
 * RespondToDialogueRequest
 */

@JsonTypeName("respondToDialogue_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:49:00.930667100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class RespondToDialogueRequest {

  private UUID characterId;

  private String optionId;

  public RespondToDialogueRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RespondToDialogueRequest(UUID characterId, String optionId) {
    this.characterId = characterId;
    this.optionId = optionId;
  }

  public RespondToDialogueRequest characterId(UUID characterId) {
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

  public RespondToDialogueRequest optionId(String optionId) {
    this.optionId = optionId;
    return this;
  }

  /**
   * Get optionId
   * @return optionId
   */
  @NotNull 
  @Schema(name = "optionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("optionId")
  public String getOptionId() {
    return optionId;
  }

  public void setOptionId(String optionId) {
    this.optionId = optionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RespondToDialogueRequest respondToDialogueRequest = (RespondToDialogueRequest) o;
    return Objects.equals(this.characterId, respondToDialogueRequest.characterId) &&
        Objects.equals(this.optionId, respondToDialogueRequest.optionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, optionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RespondToDialogueRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    optionId: ").append(toIndentedString(optionId)).append("\n");
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


