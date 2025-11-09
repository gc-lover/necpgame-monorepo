package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ImplantLimitCalculationBonuses;
import com.necpgame.gameplayservice.model.ImplantLimitCalculationPenalties;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Расчет лимита имплантов с учетом всех факторов. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -&gt; Лимит имплантов 
 */

@Schema(name = "ImplantLimitCalculation", description = "Расчет лимита имплантов с учетом всех факторов. Источник: .BRAIN/02-gameplay/combat/combat-implants-limits.md -> Лимит имплантов ")

public class ImplantLimitCalculation {

  private Integer base;

  private @Nullable ImplantLimitCalculationBonuses bonuses;

  private @Nullable ImplantLimitCalculationPenalties penalties;

  private Integer total;

  private @Nullable String breakdown;

  public ImplantLimitCalculation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ImplantLimitCalculation(Integer base, Integer total) {
    this.base = base;
    this.total = total;
  }

  public ImplantLimitCalculation base(Integer base) {
    this.base = base;
    return this;
  }

  /**
   * Базовый лимит
   * minimum: 0
   * @return base
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "base", description = "Базовый лимит", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("base")
  public Integer getBase() {
    return base;
  }

  public void setBase(Integer base) {
    this.base = base;
  }

  public ImplantLimitCalculation bonuses(@Nullable ImplantLimitCalculationBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable ImplantLimitCalculationBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable ImplantLimitCalculationBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public ImplantLimitCalculation penalties(@Nullable ImplantLimitCalculationPenalties penalties) {
    this.penalties = penalties;
    return this;
  }

  /**
   * Get penalties
   * @return penalties
   */
  @Valid 
  @Schema(name = "penalties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penalties")
  public @Nullable ImplantLimitCalculationPenalties getPenalties() {
    return penalties;
  }

  public void setPenalties(@Nullable ImplantLimitCalculationPenalties penalties) {
    this.penalties = penalties;
  }

  public ImplantLimitCalculation total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Итоговый лимит
   * minimum: 0
   * @return total
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "total", description = "Итоговый лимит", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("total")
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public ImplantLimitCalculation breakdown(@Nullable String breakdown) {
    this.breakdown = breakdown;
    return this;
  }

  /**
   * Описание расчета
   * @return breakdown
   */
  
  @Schema(name = "breakdown", description = "Описание расчета", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breakdown")
  public @Nullable String getBreakdown() {
    return breakdown;
  }

  public void setBreakdown(@Nullable String breakdown) {
    this.breakdown = breakdown;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImplantLimitCalculation implantLimitCalculation = (ImplantLimitCalculation) o;
    return Objects.equals(this.base, implantLimitCalculation.base) &&
        Objects.equals(this.bonuses, implantLimitCalculation.bonuses) &&
        Objects.equals(this.penalties, implantLimitCalculation.penalties) &&
        Objects.equals(this.total, implantLimitCalculation.total) &&
        Objects.equals(this.breakdown, implantLimitCalculation.breakdown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, bonuses, penalties, total, breakdown);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImplantLimitCalculation {\n");
    sb.append("    base: ").append(toIndentedString(base)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    penalties: ").append(toIndentedString(penalties)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    breakdown: ").append(toIndentedString(breakdown)).append("\n");
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

