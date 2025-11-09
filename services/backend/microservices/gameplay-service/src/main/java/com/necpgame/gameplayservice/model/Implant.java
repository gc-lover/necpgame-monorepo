package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ImplantStatModifiers;
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
 * Implant
 */


public class Implant {

  private String id;

  private String name;

  private @Nullable String description;

  /**
   * - combat: боевые (точность, урон, скорострельность) - tactical: тактические (сканирование, хакерство) - defensive: защитные (броня, восстановление) - mobility: двигательные (скорость, прыжки, паркур) - os: операционные системы (Sandevistan, Cyberdeck, Berserk) 
   */
  public enum TypeEnum {
    COMBAT("combat"),
    
    TACTICAL("tactical"),
    
    DEFENSIVE("defensive"),
    
    MOBILITY("mobility"),
    
    OS("os");

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
   * Слот для установки: - os: операционная система - optics: киберглаза - arms: киберруки - legs: киберноги - nervous_system: нервная система - integumentary: интегральная система (кожа) - skeleton: скелет - circulatory: кровеносная система - other: другие 
   */
  public enum SlotEnum {
    OS("os"),
    
    OPTICS("optics"),
    
    ARMS("arms"),
    
    LEGS("legs"),
    
    NERVOUS_SYSTEM("nervous_system"),
    
    INTEGUMENTARY("integumentary"),
    
    SKELETON("skeleton"),
    
    CIRCULATORY("circulatory"),
    
    OTHER("other");

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

  private @Nullable String brand;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary");

    private final String value;

    RarityEnum(String value) {
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
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RarityEnum rarity;

  private @Nullable ImplantStatModifiers statModifiers;

  @Valid
  private List<String> abilitiesGranted = new ArrayList<>();

  private @Nullable BigDecimal humanityCost;

  private @Nullable BigDecimal energyConsumption;

  private @Nullable BigDecimal heatGeneration;

  @Valid
  private List<String> compatibleWith = new ArrayList<>();

  @Valid
  private List<String> incompatibleWith = new ArrayList<>();

  private @Nullable String loreSource;

  public Implant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Implant(String id, String name, TypeEnum type, SlotEnum slot, RarityEnum rarity) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.slot = slot;
    this.rarity = rarity;
  }

