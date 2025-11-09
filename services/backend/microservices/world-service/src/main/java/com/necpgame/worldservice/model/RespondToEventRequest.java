package com.necpgame.worldservice.model;

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
 * RespondToEventRequest
 */

@JsonTypeName("respondToEvent_request")

public class RespondToEventRequest {

  private UUID characterId;

  private String optionId;

  public RespondToEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RespondToEventRequest(UUID characterId, String optionId) {
    this.characterId = characterId;
    this.optionId = optionId;
  }

  public RespondToEventRequest characterId(UUID characterId) {
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

  public RespondToEventRequest optionId(String optionId) {
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
    RespondToEventRequest respondToEventRequest = (RespondToEventRequest) o;
    return Objects.equals(this.characterId, respondToEventRequest.characterId) &&
        Objects.equals(this.optionId, respondToEventRequest.optionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, optionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RespondToEventRequest {\n");
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

