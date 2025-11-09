package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GameCharacterClassSubclassesInner;
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
 * GameCharacterClass
 */


public class GameCharacterClass {

  private String id;

  private String name;

  private String description;

  @Valid
  private List<@Valid GameCharacterClassSubclassesInner> subclasses = new ArrayList<>();

  public GameCharacterClass() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameCharacterClass(String id, String name, String description, List<@Valid GameCharacterClassSubclassesInner> subclasses) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.subclasses = subclasses;
  }

  public GameCharacterClass id(String id) {
    this.id = id;
    return this;
  }

  /**
   * РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РєР»Р°СЃСЃР°
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "solo", description = "РРґРµРЅС‚РёС„РёРєР°С‚РѕСЂ РєР»Р°СЃСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public GameCharacterClass name(String name) {
    this.name = name;
    return this;
  }

  /**
   * РќР°Р·РІР°РЅРёРµ РєР»Р°СЃСЃР°
   * @return name
   */
  @NotNull 
  @Schema(name = "name", example = "Solo", description = "РќР°Р·РІР°РЅРёРµ РєР»Р°СЃСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameCharacterClass description(String description) {
    this.description = description;
    return this;
  }

  /**
   * РћРїРёСЃР°РЅРёРµ РєР»Р°СЃСЃР°
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Р‘РѕРµРІРѕР№ РєР»Р°СЃСЃ, СЃРїРµС†РёР°Р»РёР·Р°С†РёСЏ РЅР° Р±РѕРµРІС‹С… РЅР°РІС‹РєР°С…", description = "РћРїРёСЃР°РЅРёРµ РєР»Р°СЃСЃР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GameCharacterClass subclasses(List<@Valid GameCharacterClassSubclassesInner> subclasses) {
    this.subclasses = subclasses;
    return this;
  }

  public GameCharacterClass addSubclassesItem(GameCharacterClassSubclassesInner subclassesItem) {
    if (this.subclasses == null) {
      this.subclasses = new ArrayList<>();
    }
    this.subclasses.add(subclassesItem);
    return this;
  }

  /**
   * РЎРїРёСЃРѕРє РїРѕРґРєР»Р°СЃСЃРѕРІ
   * @return subclasses
   */
  @NotNull @Valid 
  @Schema(name = "subclasses", description = "РЎРїРёСЃРѕРє РїРѕРґРєР»Р°СЃСЃРѕРІ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subclasses")
  public List<@Valid GameCharacterClassSubclassesInner> getSubclasses() {
    return subclasses;
  }

  public void setSubclasses(List<@Valid GameCharacterClassSubclassesInner> subclasses) {
    this.subclasses = subclasses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameCharacterClass gameCharacterClass = (GameCharacterClass) o;
    return Objects.equals(this.id, gameCharacterClass.id) &&
        Objects.equals(this.name, gameCharacterClass.name) &&
        Objects.equals(this.description, gameCharacterClass.description) &&
        Objects.equals(this.subclasses, gameCharacterClass.subclasses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, subclasses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameCharacterClass {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    subclasses: ").append(toIndentedString(subclasses)).append("\n");
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

