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
 * EnergyBudgetImplantsConsumptionInner
 */

@JsonTypeName("EnergyBudget_implants_consumption_inner")

public class EnergyBudgetImplantsConsumptionInner {

  private @Nullable String implantId;

  private @Nullable String implantName;

  private @Nullable BigDecimal energyCost;

  public EnergyBudgetImplantsConsumptionInner implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public EnergyBudgetImplantsConsumptionInner implantName(@Nullable String implantName) {
    this.implantName = implantName;
    return this;
  }

  /**
   * Get implantName
   * @return implantName
   */
  
  @Schema(name = "implant_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_name")
  public @Nullable String getImplantName() {
    return implantName;
  }

  public void setImplantName(@Nullable String implantName) {
    this.implantName = implantName;
  }

  public EnergyBudgetImplantsConsumptionInner energyCost(@Nullable BigDecimal energyCost) {
    this.energyCost = energyCost;
    return this;
  }

  /**
   * Get energyCost
   * @return energyCost
   */
  @Valid 
  @Schema(name = "energy_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy_cost")
  public @Nullable BigDecimal getEnergyCost() {
    return energyCost;
  }

  public void setEnergyCost(@Nullable BigDecimal energyCost) {
    this.energyCost = energyCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnergyBudgetImplantsConsumptionInner energyBudgetImplantsConsumptionInner = (EnergyBudgetImplantsConsumptionInner) o;
    return Objects.equals(this.implantId, energyBudgetImplantsConsumptionInner.implantId) &&
        Objects.equals(this.implantName, energyBudgetImplantsConsumptionInner.implantName) &&
        Objects.equals(this.energyCost, energyBudgetImplantsConsumptionInner.energyCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, implantName, energyCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnergyBudgetImplantsConsumptionInner {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    implantName: ").append(toIndentedString(implantName)).append("\n");
    sb.append("    energyCost: ").append(toIndentedString(energyCost)).append("\n");
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

