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
 * AbilityDetailCosts
 */

@JsonTypeName("AbilityDetail_costs")

public class AbilityDetailCosts {

  private @Nullable BigDecimal energy;

  private @Nullable BigDecimal cooldown;

  private @Nullable Integer charges;

  public AbilityDetailCosts energy(@Nullable BigDecimal energy) {
    this.energy = energy;
    return this;
  }

  /**
   * Get energy
   * @return energy
   */
  @Valid 
  @Schema(name = "energy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energy")
  public @Nullable BigDecimal getEnergy() {
    return energy;
  }

  public void setEnergy(@Nullable BigDecimal energy) {
    this.energy = energy;
  }

  public AbilityDetailCosts cooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
    return this;
  }

  /**
   * Get cooldown
   * @return cooldown
   */
  @Valid 
  @Schema(name = "cooldown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldown")
  public @Nullable BigDecimal getCooldown() {
    return cooldown;
  }

  public void setCooldown(@Nullable BigDecimal cooldown) {
    this.cooldown = cooldown;
  }

  public AbilityDetailCosts charges(@Nullable Integer charges) {
    this.charges = charges;
    return this;
  }

  /**
   * Get charges
   * @return charges
   */
  
  @Schema(name = "charges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("charges")
  public @Nullable Integer getCharges() {
    return charges;
  }

  public void setCharges(@Nullable Integer charges) {
    this.charges = charges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityDetailCosts abilityDetailCosts = (AbilityDetailCosts) o;
    return Objects.equals(this.energy, abilityDetailCosts.energy) &&
        Objects.equals(this.cooldown, abilityDetailCosts.cooldown) &&
        Objects.equals(this.charges, abilityDetailCosts.charges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(energy, cooldown, charges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityDetailCosts {\n");
    sb.append("    energy: ").append(toIndentedString(energy)).append("\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
    sb.append("    charges: ").append(toIndentedString(charges)).append("\n");
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

