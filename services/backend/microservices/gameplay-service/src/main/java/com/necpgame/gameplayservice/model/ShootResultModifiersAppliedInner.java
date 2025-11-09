package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ShootResultModifiersAppliedInner
 */

@JsonTypeName("ShootResult_modifiers_applied_inner")

public class ShootResultModifiersAppliedInner {

  private @Nullable String modifierType;

  private @Nullable BigDecimal modifierValue;

  public ShootResultModifiersAppliedInner modifierType(@Nullable String modifierType) {
    this.modifierType = modifierType;
    return this;
  }

  /**
   * Get modifierType
   * @return modifierType
   */
  
  @Schema(name = "modifier_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier_type")
  public @Nullable String getModifierType() {
    return modifierType;
  }

  public void setModifierType(@Nullable String modifierType) {
    this.modifierType = modifierType;
  }

  public ShootResultModifiersAppliedInner modifierValue(@Nullable BigDecimal modifierValue) {
    this.modifierValue = modifierValue;
    return this;
  }

  /**
   * Get modifierValue
   * @return modifierValue
   */
  @Valid 
  @Schema(name = "modifier_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier_value")
  public @Nullable BigDecimal getModifierValue() {
    return modifierValue;
  }

  public void setModifierValue(@Nullable BigDecimal modifierValue) {
    this.modifierValue = modifierValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShootResultModifiersAppliedInner shootResultModifiersAppliedInner = (ShootResultModifiersAppliedInner) o;
    return Objects.equals(this.modifierType, shootResultModifiersAppliedInner.modifierType) &&
        Objects.equals(this.modifierValue, shootResultModifiersAppliedInner.modifierValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modifierType, modifierValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShootResultModifiersAppliedInner {\n");
    sb.append("    modifierType: ").append(toIndentedString(modifierType)).append("\n");
    sb.append("    modifierValue: ").append(toIndentedString(modifierValue)).append("\n");
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

