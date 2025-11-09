package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WeaponSummary
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class WeaponSummary {

  private String id;

  private String name;

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

  private @Nullable BigDecimal damage;

  private @Nullable BigDecimal fireRate;

  public WeaponSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WeaponSummary(String id, String name, WeaponClassEnum weaponClass, RarityEnum rarity) {
    this.id = id;
    this.name = name;
    this.weaponClass = weaponClass;
    this.rarity = rarity;
  }

  public WeaponSummary id(String id) {
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

  public WeaponSummary name(String name) {
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

  public WeaponSummary weaponClass(WeaponClassEnum weaponClass) {
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

  public WeaponSummary brand(@Nullable String brand) {
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

  public WeaponSummary rarity(RarityEnum rarity) {
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

  public WeaponSummary damage(@Nullable BigDecimal damage) {
    this.damage = damage;
    return this;
  }

  /**
   * Get damage
   * @return damage
   */
  @Valid 
  @Schema(name = "damage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage")
  public @Nullable BigDecimal getDamage() {
    return damage;
  }

  public void setDamage(@Nullable BigDecimal damage) {
    this.damage = damage;
  }

  public WeaponSummary fireRate(@Nullable BigDecimal fireRate) {
    this.fireRate = fireRate;
    return this;
  }

  /**
   * Get fireRate
   * @return fireRate
   */
  @Valid 
  @Schema(name = "fire_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fire_rate")
  public @Nullable BigDecimal getFireRate() {
    return fireRate;
  }

  public void setFireRate(@Nullable BigDecimal fireRate) {
    this.fireRate = fireRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WeaponSummary weaponSummary = (WeaponSummary) o;
    return Objects.equals(this.id, weaponSummary.id) &&
        Objects.equals(this.name, weaponSummary.name) &&
        Objects.equals(this.weaponClass, weaponSummary.weaponClass) &&
        Objects.equals(this.brand, weaponSummary.brand) &&
        Objects.equals(this.rarity, weaponSummary.rarity) &&
        Objects.equals(this.damage, weaponSummary.damage) &&
        Objects.equals(this.fireRate, weaponSummary.fireRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, weaponClass, brand, rarity, damage, fireRate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WeaponSummary {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    weaponClass: ").append(toIndentedString(weaponClass)).append("\n");
    sb.append("    brand: ").append(toIndentedString(brand)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    fireRate: ").append(toIndentedString(fireRate)).append("\n");
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


