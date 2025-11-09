package com.necpgame.worldservice.model;

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
 * EraMechanicsSocialMechanics
 */

@JsonTypeName("EraMechanics_social_mechanics")

public class EraMechanicsSocialMechanics {

  private @Nullable BigDecimal reputationVolatility;

  private @Nullable BigDecimal factionHostilityMultiplier;

  public EraMechanicsSocialMechanics reputationVolatility(@Nullable BigDecimal reputationVolatility) {
    this.reputationVolatility = reputationVolatility;
    return this;
  }

  /**
   * Get reputationVolatility
   * @return reputationVolatility
   */
  @Valid 
  @Schema(name = "reputation_volatility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_volatility")
  public @Nullable BigDecimal getReputationVolatility() {
    return reputationVolatility;
  }

  public void setReputationVolatility(@Nullable BigDecimal reputationVolatility) {
    this.reputationVolatility = reputationVolatility;
  }

  public EraMechanicsSocialMechanics factionHostilityMultiplier(@Nullable BigDecimal factionHostilityMultiplier) {
    this.factionHostilityMultiplier = factionHostilityMultiplier;
    return this;
  }

  /**
   * Get factionHostilityMultiplier
   * @return factionHostilityMultiplier
   */
  @Valid 
  @Schema(name = "faction_hostility_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_hostility_multiplier")
  public @Nullable BigDecimal getFactionHostilityMultiplier() {
    return factionHostilityMultiplier;
  }

  public void setFactionHostilityMultiplier(@Nullable BigDecimal factionHostilityMultiplier) {
    this.factionHostilityMultiplier = factionHostilityMultiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraMechanicsSocialMechanics eraMechanicsSocialMechanics = (EraMechanicsSocialMechanics) o;
    return Objects.equals(this.reputationVolatility, eraMechanicsSocialMechanics.reputationVolatility) &&
        Objects.equals(this.factionHostilityMultiplier, eraMechanicsSocialMechanics.factionHostilityMultiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationVolatility, factionHostilityMultiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraMechanicsSocialMechanics {\n");
    sb.append("    reputationVolatility: ").append(toIndentedString(reputationVolatility)).append("\n");
    sb.append("    factionHostilityMultiplier: ").append(toIndentedString(factionHostilityMultiplier)).append("\n");
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

