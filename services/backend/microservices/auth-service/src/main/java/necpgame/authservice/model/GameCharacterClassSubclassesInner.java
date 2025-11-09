package com.necpgame.authservice.model;

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
 * GameCharacterClassSubclassesInner
 */

@JsonTypeName("GameCharacterClass_subclasses_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GameCharacterClassSubclassesInner {

  private String id;

  private String name;

  private String description;

  public GameCharacterClassSubclassesInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterClassSubclassesInner(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public GameCharacterClassSubclassesInner id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор подкласса
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "solo_assassin", description = "Идентификатор подкласса", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameCharacterClassSubclassesInner name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название подкласса
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Assassin", description = "Название подкласса", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameCharacterClassSubclassesInner description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание подкласса
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Специализация на скрытности и убийствах", description = "Описание подкласса", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterClassSubclassesInner gameCharacterClassSubclassesInner = (GameCharacterClassSubclassesInner) o;
    return Objects.equals(this.id, gameCharacterClassSubclassesInner.id) &&
        Objects.equals(this.name, gameCharacterClassSubclassesInner.name) &&
        Objects.equals(this.description, gameCharacterClassSubclassesInner.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacterClassSubclassesInner {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

