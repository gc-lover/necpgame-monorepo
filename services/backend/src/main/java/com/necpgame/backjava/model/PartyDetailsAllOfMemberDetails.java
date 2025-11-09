package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PartyDetailsAllOfMemberDetails
 */

@JsonTypeName("PartyDetails_allOf_member_details")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PartyDetailsAllOfMemberDetails {

  private @Nullable String characterId;

  private @Nullable String name;

  private @Nullable String propertyClass;

  private @Nullable Integer level;

  private @Nullable Boolean isOnline;

  public PartyDetailsAllOfMemberDetails characterId(@Nullable String characterId) {
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

  public PartyDetailsAllOfMemberDetails name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public PartyDetailsAllOfMemberDetails propertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * @return propertyClass
   */
  
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public PartyDetailsAllOfMemberDetails level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public PartyDetailsAllOfMemberDetails isOnline(@Nullable Boolean isOnline) {
    this.isOnline = isOnline;
    return this;
  }

  /**
   * Get isOnline
   * @return isOnline
   */
  
  @Schema(name = "is_online", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_online")
  public @Nullable Boolean getIsOnline() {
    return isOnline;
  }

  public void setIsOnline(@Nullable Boolean isOnline) {
    this.isOnline = isOnline;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyDetailsAllOfMemberDetails partyDetailsAllOfMemberDetails = (PartyDetailsAllOfMemberDetails) o;
    return Objects.equals(this.characterId, partyDetailsAllOfMemberDetails.characterId) &&
        Objects.equals(this.name, partyDetailsAllOfMemberDetails.name) &&
        Objects.equals(this.propertyClass, partyDetailsAllOfMemberDetails.propertyClass) &&
        Objects.equals(this.level, partyDetailsAllOfMemberDetails.level) &&
        Objects.equals(this.isOnline, partyDetailsAllOfMemberDetails.isOnline);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, name, propertyClass, level, isOnline);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyDetailsAllOfMemberDetails {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    isOnline: ").append(toIndentedString(isOnline)).append("\n");
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

