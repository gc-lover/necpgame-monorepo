package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.Relationship;
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
 * GetRelationships200Response
 */

@JsonTypeName("getRelationships_200_response")

public class GetRelationships200Response {

  private @Nullable String characterId;

  @Valid
  private List<@Valid Relationship> relationships = new ArrayList<>();

  public GetRelationships200Response characterId(@Nullable String characterId) {
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

  public GetRelationships200Response relationships(List<@Valid Relationship> relationships) {
    this.relationships = relationships;
    return this;
  }

  public GetRelationships200Response addRelationshipsItem(Relationship relationshipsItem) {
    if (this.relationships == null) {
      this.relationships = new ArrayList<>();
    }
    this.relationships.add(relationshipsItem);
    return this;
  }

  /**
   * Get relationships
   * @return relationships
   */
  @Valid 
  @Schema(name = "relationships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationships")
  public List<@Valid Relationship> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<@Valid Relationship> relationships) {
    this.relationships = relationships;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRelationships200Response getRelationships200Response = (GetRelationships200Response) o;
    return Objects.equals(this.characterId, getRelationships200Response.characterId) &&
        Objects.equals(this.relationships, getRelationships200Response.relationships);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, relationships);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRelationships200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    relationships: ").append(toIndentedString(relationships)).append("\n");
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

