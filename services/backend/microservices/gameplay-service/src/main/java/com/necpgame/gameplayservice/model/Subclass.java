package com.necpgame.gameplayservice.model;

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
 * Subclass
 */


public class Subclass {

  private String subclassId;

  private String name;

  private @Nullable String description;

  private String focus;

  @Valid
  private List<String> bonusAbilities = new ArrayList<>();

  private @Nullable Object statModifiers;

  public Subclass() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Subclass(String subclassId, String name, String focus) {
    this.subclassId = subclassId;
    this.name = name;
    this.focus = focus;
  }

  public Subclass subclassId(String subclassId) {
    this.subclassId = subclassId;
    return this;
  }

  /**
   * Get subclassId
   * @return subclassId
   */
  @NotNull 
  @Schema(name = "subclass_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subclass_id")
  public String getSubclassId() {
    return subclassId;
  }

  public void setSubclassId(String subclassId) {
    this.subclassId = subclassId;
  }

  public Subclass name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Subclass description(@Nullable String description) {
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

  public Subclass focus(String focus) {
    this.focus = focus;
    return this;
  }

  /**
   * Фокус подкласса
   * @return focus
   */
  @NotNull 
  @Schema(name = "focus", description = "Фокус подкласса", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("focus")
  public String getFocus() {
    return focus;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }

  public Subclass bonusAbilities(List<String> bonusAbilities) {
    this.bonusAbilities = bonusAbilities;
    return this;
  }

  public Subclass addBonusAbilitiesItem(String bonusAbilitiesItem) {
    if (this.bonusAbilities == null) {
      this.bonusAbilities = new ArrayList<>();
    }
    this.bonusAbilities.add(bonusAbilitiesItem);
    return this;
  }

  /**
   * Дополнительные способности подкласса
   * @return bonusAbilities
   */
  
  @Schema(name = "bonus_abilities", description = "Дополнительные способности подкласса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_abilities")
  public List<String> getBonusAbilities() {
    return bonusAbilities;
  }

  public void setBonusAbilities(List<String> bonusAbilities) {
    this.bonusAbilities = bonusAbilities;
  }

  public Subclass statModifiers(@Nullable Object statModifiers) {
    this.statModifiers = statModifiers;
    return this;
  }

  /**
   * Модификаторы характеристик подкласса
   * @return statModifiers
   */
  
  @Schema(name = "stat_modifiers", description = "Модификаторы характеристик подкласса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stat_modifiers")
  public @Nullable Object getStatModifiers() {
    return statModifiers;
  }

  public void setStatModifiers(@Nullable Object statModifiers) {
    this.statModifiers = statModifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Subclass subclass = (Subclass) o;
    return Objects.equals(this.subclassId, subclass.subclassId) &&
        Objects.equals(this.name, subclass.name) &&
        Objects.equals(this.description, subclass.description) &&
        Objects.equals(this.focus, subclass.focus) &&
        Objects.equals(this.bonusAbilities, subclass.bonusAbilities) &&
        Objects.equals(this.statModifiers, subclass.statModifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subclassId, name, description, focus, bonusAbilities, statModifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Subclass {\n");
    sb.append("    subclassId: ").append(toIndentedString(subclassId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    focus: ").append(toIndentedString(focus)).append("\n");
    sb.append("    bonusAbilities: ").append(toIndentedString(bonusAbilities)).append("\n");
    sb.append("    statModifiers: ").append(toIndentedString(statModifiers)).append("\n");
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

