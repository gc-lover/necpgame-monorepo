package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * WeaponMod
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class WeaponMod {

  private @Nullable String id;

  private @Nullable String name;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    SCOPE("scope"),
    
    SILENCER("silencer"),
    
    MAGAZINE("magazine"),
    
    BARREL("barrel"),
    
    STOCK("stock"),
    
    GRIP("grip"),
    
    SMART_LINK("smart_link"),
    
    OTHER("other");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Object statModifiers;

  @Valid
  private List<String> compatibleClasses = new ArrayList<>();

  public WeaponMod id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public WeaponMod name(@Nullable String name) {
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

  public WeaponMod description(@Nullable String description) {
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

  public WeaponMod type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public WeaponMod statModifiers(@Nullable Object statModifiers) {
    this.statModifiers = statModifiers;
    return this;
  }

  /**
   * Изменения характеристик оружия
   * @return statModifiers
   */
  
  @Schema(name = "stat_modifiers", description = "Изменения характеристик оружия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stat_modifiers")
  public @Nullable Object getStatModifiers() {
    return statModifiers;
  }

  public void setStatModifiers(@Nullable Object statModifiers) {
    this.statModifiers = statModifiers;
  }

  public WeaponMod compatibleClasses(List<String> compatibleClasses) {
    this.compatibleClasses = compatibleClasses;
    return this;
  }

  public WeaponMod addCompatibleClassesItem(String compatibleClassesItem) {
    if (this.compatibleClasses == null) {
      this.compatibleClasses = new ArrayList<>();
    }
    this.compatibleClasses.add(compatibleClassesItem);
    return this;
  }

  /**
   * Совместимые классы оружия
   * @return compatibleClasses
   */
  
  @Schema(name = "compatible_classes", description = "Совместимые классы оружия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatible_classes")
  public List<String> getCompatibleClasses() {
    return compatibleClasses;
  }

  public void setCompatibleClasses(List<String> compatibleClasses) {
    this.compatibleClasses = compatibleClasses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponMod weaponMod = (WeaponMod) o;
    return Objects.equals(this.id, weaponMod.id) &&
        Objects.equals(this.name, weaponMod.name) &&
        Objects.equals(this.description, weaponMod.description) &&
        Objects.equals(this.type, weaponMod.type) &&
        Objects.equals(this.statModifiers, weaponMod.statModifiers) &&
        Objects.equals(this.compatibleClasses, weaponMod.compatibleClasses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, statModifiers, compatibleClasses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponMod {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    statModifiers: ").append(toIndentedString(statModifiers)).append("\n");
    sb.append("    compatibleClasses: ").append(toIndentedString(compatibleClasses)).append("\n");
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


