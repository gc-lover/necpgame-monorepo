package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.AbilitySynergyBonusEffectsInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AbilitySynergy
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:49:04.787810800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class AbilitySynergy {

  private @Nullable String name;

  private @Nullable String description;

  /**
   * - set_bonus: бонус за сет способностей - brand_bonus: бонус за бренд (Arasaka, Militech) - class_bonus: бонус за класс (Solo, Netrunner) - combo_bonus: бонус за комбо способностей 
   */
  public enum TypeEnum {
    SET_BONUS("set_bonus"),
    
    BRAND_BONUS("brand_bonus"),
    
    CLASS_BONUS("class_bonus"),
    
    COMBO_BONUS("combo_bonus");

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

  @Valid
  private List<String> requiredAbilities = new ArrayList<>();

  @Valid
  private List<@Valid AbilitySynergyBonusEffectsInner> bonusEffects = new ArrayList<>();

  public AbilitySynergy name(@Nullable String name) {
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

  public AbilitySynergy description(@Nullable String description) {
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

  public AbilitySynergy type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * - set_bonus: бонус за сет способностей - brand_bonus: бонус за бренд (Arasaka, Militech) - class_bonus: бонус за класс (Solo, Netrunner) - combo_bonus: бонус за комбо способностей 
   * @return type
   */
  
  @Schema(name = "type", description = "- set_bonus: бонус за сет способностей - brand_bonus: бонус за бренд (Arasaka, Militech) - class_bonus: бонус за класс (Solo, Netrunner) - combo_bonus: бонус за комбо способностей ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public AbilitySynergy requiredAbilities(List<String> requiredAbilities) {
    this.requiredAbilities = requiredAbilities;
    return this;
  }

  public AbilitySynergy addRequiredAbilitiesItem(String requiredAbilitiesItem) {
    if (this.requiredAbilities == null) {
      this.requiredAbilities = new ArrayList<>();
    }
    this.requiredAbilities.add(requiredAbilitiesItem);
    return this;
  }

  /**
   * Способности, необходимые для синергии
   * @return requiredAbilities
   */
  
  @Schema(name = "required_abilities", description = "Способности, необходимые для синергии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_abilities")
  public List<String> getRequiredAbilities() {
    return requiredAbilities;
  }

  public void setRequiredAbilities(List<String> requiredAbilities) {
    this.requiredAbilities = requiredAbilities;
  }

  public AbilitySynergy bonusEffects(List<@Valid AbilitySynergyBonusEffectsInner> bonusEffects) {
    this.bonusEffects = bonusEffects;
    return this;
  }

  public AbilitySynergy addBonusEffectsItem(AbilitySynergyBonusEffectsInner bonusEffectsItem) {
    if (this.bonusEffects == null) {
      this.bonusEffects = new ArrayList<>();
    }
    this.bonusEffects.add(bonusEffectsItem);
    return this;
  }

  /**
   * Эффекты синергии
   * @return bonusEffects
   */
  @Valid 
  @Schema(name = "bonus_effects", description = "Эффекты синергии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_effects")
  public List<@Valid AbilitySynergyBonusEffectsInner> getBonusEffects() {
    return bonusEffects;
  }

  public void setBonusEffects(List<@Valid AbilitySynergyBonusEffectsInner> bonusEffects) {
    this.bonusEffects = bonusEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilitySynergy abilitySynergy = (AbilitySynergy) o;
    return Objects.equals(this.name, abilitySynergy.name) &&
        Objects.equals(this.description, abilitySynergy.description) &&
        Objects.equals(this.type, abilitySynergy.type) &&
        Objects.equals(this.requiredAbilities, abilitySynergy.requiredAbilities) &&
        Objects.equals(this.bonusEffects, abilitySynergy.bonusEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, type, requiredAbilities, bonusEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilitySynergy {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requiredAbilities: ").append(toIndentedString(requiredAbilities)).append("\n");
    sb.append("    bonusEffects: ").append(toIndentedString(bonusEffects)).append("\n");
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

