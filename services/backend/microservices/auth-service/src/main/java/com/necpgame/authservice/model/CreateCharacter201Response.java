package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.authservice.model.GameCharacter;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateCharacter201Response
 */

@JsonTypeName("createCharacter_201_response")

public class CreateCharacter201Response {

  private GameCharacter character;

  public CreateCharacter201Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateCharacter201Response(GameCharacter character) {
    this.character = character;
  }

  public CreateCharacter201Response character(GameCharacter character) {
    this.character = character;
    return this;
  }

  /**
   * Get character
   * @return character
   */
  @NotNull @Valid 
  @Schema(name = "character", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character")
  public GameCharacter getCharacter() {
    return character;
  }

  public void setCharacter(GameCharacter character) {
    this.character = character;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCharacter201Response createCharacter201Response = (CreateCharacter201Response) o;
    return Objects.equals(this.character, createCharacter201Response.character);
  }

  @Override
  public int hashCode() {
    return Objects.hash(character);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCharacter201Response {\n");
    sb.append("    character: ").append(toIndentedString(character)).append("\n");
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

