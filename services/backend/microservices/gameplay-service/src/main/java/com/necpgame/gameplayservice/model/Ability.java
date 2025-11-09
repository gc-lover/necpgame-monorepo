package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.AbilityCost;
import com.necpgame.gameplayservice.model.AbilityEffectsInner;
import com.necpgame.gameplayservice.model.AbilitySource;
import java.math.BigDecimal;
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
 * Ability
 */


public class Ability {

  private String id;

  private String name;

  private @Nullable String description;

  /**
   * - tactical: Q способность (1-2 на перезарядке) - signature: E способность (бесплатная, но кулдаун) - ultimate: R способность (требует зарядки) - passive: постоянно активна - cyberdeck: хакерская способность 
   */
  public enum TypeEnum {
    TACTICAL("tactical"),
    
    SIGNATURE("signature"),
    
    ULTIMATE("ultimate"),
    
    PASSIVE("passive"),
    
    CYBERDECK("cyberdeck");

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

  private TypeEnum type;

  /**
   * Слот для способности
   */
  public enum SlotEnum {
    Q("Q"),
    
    E("E"),
    
    R("R"),
    
    PASSIVE("passive"),
    
    CYBERDECK("cyberdeck");

    private final String value;

    SlotEnum(String value) {
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
    public static SlotEnum fromValue(String value) {
      for (SlotEnum b : SlotEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SlotEnum slot;

  private AbilitySource source;

  private AbilityCost cost;

  private @Nullable BigDecimal cooldown;

  @Valid
  private List<@Valid AbilityEffectsInner> effects = new ArrayList<>();

  private @Nullable BigDecimal range;

  private @Nullable BigDecimal aoe;

  @Valid
  private List<String> classAffinity = new ArrayList<>();

  public Ability() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Ability(String id, String name, TypeEnum type, SlotEnum slot, AbilitySource source, AbilityCost cost) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.slot = slot;
    this.source = source;
    this.cost = cost;
  }

  public Ability id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Ability name(String name) {
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

  public Ability description(@Nullable String description) {
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

  public Ability type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * - tactical: Q способность (1-2 на перезарядке) - signature: E способность (бесплатная, но кулдаун) - ultimate: R способность (требует зарядки) - passive: постоянно активна - cyberdeck: хакерская способность 
   * @return type
   */
  @NotNull 
  @Schema(name = "type", description = "- tactical: Q способность (1-2 на перезарядке) - signature: E способность (бесплатная, но кулдаун) - ultimate: R способность (требует зарядки) - passive: постоянно активна - cyberdeck: хакерская способность ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Ability slot(SlotEnum slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Слот для способности
   * @return slot
   */
  @NotNull 
  @Schema(name = "slot", description = "Слот для способности", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot")
  public SlotEnum getSlot() {
    return slot;
  }

  public void setSlot(SlotEnum slot) {
    this.slot = slot;
  }

  public Ability source(AbilitySource source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull @Valid 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public AbilitySource getSource() {
    return source;
  }

  public void setSource(AbilitySource source) {
    this.source = source;
  }

  public Ability cost(AbilityCost cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  @NotNull @Valid 
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public AbilityCost getCost() {
    return cost;
  }

  public void setCost(AbilityCost cost) {
    this.cost = cost;
  }

  public Ability cooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
    return this;
  }

  /**
   * Кулдаун в секундах
   * @return cooldown
   */
  @Valid 
  @Schema(name = "cooldown", description = "Кулдаун в секундах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public @Nullable BigDecimal getCooldown() {
    return cooldown;
  }

  public void setCooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
  }

  public Ability effects(List<@Valid AbilityEffectsInner> effects) {
    this.effects = effects;
    return this;
  }

  public Ability addEffectsItem(AbilityEffectsInner effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Эффекты способности
   * @return effects
   */
  @Valid 
  @Schema(name = "effects", description = "Эффекты способности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public List<@Valid AbilityEffectsInner> getEffects() {
    return effects;
  }

  public void setEffects(List<@Valid AbilityEffectsInner> effects) {
    this.effects = effects;
  }

  public Ability range(@Nullable BigDecimal range) {
    this.range = range;
    return this;
  }

  /**
   * Дальность действия
   * @return range
   */
  @Valid 
  @Schema(name = "range", description = "Дальность действия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable BigDecimal getRange() {
    return range;
  }

  public void setRange(@Nullable BigDecimal range) {
    this.range = range;
  }

  public Ability aoe(@Nullable BigDecimal aoe) {
    this.aoe = aoe;
    return this;
  }

  /**
   * Радиус действия (площадь)
   * @return aoe
   */
  @Valid 
  @Schema(name = "aoe", description = "Радиус действия (площадь)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aoe")
  public @Nullable BigDecimal getAoe() {
    return aoe;
  }

  public void setAoe(@Nullable BigDecimal aoe) {
    this.aoe = aoe;
  }

  public Ability classAffinity(List<String> classAffinity) {
    this.classAffinity = classAffinity;
    return this;
  }

  public Ability addClassAffinityItem(String classAffinityItem) {
    if (this.classAffinity == null) {
      this.classAffinity = new ArrayList<>();
    }
    this.classAffinity.add(classAffinityItem);
    return this;
  }

  /**
   * Классы, для которых способность оптимальна
   * @return classAffinity
   */
  
  @Schema(name = "class_affinity", description = "Классы, для которых способность оптимальна", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_affinity")
  public List<String> getClassAffinity() {
    return classAffinity;
  }

  public void setClassAffinity(List<String> classAffinity) {
    this.classAffinity = classAffinity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ability ability = (Ability) o;
    return Objects.equals(this.id, ability.id) &&
        Objects.equals(this.name, ability.name) &&
        Objects.equals(this.description, ability.description) &&
        Objects.equals(this.type, ability.type) &&
        Objects.equals(this.slot, ability.slot) &&
        Objects.equals(this.source, ability.source) &&
        Objects.equals(this.cost, ability.cost) &&
        Objects.equals(this.cooldown, ability.cooldown) &&
        Objects.equals(this.effects, ability.effects) &&
        Objects.equals(this.range, ability.range) &&
        Objects.equals(this.aoe, ability.aoe) &&
        Objects.equals(this.classAffinity, ability.classAffinity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, slot, source, cost, cooldown, effects, range, aoe, classAffinity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ability {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    aoe: ").append(toIndentedString(aoe)).append("\n");
    sb.append("    classAffinity: ").append(toIndentedString(classAffinity)).append("\n");
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

