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
 * Факторы риска прогрессии
 */

@Schema(name = "CyberpsychosisStatus_risk_factors", description = "Факторы риска прогрессии")
@JsonTypeName("CyberpsychosisStatus_risk_factors")

public class CyberpsychosisStatusRiskFactors {

  private @Nullable Integer implantsCount;

  private @Nullable BigDecimal combatStress;

  private @Nullable Integer damagedImplants;

  private @Nullable Boolean limitExceeded;

  public CyberpsychosisStatusRiskFactors implantsCount(@Nullable Integer implantsCount) {
    this.implantsCount = implantsCount;
    return this;
  }

  /**
   * Get implantsCount
   * @return implantsCount
   */
  
  @Schema(name = "implants_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants_count")
  public @Nullable Integer getImplantsCount() {
    return implantsCount;
  }

  public void setImplantsCount(@Nullable Integer implantsCount) {
    this.implantsCount = implantsCount;
  }

  public CyberpsychosisStatusRiskFactors combatStress(@Nullable BigDecimal combatStress) {
    this.combatStress = combatStress;
    return this;
  }

  /**
   * Get combatStress
   * @return combatStress
   */
  @Valid 
  @Schema(name = "combat_stress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combat_stress")
  public @Nullable BigDecimal getCombatStress() {
    return combatStress;
  }

  public void setCombatStress(@Nullable BigDecimal combatStress) {
    this.combatStress = combatStress;
  }

  public CyberpsychosisStatusRiskFactors damagedImplants(@Nullable Integer damagedImplants) {
    this.damagedImplants = damagedImplants;
    return this;
  }

  /**
   * Get damagedImplants
   * @return damagedImplants
   */
  
  @Schema(name = "damaged_implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damaged_implants")
  public @Nullable Integer getDamagedImplants() {
    return damagedImplants;
  }

  public void setDamagedImplants(@Nullable Integer damagedImplants) {
    this.damagedImplants = damagedImplants;
  }

  public CyberpsychosisStatusRiskFactors limitExceeded(@Nullable Boolean limitExceeded) {
    this.limitExceeded = limitExceeded;
    return this;
  }

  /**
   * Get limitExceeded
   * @return limitExceeded
   */
  
  @Schema(name = "limit_exceeded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limit_exceeded")
  public @Nullable Boolean getLimitExceeded() {
    return limitExceeded;
  }

  public void setLimitExceeded(@Nullable Boolean limitExceeded) {
    this.limitExceeded = limitExceeded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberpsychosisStatusRiskFactors cyberpsychosisStatusRiskFactors = (CyberpsychosisStatusRiskFactors) o;
    return Objects.equals(this.implantsCount, cyberpsychosisStatusRiskFactors.implantsCount) &&
        Objects.equals(this.combatStress, cyberpsychosisStatusRiskFactors.combatStress) &&
        Objects.equals(this.damagedImplants, cyberpsychosisStatusRiskFactors.damagedImplants) &&
        Objects.equals(this.limitExceeded, cyberpsychosisStatusRiskFactors.limitExceeded);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantsCount, combatStress, damagedImplants, limitExceeded);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisStatusRiskFactors {\n");
    sb.append("    implantsCount: ").append(toIndentedString(implantsCount)).append("\n");
    sb.append("    combatStress: ").append(toIndentedString(combatStress)).append("\n");
    sb.append("    damagedImplants: ").append(toIndentedString(damagedImplants)).append("\n");
    sb.append("    limitExceeded: ").append(toIndentedString(limitExceeded)).append("\n");
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

