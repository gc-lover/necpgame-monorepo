package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.authservice.model.GameCharacterSummary;
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
 * ListCharacters200Response
 */

@JsonTypeName("listCharacters_200_response")

public class ListCharacters200Response {

  @Valid
  private List<@Valid GameCharacterSummary> characters = new ArrayList<>();

  private Integer maxCharacters;

  private Integer currentCount;

  public ListCharacters200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ListCharacters200Response(List<@Valid GameCharacterSummary> characters, Integer maxCharacters, Integer currentCount) {
    this.characters = characters;
    this.maxCharacters = maxCharacters;
    this.currentCount = currentCount;
  }

  public ListCharacters200Response characters(List<@Valid GameCharacterSummary> characters) {
    this.characters = characters;
    return this;
  }

  public ListCharacters200Response addCharactersItem(GameCharacterSummary charactersItem) {
    if (this.characters == null) {
      this.characters = new ArrayList<>();
    }
    this.characters.add(charactersItem);
    return this;
  }

  /**
   * Список персонажей
   * @return characters
   */
  @NotNull @Valid 
  @Schema(name = "characters", description = "Список персонажей", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characters")
  public List<@Valid GameCharacterSummary> getCharacters() {
    return characters;
  }

  public void setCharacters(List<@Valid GameCharacterSummary> characters) {
    this.characters = characters;
  }

  public ListCharacters200Response maxCharacters(Integer maxCharacters) {
    this.maxCharacters = maxCharacters;
    return this;
  }

  /**
   * Максимальное количество персонажей на аккаунт
   * @return maxCharacters
   */
  @NotNull 
  @Schema(name = "max_characters", example = "5", description = "Максимальное количество персонажей на аккаунт", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("max_characters")
  public Integer getMaxCharacters() {
    return maxCharacters;
  }

  public void setMaxCharacters(Integer maxCharacters) {
    this.maxCharacters = maxCharacters;
  }

  public ListCharacters200Response currentCount(Integer currentCount) {
    this.currentCount = currentCount;
    return this;
  }

  /**
   * Текущее количество персонажей
   * @return currentCount
   */
  @NotNull 
  @Schema(name = "current_count", example = "2", description = "Текущее количество персонажей", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_count")
  public Integer getCurrentCount() {
    return currentCount;
  }

  public void setCurrentCount(Integer currentCount) {
    this.currentCount = currentCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListCharacters200Response listCharacters200Response = (ListCharacters200Response) o;
    return Objects.equals(this.characters, listCharacters200Response.characters) &&
        Objects.equals(this.maxCharacters, listCharacters200Response.maxCharacters) &&
        Objects.equals(this.currentCount, listCharacters200Response.currentCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characters, maxCharacters, currentCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListCharacters200Response {\n");
    sb.append("    characters: ").append(toIndentedString(characters)).append("\n");
    sb.append("    maxCharacters: ").append(toIndentedString(maxCharacters)).append("\n");
    sb.append("    currentCount: ").append(toIndentedString(currentCount)).append("\n");
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

