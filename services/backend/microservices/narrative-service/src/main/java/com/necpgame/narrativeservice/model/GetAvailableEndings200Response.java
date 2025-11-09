package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.Ending;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetAvailableEndings200Response
 */

@JsonTypeName("getAvailableEndings_200_response")

public class GetAvailableEndings200Response {

  private @Nullable String characterId;

  @Valid
  private List<@Valid Ending> availableEndings = new ArrayList<>();

  public GetAvailableEndings200Response characterId(@Nullable String characterId) {
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

  public GetAvailableEndings200Response availableEndings(List<@Valid Ending> availableEndings) {
    this.availableEndings = availableEndings;
    return this;
  }

  public GetAvailableEndings200Response addAvailableEndingsItem(Ending availableEndingsItem) {
    if (this.availableEndings == null) {
      this.availableEndings = new ArrayList<>();
    }
    this.availableEndings.add(availableEndingsItem);
    return this;
  }

  /**
   * Get availableEndings
   * @return availableEndings
   */
  @Valid 
  @Schema(name = "available_endings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_endings")
  public List<@Valid Ending> getAvailableEndings() {
    return availableEndings;
  }

  public void setAvailableEndings(List<@Valid Ending> availableEndings) {
    this.availableEndings = availableEndings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableEndings200Response getAvailableEndings200Response = (GetAvailableEndings200Response) o;
    return Objects.equals(this.characterId, getAvailableEndings200Response.characterId) &&
        Objects.equals(this.availableEndings, getAvailableEndings200Response.availableEndings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, availableEndings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableEndings200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    availableEndings: ").append(toIndentedString(availableEndings)).append("\n");
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

