package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ImplantSynergyBonusEffectsInner;
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
 * ImplantSynergy
 */


public class ImplantSynergy {

  private @Nullable String name;

  private @Nullable String description;

  /**
   * - implant_set: синергия набора имплантов - brand_bonus: бонус за бренд - equipment_synergy: синергия с экипировкой - ability_synergy: синергия со способностями 
   */
  public enum TypeEnum {
    IMPLANT_SET("implant_set"),
    
    BRAND_BONUS("brand_bonus"),
    
    EQUIPMENT_SYNERGY("equipment_synergy"),
    
    ABILITY_SYNERGY("ability_synergy");

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
  private List<String> requiredItems = new ArrayList<>();

  @Valid
  private List<@Valid ImplantSynergyBonusEffectsInner> bonusEffects = new ArrayList<>();

  public ImplantSynergy name(@Nullable String name) {
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

  public ImplantSynergy description(@Nullable String description) {
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

  public ImplantSynergy type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * - implant_set: синергия набора имплантов - brand_bonus: бонус за бренд - equipment_synergy: синергия с экипировкой - ability_synergy: синергия со способностями 
   * @return type
   */
  
  @Schema(name = "type", description = "- implant_set: синергия набора имплантов - brand_bonus: бонус за бренд - equipment_synergy: синергия с экипировкой - ability_synergy: синергия со способностями ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public ImplantSynergy requiredItems(List<String> requiredItems) {
    this.requiredItems = requiredItems;
    return this;
  }

  public ImplantSynergy addRequiredItemsItem(String requiredItemsItem) {
    if (this.requiredItems == null) {
      this.requiredItems = new ArrayList<>();
    }
    this.requiredItems.add(requiredItemsItem);
    return this;
  }

  /**
   * Необходимые импланты/экипировка
   * @return requiredItems
   */
  
  @Schema(name = "required_items", description = "Необходимые импланты/экипировка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_items")
  public List<String> getRequiredItems() {
    return requiredItems;
  }

  public void setRequiredItems(List<String> requiredItems) {
    this.requiredItems = requiredItems;
  }

  public ImplantSynergy bonusEffects(List<@Valid ImplantSynergyBonusEffectsInner> bonusEffects) {
    this.bonusEffects = bonusEffects;
    return this;
  }

  public ImplantSynergy addBonusEffectsItem(ImplantSynergyBonusEffectsInner bonusEffectsItem) {
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
  public List<@Valid ImplantSynergyBonusEffectsInner> getBonusEffects() {
    return bonusEffects;
  }

  public void setBonusEffects(List<@Valid ImplantSynergyBonusEffectsInner> bonusEffects) {
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
    ImplantSynergy implantSynergy = (ImplantSynergy) o;
    return Objects.equals(this.name, implantSynergy.name) &&
        Objects.equals(this.description, implantSynergy.description) &&
        Objects.equals(this.type, implantSynergy.type) &&
        Objects.equals(this.requiredItems, implantSynergy.requiredItems) &&
        Objects.equals(this.bonusEffects, implantSynergy.bonusEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, type, requiredItems, bonusEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantSynergy {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requiredItems: ").append(toIndentedString(requiredItems)).append("\n");
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

