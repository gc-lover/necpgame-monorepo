package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AttributeModifiersAttributesValue
 */

@JsonTypeName("AttributeModifiers_attributes_value")

public class AttributeModifiersAttributesValue {

  private @Nullable Integer baseValue;

  private @Nullable Integer equipmentBonus;

  private @Nullable Integer buffBonus;

  private @Nullable Integer temporaryBonus;

  private @Nullable Integer total;

  private @Nullable Integer modifier;

  public AttributeModifiersAttributesValue baseValue(@Nullable Integer baseValue) {
    this.baseValue = baseValue;
    return this;
  }

  /**
   * Get baseValue
   * @return baseValue
   */
  
  @Schema(name = "base_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_value")
  public @Nullable Integer getBaseValue() {
    return baseValue;
  }

  public void setBaseValue(@Nullable Integer baseValue) {
    this.baseValue = baseValue;
  }

  public AttributeModifiersAttributesValue equipmentBonus(@Nullable Integer equipmentBonus) {
    this.equipmentBonus = equipmentBonus;
    return this;
  }

  /**
   * Get equipmentBonus
   * @return equipmentBonus
   */
  
  @Schema(name = "equipment_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equipment_bonus")
  public @Nullable Integer getEquipmentBonus() {
    return equipmentBonus;
  }

  public void setEquipmentBonus(@Nullable Integer equipmentBonus) {
    this.equipmentBonus = equipmentBonus;
  }

  public AttributeModifiersAttributesValue buffBonus(@Nullable Integer buffBonus) {
    this.buffBonus = buffBonus;
    return this;
  }

  /**
   * Get buffBonus
   * @return buffBonus
   */
  
  @Schema(name = "buff_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buff_bonus")
  public @Nullable Integer getBuffBonus() {
    return buffBonus;
  }

  public void setBuffBonus(@Nullable Integer buffBonus) {
    this.buffBonus = buffBonus;
  }

  public AttributeModifiersAttributesValue temporaryBonus(@Nullable Integer temporaryBonus) {
    this.temporaryBonus = temporaryBonus;
    return this;
  }

  /**
   * Get temporaryBonus
   * @return temporaryBonus
   */
  
  @Schema(name = "temporary_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("temporary_bonus")
  public @Nullable Integer getTemporaryBonus() {
    return temporaryBonus;
  }

  public void setTemporaryBonus(@Nullable Integer temporaryBonus) {
    this.temporaryBonus = temporaryBonus;
  }

  public AttributeModifiersAttributesValue total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public AttributeModifiersAttributesValue modifier(@Nullable Integer modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * D&D modifier ((total - 10) / 2)
   * @return modifier
   */
  
  @Schema(name = "modifier", description = "D&D modifier ((total - 10) / 2)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier")
  public @Nullable Integer getModifier() {
    return modifier;
  }

  public void setModifier(@Nullable Integer modifier) {
    this.modifier = modifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeModifiersAttributesValue attributeModifiersAttributesValue = (AttributeModifiersAttributesValue) o;
    return Objects.equals(this.baseValue, attributeModifiersAttributesValue.baseValue) &&
        Objects.equals(this.equipmentBonus, attributeModifiersAttributesValue.equipmentBonus) &&
        Objects.equals(this.buffBonus, attributeModifiersAttributesValue.buffBonus) &&
        Objects.equals(this.temporaryBonus, attributeModifiersAttributesValue.temporaryBonus) &&
        Objects.equals(this.total, attributeModifiersAttributesValue.total) &&
        Objects.equals(this.modifier, attributeModifiersAttributesValue.modifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseValue, equipmentBonus, buffBonus, temporaryBonus, total, modifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeModifiersAttributesValue {\n");
    sb.append("    baseValue: ").append(toIndentedString(baseValue)).append("\n");
    sb.append("    equipmentBonus: ").append(toIndentedString(equipmentBonus)).append("\n");
    sb.append("    buffBonus: ").append(toIndentedString(buffBonus)).append("\n");
    sb.append("    temporaryBonus: ").append(toIndentedString(temporaryBonus)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
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

