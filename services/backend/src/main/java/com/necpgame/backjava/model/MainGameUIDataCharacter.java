package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MainGameUIDataCharacter
 */

@JsonTypeName("MainGameUIData_character")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MainGameUIDataCharacter {

  private @Nullable String name;

  private @Nullable Integer level;

  private @Nullable Integer experience;

  private @Nullable Integer hp;

  @Valid
  private Map<String, Integer> attributes = new HashMap<>();

  public MainGameUIDataCharacter name(@Nullable String name) {
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

  public MainGameUIDataCharacter level(@Nullable Integer level) {
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

  public MainGameUIDataCharacter experience(@Nullable Integer experience) {
    this.experience = experience;
    return this;
  }

  /**
   * Get experience
   * @return experience
   */
  
  @Schema(name = "experience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience")
  public @Nullable Integer getExperience() {
    return experience;
  }

  public void setExperience(@Nullable Integer experience) {
    this.experience = experience;
  }

  public MainGameUIDataCharacter hp(@Nullable Integer hp) {
    this.hp = hp;
    return this;
  }

  /**
   * Get hp
   * @return hp
   */
  
  @Schema(name = "hp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hp")
  public @Nullable Integer getHp() {
    return hp;
  }

  public void setHp(@Nullable Integer hp) {
    this.hp = hp;
  }

  public MainGameUIDataCharacter attributes(Map<String, Integer> attributes) {
    this.attributes = attributes;
    return this;
  }

  public MainGameUIDataCharacter putAttributesItem(String key, Integer attributesItem) {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    this.attributes.put(key, attributesItem);
    return this;
  }

  /**
   * Ключевые характеристики персонажа
   * @return attributes
   */
  
  @Schema(name = "attributes", description = "Ключевые характеристики персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public Map<String, Integer> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Integer> attributes) {
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
    MainGameUIDataCharacter mainGameUIDataCharacter = (MainGameUIDataCharacter) o;
    return Objects.equals(this.name, mainGameUIDataCharacter.name) &&
        Objects.equals(this.level, mainGameUIDataCharacter.level) &&
        Objects.equals(this.experience, mainGameUIDataCharacter.experience) &&
        Objects.equals(this.hp, mainGameUIDataCharacter.hp) &&
        Objects.equals(this.attributes, mainGameUIDataCharacter.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, level, experience, hp, attributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainGameUIDataCharacter {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    experience: ").append(toIndentedString(experience)).append("\n");
    sb.append("    hp: ").append(toIndentedString(hp)).append("\n");
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

