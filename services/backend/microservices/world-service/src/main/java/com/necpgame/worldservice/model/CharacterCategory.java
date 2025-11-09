package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CharacterCategory
 */


public class CharacterCategory {

  private @Nullable String categoryId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String role;

  @Valid
  private List<String> exampleCharacters = new ArrayList<>();

  public CharacterCategory categoryId(@Nullable String categoryId) {
    this.categoryId = categoryId;
    return this;
  }

  /**
   * Get categoryId
   * @return categoryId
   */
  
  @Schema(name = "category_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category_id")
  public @Nullable String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(@Nullable String categoryId) {
    this.categoryId = categoryId;
  }

  public CharacterCategory name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Fixers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CharacterCategory description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CharacterCategory role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public CharacterCategory exampleCharacters(List<String> exampleCharacters) {
    this.exampleCharacters = exampleCharacters;
    return this;
  }

  public CharacterCategory addExampleCharactersItem(String exampleCharactersItem) {
    if (this.exampleCharacters == null) {
      this.exampleCharacters = new ArrayList<>();
    }
    this.exampleCharacters.add(exampleCharactersItem);
    return this;
  }

  /**
   * Get exampleCharacters
   * @return exampleCharacters
   */
  
  @Schema(name = "example_characters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("example_characters")
  public List<String> getExampleCharacters() {
    return exampleCharacters;
  }

  public void setExampleCharacters(List<String> exampleCharacters) {
    this.exampleCharacters = exampleCharacters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterCategory characterCategory = (CharacterCategory) o;
    return Objects.equals(this.categoryId, characterCategory.categoryId) &&
        Objects.equals(this.name, characterCategory.name) &&
        Objects.equals(this.description, characterCategory.description) &&
        Objects.equals(this.role, characterCategory.role) &&
        Objects.equals(this.exampleCharacters, characterCategory.exampleCharacters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryId, name, description, role, exampleCharacters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterCategory {\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    exampleCharacters: ").append(toIndentedString(exampleCharacters)).append("\n");
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

