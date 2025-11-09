package com.necpgame.backjava.model;

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
 * Шансы качества
 */

@Schema(name = "CraftingCalculation_quality_chances", description = "Шансы качества")
@JsonTypeName("CraftingCalculation_quality_chances")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CraftingCalculationQualityChances {

  private @Nullable BigDecimal poor;

  private @Nullable BigDecimal common;

  private @Nullable BigDecimal uncommon;

  private @Nullable BigDecimal rare;

  private @Nullable BigDecimal epic;

  private @Nullable BigDecimal legendary;

  public CraftingCalculationQualityChances poor(@Nullable BigDecimal poor) {
    this.poor = poor;
    return this;
  }

  /**
   * Get poor
   * @return poor
   */
  @Valid 
  @Schema(name = "poor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("poor")
  public @Nullable BigDecimal getPoor() {
    return poor;
  }

  public void setPoor(@Nullable BigDecimal poor) {
    this.poor = poor;
  }

  public CraftingCalculationQualityChances common(@Nullable BigDecimal common) {
    this.common = common;
    return this;
  }

  /**
   * Get common
   * @return common
   */
  @Valid 
  @Schema(name = "common", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("common")
  public @Nullable BigDecimal getCommon() {
    return common;
  }

  public void setCommon(@Nullable BigDecimal common) {
    this.common = common;
  }

  public CraftingCalculationQualityChances uncommon(@Nullable BigDecimal uncommon) {
    this.uncommon = uncommon;
    return this;
  }

  /**
   * Get uncommon
   * @return uncommon
   */
  @Valid 
  @Schema(name = "uncommon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uncommon")
  public @Nullable BigDecimal getUncommon() {
    return uncommon;
  }

  public void setUncommon(@Nullable BigDecimal uncommon) {
    this.uncommon = uncommon;
  }

  public CraftingCalculationQualityChances rare(@Nullable BigDecimal rare) {
    this.rare = rare;
    return this;
  }

  /**
   * Get rare
   * @return rare
   */
  @Valid 
  @Schema(name = "rare", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rare")
  public @Nullable BigDecimal getRare() {
    return rare;
  }

  public void setRare(@Nullable BigDecimal rare) {
    this.rare = rare;
  }

  public CraftingCalculationQualityChances epic(@Nullable BigDecimal epic) {
    this.epic = epic;
    return this;
  }

  /**
   * Get epic
   * @return epic
   */
  @Valid 
  @Schema(name = "epic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("epic")
  public @Nullable BigDecimal getEpic() {
    return epic;
  }

  public void setEpic(@Nullable BigDecimal epic) {
    this.epic = epic;
  }

  public CraftingCalculationQualityChances legendary(@Nullable BigDecimal legendary) {
    this.legendary = legendary;
    return this;
  }

  /**
   * Get legendary
   * @return legendary
   */
  @Valid 
  @Schema(name = "legendary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("legendary")
  public @Nullable BigDecimal getLegendary() {
    return legendary;
  }

  public void setLegendary(@Nullable BigDecimal legendary) {
    this.legendary = legendary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingCalculationQualityChances craftingCalculationQualityChances = (CraftingCalculationQualityChances) o;
    return Objects.equals(this.poor, craftingCalculationQualityChances.poor) &&
        Objects.equals(this.common, craftingCalculationQualityChances.common) &&
        Objects.equals(this.uncommon, craftingCalculationQualityChances.uncommon) &&
        Objects.equals(this.rare, craftingCalculationQualityChances.rare) &&
        Objects.equals(this.epic, craftingCalculationQualityChances.epic) &&
        Objects.equals(this.legendary, craftingCalculationQualityChances.legendary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(poor, common, uncommon, rare, epic, legendary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingCalculationQualityChances {\n");
    sb.append("    poor: ").append(toIndentedString(poor)).append("\n");
    sb.append("    common: ").append(toIndentedString(common)).append("\n");
    sb.append("    uncommon: ").append(toIndentedString(uncommon)).append("\n");
    sb.append("    rare: ").append(toIndentedString(rare)).append("\n");
    sb.append("    epic: ").append(toIndentedString(epic)).append("\n");
    sb.append("    legendary: ").append(toIndentedString(legendary)).append("\n");
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

