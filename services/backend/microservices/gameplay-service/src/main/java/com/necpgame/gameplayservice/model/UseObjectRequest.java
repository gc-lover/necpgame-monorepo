package com.necpgame.gameplayservice.model;

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
 * UseObjectRequest
 */

@JsonTypeName("useObject_request")

public class UseObjectRequest {

  private UUID characterId;

  private String locationId;

  private String objectId;

  public UseObjectRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UseObjectRequest(UUID characterId, String locationId, String objectId) {
    this.characterId = characterId;
    this.locationId = locationId;
    this.objectId = objectId;
  }

  public UseObjectRequest characterId(UUID characterId) {
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

  public UseObjectRequest locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull 
  @Schema(name = "locationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locationId")
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public UseObjectRequest objectId(String objectId) {
    this.objectId = objectId;
    return this;
  }

  /**
   * Get objectId
   * @return objectId
   */
  @NotNull 
  @Schema(name = "objectId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("objectId")
  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UseObjectRequest useObjectRequest = (UseObjectRequest) o;
    return Objects.equals(this.characterId, useObjectRequest.characterId) &&
        Objects.equals(this.locationId, useObjectRequest.locationId) &&
        Objects.equals(this.objectId, useObjectRequest.objectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, locationId, objectId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UseObjectRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
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

