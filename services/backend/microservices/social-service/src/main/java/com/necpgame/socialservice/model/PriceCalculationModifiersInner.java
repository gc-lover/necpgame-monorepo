package com.necpgame.socialservice.model;

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
 * PriceCalculationModifiersInner
 */

@JsonTypeName("PriceCalculation_modifiers_inner")

public class PriceCalculationModifiersInner {

  private @Nullable String modifierType;

  private @Nullable BigDecimal value;

  public PriceCalculationModifiersInner modifierType(@Nullable String modifierType) {
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

  public PriceCalculationModifiersInner value(@Nullable BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable BigDecimal getValue() {
    return value;
  }

  public void setValue(@Nullable BigDecimal value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceCalculationModifiersInner priceCalculationModifiersInner = (PriceCalculationModifiersInner) o;
    return Objects.equals(this.modifierType, priceCalculationModifiersInner.modifierType) &&
        Objects.equals(this.value, priceCalculationModifiersInner.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modifierType, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceCalculationModifiersInner {\n");
    sb.append("    modifierType: ").append(toIndentedString(modifierType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

