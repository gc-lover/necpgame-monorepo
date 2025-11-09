package com.necpgame.characterservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.characterservice.model.PlayerCharacter;
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
 * GetCharacters200Response
 */

@JsonTypeName("getCharacters_200_response")

public class GetCharacters200Response {

  @Valid
  private List<@Valid PlayerCharacter> characters = new ArrayList<>();

  private @Nullable Integer availableSlots;

  private @Nullable Integer totalSlots;

  public GetCharacters200Response characters(List<@Valid PlayerCharacter> characters) {
    this.characters = characters;
    return this;
  }

  public GetCharacters200Response addCharactersItem(PlayerCharacter charactersItem) {
    if (this.characters == null) {
      this.characters = new ArrayList<>();
    }
    this.characters.add(charactersItem);
    return this;
  }

  /**
   * Get characters
   * @return characters
   */
  @Valid 
  @Schema(name = "characters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characters")
  public List<@Valid PlayerCharacter> getCharacters() {
    return characters;
  }

  public void setCharacters(List<@Valid PlayerCharacter> characters) {
    this.characters = characters;
  }

  public GetCharacters200Response availableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
    return this;
  }

  /**
   * Get availableSlots
   * @return availableSlots
   */
  
  @Schema(name = "available_slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_slots")
  public @Nullable Integer getAvailableSlots() {
    return availableSlots;
  }

  public void setAvailableSlots(@Nullable Integer availableSlots) {
    this.availableSlots = availableSlots;
  }

  public GetCharacters200Response totalSlots(@Nullable Integer totalSlots) {
    this.totalSlots = totalSlots;
    return this;
  }

  /**
   * Get totalSlots
   * @return totalSlots
   */
  
  @Schema(name = "total_slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_slots")
  public @Nullable Integer getTotalSlots() {
    return totalSlots;
  }

  public void setTotalSlots(@Nullable Integer totalSlots) {
    this.totalSlots = totalSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCharacters200Response getCharacters200Response = (GetCharacters200Response) o;
    return Objects.equals(this.characters, getCharacters200Response.characters) &&
        Objects.equals(this.availableSlots, getCharacters200Response.availableSlots) &&
        Objects.equals(this.totalSlots, getCharacters200Response.totalSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characters, availableSlots, totalSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacters200Response {\n");
    sb.append("    characters: ").append(toIndentedString(characters)).append("\n");
    sb.append("    availableSlots: ").append(toIndentedString(availableSlots)).append("\n");
    sb.append("    totalSlots: ").append(toIndentedString(totalSlots)).append("\n");
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

