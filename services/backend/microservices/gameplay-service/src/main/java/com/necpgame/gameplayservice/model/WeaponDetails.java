package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.WeaponDetailsRequirements;
import com.necpgame.gameplayservice.model.WeaponDetailsSpecialAbilitiesInner;
import com.necpgame.gameplayservice.model.WeaponStats;
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
 * WeaponDetails
 */


public class WeaponDetails {

  private String id;

  private String name;

  private @Nullable String description;

  private @Nullable String lore;

  /**
   * Gets or Sets weaponClass
   */
  public enum WeaponClassEnum {
    PISTOL("pistol"),
    
    ASSAULT_RIFLE("assault_rifle"),
    
    SHOTGUN("shotgun"),
    
    SNIPER_RIFLE("sniper_rifle"),
    
    SMG("smg"),
    
    LMG("lmg"),
    
    MELEE("melee"),
    
    CYBERWARE("cyberware");

    private final String value;

    WeaponClassEnum(String value) {
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
    public static WeaponClassEnum fromValue(String value) {
      for (WeaponClassEnum b : WeaponClassEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private WeaponClassEnum weaponClass;

  private @Nullable String subclass;

  private @Nullable String brand;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    UNCOMMON("uncommon"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary"),
    
    ICONIC("iconic");

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

  private WeaponStats stats;

  @Valid
  private List<@Valid WeaponDetailsSpecialAbilitiesInner> specialAbilities = new ArrayList<>();

  private @Nullable Integer modSlots;

  @Valid
  private List<String> compatibleMods = new ArrayList<>();

  private @Nullable WeaponDetailsRequirements requirements;

  public WeaponDetails() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WeaponDetails(String id, String name, WeaponClassEnum weaponClass, RarityEnum rarity, WeaponStats stats) {
    this.id = id;
    this.name = name;
    this.weaponClass = weaponClass;
    this.rarity = rarity;
    this.stats = stats;
  }

  public WeaponDetails id(String id) {
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

  public WeaponDetails name(String name) {
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

  public WeaponDetails description(@Nullable String description) {
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

  public WeaponDetails lore(@Nullable String lore) {
    this.lore = lore;
    return this;
  }

  /**
   * Лор из Cyberpunk 2077
   * @return lore
   */
  
  @Schema(name = "lore", description = "Лор из Cyberpunk 2077", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore")
  public @Nullable String getLore() {
    return lore;
  }

  public void setLore(@Nullable String lore) {
    this.lore = lore;
  }

  public WeaponDetails weaponClass(WeaponClassEnum weaponClass) {
    this.weaponClass = weaponClass;
    return this;
  }

  /**
   * Get weaponClass
   * @return weaponClass
   */
  @NotNull 
  @Schema(name = "weapon_class", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weapon_class")
  public WeaponClassEnum getWeaponClass() {
    return weaponClass;
  }

  public void setWeaponClass(WeaponClassEnum weaponClass) {
    this.weaponClass = weaponClass;
  }

  public WeaponDetails subclass(@Nullable String subclass) {
    this.subclass = subclass;
    return this;
  }

  /**
   * Подкласс (например, revolver, semi-auto pistol)
   * @return subclass
   */
  
  @Schema(name = "subclass", description = "Подкласс (например, revolver, semi-auto pistol)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclass")
  public @Nullable String getSubclass() {
    return subclass;
  }

  public void setSubclass(@Nullable String subclass) {
    this.subclass = subclass;
  }

  public WeaponDetails brand(@Nullable String brand) {
    this.brand = brand;
    return this;
  }

  /**
   * Get brand
   * @return brand
   */
  
  @Schema(name = "brand", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("brand")
  public @Nullable String getBrand() {
    return brand;
  }

  public void setBrand(@Nullable String brand) {
    this.brand = brand;
  }

  public WeaponDetails rarity(RarityEnum rarity) {
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

  public WeaponDetails stats(WeaponStats stats) {
    this.stats = stats;
    return this;
  }

  /**
   * Get stats
   * @return stats
   */
  @NotNull @Valid 
  @Schema(name = "stats", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stats")
  public WeaponStats getStats() {
    return stats;
  }

  public void setStats(WeaponStats stats) {
    this.stats = stats;
  }

  public WeaponDetails specialAbilities(List<@Valid WeaponDetailsSpecialAbilitiesInner> specialAbilities) {
    this.specialAbilities = specialAbilities;
    return this;
  }

  public WeaponDetails addSpecialAbilitiesItem(WeaponDetailsSpecialAbilitiesInner specialAbilitiesItem) {
    if (this.specialAbilities == null) {
      this.specialAbilities = new ArrayList<>();
    }
    this.specialAbilities.add(specialAbilitiesItem);
    return this;
  }

  /**
   * Специальные способности оружия
   * @return specialAbilities
   */
  @Valid 
  @Schema(name = "special_abilities", description = "Специальные способности оружия", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("special_abilities")
  public List<@Valid WeaponDetailsSpecialAbilitiesInner> getSpecialAbilities() {
    return specialAbilities;
  }

  public void setSpecialAbilities(List<@Valid WeaponDetailsSpecialAbilitiesInner> specialAbilities) {
    this.specialAbilities = specialAbilities;
  }

  public WeaponDetails modSlots(@Nullable Integer modSlots) {
    this.modSlots = modSlots;
    return this;
  }

  /**
   * Количество слотов для модов
   * @return modSlots
   */
  
  @Schema(name = "mod_slots", description = "Количество слотов для модов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mod_slots")
  public @Nullable Integer getModSlots() {
    return modSlots;
  }

  public void setModSlots(@Nullable Integer modSlots) {
    this.modSlots = modSlots;
  }

  public WeaponDetails compatibleMods(List<String> compatibleMods) {
    this.compatibleMods = compatibleMods;
    return this;
  }

  public WeaponDetails addCompatibleModsItem(String compatibleModsItem) {
    if (this.compatibleMods == null) {
      this.compatibleMods = new ArrayList<>();
    }
    this.compatibleMods.add(compatibleModsItem);
    return this;
  }

  /**
   * Get compatibleMods
   * @return compatibleMods
   */
  
  @Schema(name = "compatible_mods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatible_mods")
  public List<String> getCompatibleMods() {
    return compatibleMods;
  }

  public void setCompatibleMods(List<String> compatibleMods) {
    this.compatibleMods = compatibleMods;
  }

  public WeaponDetails requirements(@Nullable WeaponDetailsRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable WeaponDetailsRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable WeaponDetailsRequirements requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponDetails weaponDetails = (WeaponDetails) o;
    return Objects.equals(this.id, weaponDetails.id) &&
        Objects.equals(this.name, weaponDetails.name) &&
        Objects.equals(this.description, weaponDetails.description) &&
        Objects.equals(this.lore, weaponDetails.lore) &&
        Objects.equals(this.weaponClass, weaponDetails.weaponClass) &&
        Objects.equals(this.subclass, weaponDetails.subclass) &&
        Objects.equals(this.brand, weaponDetails.brand) &&
        Objects.equals(this.rarity, weaponDetails.rarity) &&
        Objects.equals(this.stats, weaponDetails.stats) &&
        Objects.equals(this.specialAbilities, weaponDetails.specialAbilities) &&
        Objects.equals(this.modSlots, weaponDetails.modSlots) &&
        Objects.equals(this.compatibleMods, weaponDetails.compatibleMods) &&
        Objects.equals(this.requirements, weaponDetails.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, lore, weaponClass, subclass, brand, rarity, stats, specialAbilities, modSlots, compatibleMods, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponDetails {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lore: ").append(toIndentedString(lore)).append("\n");
    sb.append("    weaponClass: ").append(toIndentedString(weaponClass)).append("\n");
    sb.append("    subclass: ").append(toIndentedString(subclass)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    stats: ").append(toIndentedString(stats)).append("\n");
    sb.append("    specialAbilities: ").append(toIndentedString(specialAbilities)).append("\n");
    sb.append("    modSlots: ").append(toIndentedString(modSlots)).append("\n");
    sb.append("    compatibleMods: ").append(toIndentedString(compatibleMods)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

