package com.necpgame.gameplayservice.model;

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
 * WelcomeScreenResponseCharacter
 */

@JsonTypeName("WelcomeScreenResponse_character")

public class WelcomeScreenResponseCharacter {

  private String name;

  private String propertyClass;

  private Integer level;

  public WelcomeScreenResponseCharacter() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WelcomeScreenResponseCharacter(String name, String propertyClass, Integer level) {
    this.name = name;
    this.propertyClass = propertyClass;
    this.level = level;
  }

  public WelcomeScreenResponseCharacter name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Имя персонажа
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Джон Доу", description = "Имя персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public WelcomeScreenResponseCharacter propertyClass(String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Класс персонажа
   * @return propertyClass
   */
  @NotNull 
  @Schema(name = "class", example = "Соло", description = "Класс персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("class")
  public String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public WelcomeScreenResponseCharacter level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Уровень персонажа
   * @return level
   */
  @NotNull 
  @Schema(name = "level", example = "1", description = "Уровень персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WelcomeScreenResponseCharacter welcomeScreenResponseCharacter = (WelcomeScreenResponseCharacter) o;
    return Objects.equals(this.name, welcomeScreenResponseCharacter.name) &&
        Objects.equals(this.propertyClass, welcomeScreenResponseCharacter.propertyClass) &&
        Objects.equals(this.level, welcomeScreenResponseCharacter.level);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, propertyClass, level);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WelcomeScreenResponseCharacter {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
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

