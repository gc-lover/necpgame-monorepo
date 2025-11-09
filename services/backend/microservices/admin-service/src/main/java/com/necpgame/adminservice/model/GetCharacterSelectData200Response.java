package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.CharacterSelectInfo;
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
 * GetCharacterSelectData200Response
 */

@JsonTypeName("getCharacterSelectData_200_response")

public class GetCharacterSelectData200Response {

  @Valid
  private List<@Valid CharacterSelectInfo> characters = new ArrayList<>();

  private @Nullable Integer maxSlots;

  public GetCharacterSelectData200Response characters(List<@Valid CharacterSelectInfo> characters) {
    this.characters = characters;
    return this;
  }

  public GetCharacterSelectData200Response addCharactersItem(CharacterSelectInfo charactersItem) {
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
  public List<@Valid CharacterSelectInfo> getCharacters() {
    return characters;
  }

  public void setCharacters(List<@Valid CharacterSelectInfo> characters) {
    this.characters = characters;
  }

  public GetCharacterSelectData200Response maxSlots(@Nullable Integer maxSlots) {
    this.maxSlots = maxSlots;
    return this;
  }

  /**
   * Get maxSlots
   * @return maxSlots
   */
  
  @Schema(name = "max_slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_slots")
  public @Nullable Integer getMaxSlots() {
    return maxSlots;
  }

  public void setMaxSlots(@Nullable Integer maxSlots) {
    this.maxSlots = maxSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCharacterSelectData200Response getCharacterSelectData200Response = (GetCharacterSelectData200Response) o;
    return Objects.equals(this.characters, getCharacterSelectData200Response.characters) &&
        Objects.equals(this.maxSlots, getCharacterSelectData200Response.maxSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characters, maxSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterSelectData200Response {\n");
    sb.append("    characters: ").append(toIndentedString(characters)).append("\n");
    sb.append("    maxSlots: ").append(toIndentedString(maxSlots)).append("\n");
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