  public Implant id(String id) {
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

  public Implant name(String name) {
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

  public Implant description(@Nullable String description) {
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

  public Implant type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * - combat: боевые (точность, урон, скорострельность) - tactical: тактические (сканирование, хакерство) - defensive: защитные (броня, восстановление) - mobility: двигательные (скорость, прыжки, паркур) - os: операционные системы (Sandevistan, Cyberdeck, Berserk) 
   * @return type
   */
  @NotNull 
  @Schema(name = "type", description = "- combat: боевые (точность, урон, скорострельность) - tactical: тактические (сканирование, хакерство) - defensive: защитные (броня, восстановление) - mobility: двигательные (скорость, прыжки, паркур) - os: операционные системы (Sandevistan, Cyberdeck, Berserk) ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Implant slot(SlotEnum slot) {
    this.slot = slot;
    return this;
  }

  /**
   * Слот для установки: - os: операционная система - optics: киберглаза - arms: киберруки - legs: киберноги - nervous_system: нервная система - integumentary: интегральная система (кожа) - skeleton: скелет - circulatory: кровеносная система - other: другие 
   * @return slot
   */
  @NotNull 
  @Schema(name = "slot", description = "Слот для установки: - os: операционная система - optics: киберглаза - arms: киберруки - legs: киберноги - nervous_system: нервная система - integumentary: интегральная система (кожа) - skeleton: скелет - circulatory: кровеносная система - other: другие ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slot")
  public SlotEnum getSlot() {
    return slot;
  }

  public void setSlot(SlotEnum slot) {
    this.slot = slot;
  }

  public Implant brand(@Nullable String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Бренд импланта (Arasaka, Militech, Kiroshi и т.д.)
   * @return brand
   */
  
  @Schema(name = "brand", description = "Бренд импланта (Arasaka, Militech, Kiroshi и т.д.)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("brand")
  public @Nullable String getBrand() {
    return brand;
  }

  public void setBrand(@Nullable String brand) {
    this.brand = brand;
  }

  public Implant rarity(RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  @NotNull 
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rarity")
  public RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(RarityEnum rarity) {
    this.rarity = rarity;
  }

  public Implant statModifiers(@Nullable ImplantStatModifiers statModifiers) {
    this.statModifiers = statModifiers;
    return this;
  }

  /**
   * Get statModifiers
   * @return statModifiers
   */
  @Valid 
  @Schema(name = "stat_modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stat_modifiers")
  public @Nullable ImplantStatModifiers getStatModifiers() {
    return statModifiers;
  }

  public void setStatModifiers(@Nullable ImplantStatModifiers statModifiers) {
    this.statModifiers = statModifiers;
  }

  public Implant abilitiesGranted(List<String> abilitiesGranted) {
    this.abilitiesGranted = abilitiesGranted;
    return this;
  }

  public Implant addAbilitiesGrantedItem(String abilitiesGrantedItem) {
    if (this.abilitiesGranted == null) {
      this.abilitiesGranted = new ArrayList<>();
    }
    this.abilitiesGranted.add(abilitiesGrantedItem);
    return this;
  }

  /**
   * Способности, которые дает имплант
   * @return abilitiesGranted
   */
  
  @Schema(name = "abilities_granted", description = "Способности, которые дает имплант", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities_granted")
  public List<String> getAbilitiesGranted() {
    return abilitiesGranted;
  }

  public void setAbilitiesGranted(List<String> abilitiesGranted) {
    this.abilitiesGranted = abilitiesGranted;
  }

  public Implant humanityCost(@Nullable BigDecimal humanityCost) {
    this.humanityCost = humanityCost;
    return this;
  }

  /**
   * Стоимость в очках человечности
   * @return humanityCost
   */
  @Valid 
  @Schema(name = "humanity_cost", description = "Стоимость в очках человечности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_cost")
  public @Nullable BigDecimal getHumanityCost() {
    return humanityCost;
  }

  public void setHumanityCost(@Nullable BigDecimal humanityCost) {
    this.humanityCost = humanityCost;
  }

  public Implant energyConsumption(@Nullable BigDecimal energyConsumption) {
    this.energyConsumption = energyConsumption;
    return this;
  }

  /**
   * Потребление энергии
   * @return energyConsumption
   */
  @Valid 
  @Schema(name = "energy_consumption", description = "Потребление энергии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_consumption")
  public @Nullable BigDecimal getEnergyConsumption() {
    return energyConsumption;
  }

  public void setEnergyConsumption(@Nullable BigDecimal energyConsumption) {
    this.energyConsumption = energyConsumption;
  }

  public Implant heatGeneration(@Nullable BigDecimal heatGeneration) {
    this.heatGeneration = heatGeneration;
    return this;
  }

  /**
   * Генерация перегрева
   * @return heatGeneration
   */
  @Valid 
  @Schema(name = "heat_generation", description = "Генерация перегрева", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("heat_generation")
  public @Nullable BigDecimal getHeatGeneration() {
    return heatGeneration;
  }

  public void setHeatGeneration(@Nullable BigDecimal heatGeneration) {
    this.heatGeneration = heatGeneration;
  }

  public Implant compatibleWith(List<String> compatibleWith) {
    this.compatibleWith = compatibleWith;
    return this;
  }

  public Implant addCompatibleWithItem(String compatibleWithItem) {
    if (this.compatibleWith == null) {
      this.compatibleWith = new ArrayList<>();
    }
    this.compatibleWith.add(compatibleWithItem);
    return this;
  }

  /**
   * Совместимые импланты/экипировка
   * @return compatibleWith
   */
  
  @Schema(name = "compatible_with", description = "Совместимые импланты/экипировка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatible_with")
  public List<String> getCompatibleWith() {
    return compatibleWith;
  }

  public void setCompatibleWith(List<String> compatibleWith) {
    this.compatibleWith = compatibleWith;
  }

  public Implant incompatibleWith(List<String> incompatibleWith) {
    this.incompatibleWith = incompatibleWith;
    return this;
  }

  public Implant addIncompatibleWithItem(String incompatibleWithItem) {
    if (this.incompatibleWith == null) {
      this.incompatibleWith = new ArrayList<>();
    }
    this.incompatibleWith.add(incompatibleWithItem);
    return this;
  }

  /**
   * Несовместимые импланты
   * @return incompatibleWith
   */
  
  @Schema(name = "incompatible_with", description = "Несовместимые импланты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("incompatible_with")
  public List<String> getIncompatibleWith() {
    return incompatibleWith;
  }

  public void setIncompatibleWith(List<String> incompatibleWith) {
    this.incompatibleWith = incompatibleWith;
  }

  public Implant loreSource(@Nullable String loreSource) {
    this.loreSource = loreSource;
    return this;
  }

  /**
   * Источник из лора (Cyberpunk 2077, настольная игра, авторский)
   * @return loreSource
   */
  
  @Schema(name = "lore_source", description = "Источник из лора (Cyberpunk 2077, настольная игра, авторский)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore_source")
  public @Nullable String getLoreSource() {
    return loreSource;
  }

  public void setLoreSource(@Nullable String loreSource) {
    this.loreSource = loreSource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Implant implant = (Implant) o;
    return Objects.equals(this.id, implant.id) &&
        Objects.equals(this.name, implant.name) &&
        Objects.equals(this.description, implant.description) &&
        Objects.equals(this.type, implant.type) &&
        Objects.equals(this.slot, implant.slot) &&
        Objects.equals(this.brand, implant.brand) &&
        Objects.equals(this.rarity, implant.rarity) &&
        Objects.equals(this.statModifiers, implant.statModifiers) &&
        Objects.equals(this.abilitiesGranted, implant.abilitiesGranted) &&
        Objects.equals(this.humanityCost, implant.humanityCost) &&
        Objects.equals(this.energyConsumption, implant.energyConsumption) &&
        Objects.equals(this.heatGeneration, implant.heatGeneration) &&
        Objects.equals(this.compatibleWith, implant.compatibleWith) &&
        Objects.equals(this.incompatibleWith, implant.incompatibleWith) &&
        Objects.equals(this.loreSource, implant.loreSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type, slot, brand, rarity, statModifiers, abilitiesGranted, humanityCost, energyConsumption, heatGeneration, compatibleWith, incompatibleWith, loreSource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Implant {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    slot: ").append(toIndentedString(slot)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    statModifiers: ").append(toIndentedString(statModifiers)).append("\n");
    sb.append("    abilitiesGranted: ").append(toIndentedString(abilitiesGranted)).append("\n");
    sb.append("    humanityCost: ").append(toIndentedString(humanityCost)).append("\n");
    sb.append("    energyConsumption: ").append(toIndentedString(energyConsumption)).append("\n");
    sb.append("    heatGeneration: ").append(toIndentedString(heatGeneration)).append("\n");
    sb.append("    compatibleWith: ").append(toIndentedString(compatibleWith)).append("\n");
    sb.append("    incompatibleWith: ").append(toIndentedString(incompatibleWith)).append("\n");
    sb.append("    loreSource: ").append(toIndentedString(loreSource)).append("\n");
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

