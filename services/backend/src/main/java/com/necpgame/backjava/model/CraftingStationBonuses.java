package com.necpgame.backjava.model;

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
 * CraftingStationBonuses
 */

@JsonTypeName("CraftingStation_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CraftingStationBonuses {

  private @Nullable Float successRateBonus;

  private @Nullable Float timeReduction;

  private @Nullable Float qualityChanceBonus;

  public CraftingStationBonuses successRateBonus(@Nullable Float successRateBonus) {
    this.successRateBonus = successRateBonus;
    return this;
  }

  /**
   * Get successRateBonus
   * @return successRateBonus
   */
  
  @Schema(name = "success_rate_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success_rate_bonus")
  public @Nullable Float getSuccessRateBonus() {
    return successRateBonus;
  }

  public void setSuccessRateBonus(@Nullable Float successRateBonus) {
    this.successRateBonus = successRateBonus;
  }

  public CraftingStationBonuses timeReduction(@Nullable Float timeReduction) {
    this.timeReduction = timeReduction;
    return this;
  }

  /**
   * Get timeReduction
   * @return timeReduction
   */
  
  @Schema(name = "time_reduction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_reduction")
  public @Nullable Float getTimeReduction() {
    return timeReduction;
  }

  public void setTimeReduction(@Nullable Float timeReduction) {
    this.timeReduction = timeReduction;
  }

  public CraftingStationBonuses qualityChanceBonus(@Nullable Float qualityChanceBonus) {
    this.qualityChanceBonus = qualityChanceBonus;
    return this;
  }

  /**
   * Get qualityChanceBonus
   * @return qualityChanceBonus
   */
  
  @Schema(name = "quality_chance_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality_chance_bonus")
  public @Nullable Float getQualityChanceBonus() {
    return qualityChanceBonus;
  }

  public void setQualityChanceBonus(@Nullable Float qualityChanceBonus) {
    this.qualityChanceBonus = qualityChanceBonus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingStationBonuses craftingStationBonuses = (CraftingStationBonuses) o;
    return Objects.equals(this.successRateBonus, craftingStationBonuses.successRateBonus) &&
        Objects.equals(this.timeReduction, craftingStationBonuses.timeReduction) &&
        Objects.equals(this.qualityChanceBonus, craftingStationBonuses.qualityChanceBonus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(successRateBonus, timeReduction, qualityChanceBonus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingStationBonuses {\n");
    sb.append("    successRateBonus: ").append(toIndentedString(successRateBonus)).append("\n");
    sb.append("    timeReduction: ").append(toIndentedString(timeReduction)).append("\n");
    sb.append("    qualityChanceBonus: ").append(toIndentedString(qualityChanceBonus)).append("\n");
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

