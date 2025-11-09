package com.necpgame.gameplayservice.model;

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
 * Weapon
 */


public class Weapon {

  private String id;

  private String name;

  /**
   * Gets or Sets weaponClass
   */
  public enum WeaponClassEnum {
    PISTOL("pistol"),
    
    REVOLVER("revolver"),
    
    ASSAULT_RIFLE("assault_rifle"),
    
    SMG("smg"),
    
    SHOTGUN("shotgun"),
    
    SNIPER("sniper"),
    
    LMG("lmg"),
    
    MELEE("melee");

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

  private BigDecimal damage;

  private BigDecimal fireRate;

  private BigDecimal accuracy;

  private @Nullable Integer magazineSize;

  private @Nullable BigDecimal reloadTime;

  private @Nullable BigDecimal movementPenalty;

  /**
   * Gets or Sets damageType
   */
  public enum DamageTypeEnum {
    PHYSICAL("physical"),
    
    ENERGY("energy"),
    
    CHEMICAL("chemical"),
    
    THERMAL("thermal"),
    
    EMP("emp"),
    
    CYBER("cyber");

    private final String value;

    DamageTypeEnum(String value) {
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
    public static DamageTypeEnum fromValue(String value) {
      for (DamageTypeEnum b : DamageTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DamageTypeEnum damageType;

  private @Nullable BigDecimal penetration;

  public Weapon() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Weapon(String id, String name, WeaponClassEnum weaponClass, BigDecimal damage, BigDecimal fireRate, BigDecimal accuracy) {
    this.id = id;
    this.name = name;
    this.weaponClass = weaponClass;
    this.damage = damage;
    this.fireRate = fireRate;
    this.accuracy = accuracy;
  }

  public Weapon id(String id) {
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

  public Weapon name(String name) {
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

  public Weapon weaponClass(WeaponClassEnum weaponClass) {
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

  public Weapon damage(BigDecimal damage) {
    this.damage = damage;
    return this;
  }

  /**
   * Get damage
   * minimum: 1
   * @return damage
   */
  @NotNull @Valid @DecimalMin(value = "1") 
  @Schema(name = "damage", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("damage")
  public BigDecimal getDamage() {
    return damage;
  }

  public void setDamage(BigDecimal damage) {
    this.damage = damage;
  }

  public Weapon fireRate(BigDecimal fireRate) {
    this.fireRate = fireRate;
    return this;
  }

  /**
   * Выстрелов в секунду
   * @return fireRate
   */
  @NotNull @Valid 
  @Schema(name = "fire_rate", description = "Выстрелов в секунду", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fire_rate")
  public BigDecimal getFireRate() {
    return fireRate;
  }

  public void setFireRate(BigDecimal fireRate) {
    this.fireRate = fireRate;
  }

  public Weapon accuracy(BigDecimal accuracy) {
    this.accuracy = accuracy;
    return this;
  }

  /**
   * Get accuracy
   * minimum: 0
   * maximum: 100
   * @return accuracy
   */
  @NotNull @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "accuracy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("accuracy")
  public BigDecimal getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(BigDecimal accuracy) {
    this.accuracy = accuracy;
  }

  public Weapon magazineSize(@Nullable Integer magazineSize) {
    this.magazineSize = magazineSize;
    return this;
  }

  /**
   * Get magazineSize
   * @return magazineSize
   */
  
  @Schema(name = "magazine_size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("magazine_size")
  public @Nullable Integer getMagazineSize() {
    return magazineSize;
  }

  public void setMagazineSize(@Nullable Integer magazineSize) {
    this.magazineSize = magazineSize;
  }

  public Weapon reloadTime(@Nullable BigDecimal reloadTime) {
    this.reloadTime = reloadTime;
    return this;
  }

  /**
   * Время перезарядки (секунды)
   * @return reloadTime
   */
  @Valid 
  @Schema(name = "reload_time", description = "Время перезарядки (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reload_time")
  public @Nullable BigDecimal getReloadTime() {
    return reloadTime;
  }

  public void setReloadTime(@Nullable BigDecimal reloadTime) {
    this.reloadTime = reloadTime;
  }

  public Weapon movementPenalty(@Nullable BigDecimal movementPenalty) {
    this.movementPenalty = movementPenalty;
    return this;
  }

  /**
   * Штраф к точности при движении (%)
   * @return movementPenalty
   */
  @Valid 
  @Schema(name = "movement_penalty", description = "Штраф к точности при движении (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("movement_penalty")
  public @Nullable BigDecimal getMovementPenalty() {
    return movementPenalty;
  }

  public void setMovementPenalty(@Nullable BigDecimal movementPenalty) {
    this.movementPenalty = movementPenalty;
  }

  public Weapon damageType(@Nullable DamageTypeEnum damageType) {
    this.damageType = damageType;
    return this;
  }

  /**
   * Get damageType
   * @return damageType
   */
  
  @Schema(name = "damage_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_type")
  public @Nullable DamageTypeEnum getDamageType() {
    return damageType;
  }

  public void setDamageType(@Nullable DamageTypeEnum damageType) {
    this.damageType = damageType;
  }

  public Weapon penetration(@Nullable BigDecimal penetration) {
    this.penetration = penetration;
    return this;
  }

  /**
   * Проникающая способность
   * @return penetration
   */
  @Valid 
  @Schema(name = "penetration", description = "Проникающая способность", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penetration")
  public @Nullable BigDecimal getPenetration() {
    return penetration;
  }

  public void setPenetration(@Nullable BigDecimal penetration) {
    this.penetration = penetration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Weapon weapon = (Weapon) o;
    return Objects.equals(this.id, weapon.id) &&
        Objects.equals(this.name, weapon.name) &&
        Objects.equals(this.weaponClass, weapon.weaponClass) &&
        Objects.equals(this.damage, weapon.damage) &&
        Objects.equals(this.fireRate, weapon.fireRate) &&
        Objects.equals(this.accuracy, weapon.accuracy) &&
        Objects.equals(this.magazineSize, weapon.magazineSize) &&
        Objects.equals(this.reloadTime, weapon.reloadTime) &&
        Objects.equals(this.movementPenalty, weapon.movementPenalty) &&
        Objects.equals(this.damageType, weapon.damageType) &&
        Objects.equals(this.penetration, weapon.penetration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, weaponClass, damage, fireRate, accuracy, magazineSize, reloadTime, movementPenalty, damageType, penetration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Weapon {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    weaponClass: ").append(toIndentedString(weaponClass)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    fireRate: ").append(toIndentedString(fireRate)).append("\n");
    sb.append("    accuracy: ").append(toIndentedString(accuracy)).append("\n");
    sb.append("    magazineSize: ").append(toIndentedString(magazineSize)).append("\n");
    sb.append("    reloadTime: ").append(toIndentedString(reloadTime)).append("\n");
    sb.append("    movementPenalty: ").append(toIndentedString(movementPenalty)).append("\n");
    sb.append("    damageType: ").append(toIndentedString(damageType)).append("\n");
    sb.append("    penetration: ").append(toIndentedString(penetration)).append("\n");
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

