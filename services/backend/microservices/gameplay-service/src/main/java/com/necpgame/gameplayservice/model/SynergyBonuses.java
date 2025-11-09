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
 * SynergyBonuses
 */

@JsonTypeName("Synergy_bonuses")

public class SynergyBonuses {

  private @Nullable BigDecimal hackingSpeed;

  private @Nullable BigDecimal craftingSuccessRate;

  public SynergyBonuses hackingSpeed(@Nullable BigDecimal hackingSpeed) {
    this.hackingSpeed = hackingSpeed;
    return this;
  }

  /**
   * Get hackingSpeed
   * @return hackingSpeed
   */
  @Valid 
  @Schema(name = "hacking_speed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hacking_speed")
  public @Nullable BigDecimal getHackingSpeed() {
    return hackingSpeed;
  }

  public void setHackingSpeed(@Nullable BigDecimal hackingSpeed) {
    this.hackingSpeed = hackingSpeed;
  }

  public SynergyBonuses craftingSuccessRate(@Nullable BigDecimal craftingSuccessRate) {
    this.craftingSuccessRate = craftingSuccessRate;
    return this;
  }

  /**
   * Get craftingSuccessRate
   * @return craftingSuccessRate
   */
  @Valid 
  @Schema(name = "crafting_success_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crafting_success_rate")
  public @Nullable BigDecimal getCraftingSuccessRate() {
    return craftingSuccessRate;
  }

  public void setCraftingSuccessRate(@Nullable BigDecimal craftingSuccessRate) {
    this.craftingSuccessRate = craftingSuccessRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SynergyBonuses synergyBonuses = (SynergyBonuses) o;
    return Objects.equals(this.hackingSpeed, synergyBonuses.hackingSpeed) &&
        Objects.equals(this.craftingSuccessRate, synergyBonuses.craftingSuccessRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hackingSpeed, craftingSuccessRate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SynergyBonuses {\n");
    sb.append("    hackingSpeed: ").append(toIndentedString(hackingSpeed)).append("\n");
    sb.append("    craftingSuccessRate: ").append(toIndentedString(craftingSuccessRate)).append("\n");
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

