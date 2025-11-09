package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * EconomicMultipliersTradeRestrictionsInner
 */

@JsonTypeName("EconomicMultipliers_trade_restrictions_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EconomicMultipliersTradeRestrictionsInner {

  private @Nullable String itemCategory;

  /**
   * Gets or Sets restrictionType
   */
  public enum RestrictionTypeEnum {
    BANNED("BANNED"),
    
    REGULATED("REGULATED"),
    
    TAXED("TAXED");

    private final String value;

    RestrictionTypeEnum(String value) {
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
    public static RestrictionTypeEnum fromValue(String value) {
      for (RestrictionTypeEnum b : RestrictionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RestrictionTypeEnum restrictionType;

  private @Nullable BigDecimal multiplier;

  public EconomicMultipliersTradeRestrictionsInner itemCategory(@Nullable String itemCategory) {
    this.itemCategory = itemCategory;
    return this;
  }

  /**
   * Get itemCategory
   * @return itemCategory
   */
  
  @Schema(name = "item_category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_category")
  public @Nullable String getItemCategory() {
    return itemCategory;
  }

  public void setItemCategory(@Nullable String itemCategory) {
    this.itemCategory = itemCategory;
  }

  public EconomicMultipliersTradeRestrictionsInner restrictionType(@Nullable RestrictionTypeEnum restrictionType) {
    this.restrictionType = restrictionType;
    return this;
  }

  /**
   * Get restrictionType
   * @return restrictionType
   */
  
  @Schema(name = "restriction_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restriction_type")
  public @Nullable RestrictionTypeEnum getRestrictionType() {
    return restrictionType;
  }

  public void setRestrictionType(@Nullable RestrictionTypeEnum restrictionType) {
    this.restrictionType = restrictionType;
  }

  public EconomicMultipliersTradeRestrictionsInner multiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Get multiplier
   * @return multiplier
   */
  @Valid 
  @Schema(name = "multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public @Nullable BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomicMultipliersTradeRestrictionsInner economicMultipliersTradeRestrictionsInner = (EconomicMultipliersTradeRestrictionsInner) o;
    return Objects.equals(this.itemCategory, economicMultipliersTradeRestrictionsInner.itemCategory) &&
        Objects.equals(this.restrictionType, economicMultipliersTradeRestrictionsInner.restrictionType) &&
        Objects.equals(this.multiplier, economicMultipliersTradeRestrictionsInner.multiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemCategory, restrictionType, multiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomicMultipliersTradeRestrictionsInner {\n");
    sb.append("    itemCategory: ").append(toIndentedString(itemCategory)).append("\n");
    sb.append("    restrictionType: ").append(toIndentedString(restrictionType)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
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

