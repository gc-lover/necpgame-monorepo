package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CharacterAttributesAttributes;
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
 * CharacterAttributes
 */


public class CharacterAttributes {

  private @Nullable UUID characterId;

  private @Nullable Integer unspentPoints;

  private @Nullable CharacterAttributesAttributes attributes;

  public CharacterAttributes characterId(@Nullable UUID characterId) {
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

  public CharacterAttributes unspentPoints(@Nullable Integer unspentPoints) {
    this.unspentPoints = unspentPoints;
    return this;
  }

  /**
   * Get unspentPoints
   * @return unspentPoints
   */
  
  @Schema(name = "unspent_points", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unspent_points")
  public @Nullable Integer getUnspentPoints() {
    return unspentPoints;
  }

  public void setUnspentPoints(@Nullable Integer unspentPoints) {
    this.unspentPoints = unspentPoints;
  }

  public CharacterAttributes attributes(@Nullable CharacterAttributesAttributes attributes) {
    this.attributes = attributes;
    return this;
  }

  /**
   * Get attributes
   * @return attributes
   */
  @Valid 
  @Schema(name = "attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public @Nullable CharacterAttributesAttributes getAttributes() {
    return attributes;
  }

  public void setAttributes(@Nullable CharacterAttributesAttributes attributes) {
    this.attributes = attributes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterAttributes characterAttributes = (CharacterAttributes) o;
    return Objects.equals(this.characterId, characterAttributes.characterId) &&
        Objects.equals(this.unspentPoints, characterAttributes.unspentPoints) &&
        Objects.equals(this.attributes, characterAttributes.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, unspentPoints, attributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterAttributes {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    unspentPoints: ").append(toIndentedString(unspentPoints)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

