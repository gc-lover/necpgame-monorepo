package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.CreateExtractionPointRequestLocation;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateExtractionPointRequest
 */

@JsonTypeName("createExtractionPoint_request")

public class CreateExtractionPointRequest {

  private String characterId;

  private CreateExtractionPointRequestLocation location;

  public CreateExtractionPointRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateExtractionPointRequest(String characterId, CreateExtractionPointRequestLocation location) {
    this.characterId = characterId;
    this.location = location;
  }

  public CreateExtractionPointRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CreateExtractionPointRequest location(CreateExtractionPointRequestLocation location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  @NotNull @Valid 
  @Schema(name = "location", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("location")
  public CreateExtractionPointRequestLocation getLocation() {
    return location;
  }

  public void setLocation(CreateExtractionPointRequestLocation location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateExtractionPointRequest createExtractionPointRequest = (CreateExtractionPointRequest) o;
    return Objects.equals(this.characterId, createExtractionPointRequest.characterId) &&
        Objects.equals(this.location, createExtractionPointRequest.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, location);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateExtractionPointRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
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

