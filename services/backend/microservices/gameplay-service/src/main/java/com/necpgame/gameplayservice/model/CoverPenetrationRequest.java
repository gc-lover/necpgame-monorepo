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
 * CoverPenetrationRequest
 */


public class CoverPenetrationRequest {

  private String weaponId;

  /**
   * Gets or Sets ammoType
   */
  public enum AmmoTypeEnum {
    STANDARD("standard"),
    
    ARMOR_PIERCING("armor_piercing"),
    
    ENERGY("energy"),
    
    EXPLOSIVE("explosive");

    private final String value;

    AmmoTypeEnum(String value) {
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
    public static AmmoTypeEnum fromValue(String value) {
      for (AmmoTypeEnum b : AmmoTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AmmoTypeEnum ammoType;

  /**
   * Gets or Sets coverMaterial
   */
  public enum CoverMaterialEnum {
    WOOD("wood"),
    
    METAL("metal"),
    
    CONCRETE("concrete"),
    
    ENERGY_SHIELD("energy_shield");

    private final String value;

    CoverMaterialEnum(String value) {
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
    public static CoverMaterialEnum fromValue(String value) {
      for (CoverMaterialEnum b : CoverMaterialEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CoverMaterialEnum coverMaterial;

  private BigDecimal coverThickness;

  public CoverPenetrationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CoverPenetrationRequest(String weaponId, AmmoTypeEnum ammoType, CoverMaterialEnum coverMaterial, BigDecimal coverThickness) {
    this.weaponId = weaponId;
    this.ammoType = ammoType;
    this.coverMaterial = coverMaterial;
    this.coverThickness = coverThickness;
  }

  public CoverPenetrationRequest weaponId(String weaponId) {
    this.weaponId = weaponId;
    return this;
  }

  /**
   * Get weaponId
   * @return weaponId
   */
  @NotNull 
  @Schema(name = "weapon_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weapon_id")
  public String getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(String weaponId) {
    this.weaponId = weaponId;
  }

  public CoverPenetrationRequest ammoType(AmmoTypeEnum ammoType) {
    this.ammoType = ammoType;
    return this;
  }

  /**
   * Get ammoType
   * @return ammoType
   */
  @NotNull 
  @Schema(name = "ammo_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ammo_type")
  public AmmoTypeEnum getAmmoType() {
    return ammoType;
  }

  public void setAmmoType(AmmoTypeEnum ammoType) {
    this.ammoType = ammoType;
  }

  public CoverPenetrationRequest coverMaterial(CoverMaterialEnum coverMaterial) {
    this.coverMaterial = coverMaterial;
    return this;
  }

  /**
   * Get coverMaterial
   * @return coverMaterial
   */
  @NotNull 
  @Schema(name = "cover_material", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cover_material")
  public CoverMaterialEnum getCoverMaterial() {
    return coverMaterial;
  }

  public void setCoverMaterial(CoverMaterialEnum coverMaterial) {
    this.coverMaterial = coverMaterial;
  }

  public CoverPenetrationRequest coverThickness(BigDecimal coverThickness) {
    this.coverThickness = coverThickness;
    return this;
  }

  /**
   * Толщина в сантиметрах
   * minimum: 0
   * @return coverThickness
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "cover_thickness", description = "Толщина в сантиметрах", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cover_thickness")
  public BigDecimal getCoverThickness() {
    return coverThickness;
  }

  public void setCoverThickness(BigDecimal coverThickness) {
    this.coverThickness = coverThickness;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoverPenetrationRequest coverPenetrationRequest = (CoverPenetrationRequest) o;
    return Objects.equals(this.weaponId, coverPenetrationRequest.weaponId) &&
        Objects.equals(this.ammoType, coverPenetrationRequest.ammoType) &&
        Objects.equals(this.coverMaterial, coverPenetrationRequest.coverMaterial) &&
        Objects.equals(this.coverThickness, coverPenetrationRequest.coverThickness);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weaponId, ammoType, coverMaterial, coverThickness);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoverPenetrationRequest {\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
    sb.append("    ammoType: ").append(toIndentedString(ammoType)).append("\n");
    sb.append("    coverMaterial: ").append(toIndentedString(coverMaterial)).append("\n");
    sb.append("    coverThickness: ").append(toIndentedString(coverThickness)).append("\n");
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

