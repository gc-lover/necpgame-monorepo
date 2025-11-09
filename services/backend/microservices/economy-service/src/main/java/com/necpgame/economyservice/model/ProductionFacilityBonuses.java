package com.necpgame.economyservice.model;

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
 * ProductionFacilityBonuses
 */

@JsonTypeName("ProductionFacility_bonuses")

public class ProductionFacilityBonuses {

  private @Nullable BigDecimal timeReduction;

  private @Nullable BigDecimal qualityBonus;

  private @Nullable BigDecimal costReduction;

  public ProductionFacilityBonuses timeReduction(@Nullable BigDecimal timeReduction) {
    this.timeReduction = timeReduction;
    return this;
  }

  /**
   * Get timeReduction
   * @return timeReduction
   */
  @Valid 
  @Schema(name = "time_reduction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_reduction")
  public @Nullable BigDecimal getTimeReduction() {
    return timeReduction;
  }

  public void setTimeReduction(@Nullable BigDecimal timeReduction) {
    this.timeReduction = timeReduction;
  }

  public ProductionFacilityBonuses qualityBonus(@Nullable BigDecimal qualityBonus) {
    this.qualityBonus = qualityBonus;
    return this;
  }

  /**
   * Get qualityBonus
   * @return qualityBonus
   */
  @Valid 
  @Schema(name = "quality_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_bonus")
  public @Nullable BigDecimal getQualityBonus() {
    return qualityBonus;
  }

  public void setQualityBonus(@Nullable BigDecimal qualityBonus) {
    this.qualityBonus = qualityBonus;
  }

  public ProductionFacilityBonuses costReduction(@Nullable BigDecimal costReduction) {
    this.costReduction = costReduction;
    return this;
  }

  /**
   * Get costReduction
   * @return costReduction
   */
  @Valid 
  @Schema(name = "cost_reduction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_reduction")
  public @Nullable BigDecimal getCostReduction() {
    return costReduction;
  }

  public void setCostReduction(@Nullable BigDecimal costReduction) {
    this.costReduction = costReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductionFacilityBonuses productionFacilityBonuses = (ProductionFacilityBonuses) o;
    return Objects.equals(this.timeReduction, productionFacilityBonuses.timeReduction) &&
        Objects.equals(this.qualityBonus, productionFacilityBonuses.qualityBonus) &&
        Objects.equals(this.costReduction, productionFacilityBonuses.costReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeReduction, qualityBonus, costReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductionFacilityBonuses {\n");
    sb.append("    timeReduction: ").append(toIndentedString(timeReduction)).append("\n");
    sb.append("    qualityBonus: ").append(toIndentedString(qualityBonus)).append("\n");
    sb.append("    costReduction: ").append(toIndentedString(costReduction)).append("\n");
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

