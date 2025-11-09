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
 * FactionChainTelemetry
 */

@JsonTypeName("FactionChain_telemetry")

public class FactionChainTelemetry {

  private @Nullable BigDecimal contractSuccessRate;

  private @Nullable BigDecimal branchPreferenceIndex;

  public FactionChainTelemetry contractSuccessRate(@Nullable BigDecimal contractSuccessRate) {
    this.contractSuccessRate = contractSuccessRate;
    return this;
  }

  /**
   * Get contractSuccessRate
   * minimum: 0
   * maximum: 1
   * @return contractSuccessRate
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "contractSuccessRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contractSuccessRate")
  public @Nullable BigDecimal getContractSuccessRate() {
    return contractSuccessRate;
  }

  public void setContractSuccessRate(@Nullable BigDecimal contractSuccessRate) {
    this.contractSuccessRate = contractSuccessRate;
  }

  public FactionChainTelemetry branchPreferenceIndex(@Nullable BigDecimal branchPreferenceIndex) {
    this.branchPreferenceIndex = branchPreferenceIndex;
    return this;
  }

  /**
   * Get branchPreferenceIndex
   * minimum: 0
   * maximum: 1
   * @return branchPreferenceIndex
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "branchPreferenceIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branchPreferenceIndex")
  public @Nullable BigDecimal getBranchPreferenceIndex() {
    return branchPreferenceIndex;
  }

  public void setBranchPreferenceIndex(@Nullable BigDecimal branchPreferenceIndex) {
    this.branchPreferenceIndex = branchPreferenceIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionChainTelemetry factionChainTelemetry = (FactionChainTelemetry) o;
    return Objects.equals(this.contractSuccessRate, factionChainTelemetry.contractSuccessRate) &&
        Objects.equals(this.branchPreferenceIndex, factionChainTelemetry.branchPreferenceIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contractSuccessRate, branchPreferenceIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionChainTelemetry {\n");
    sb.append("    contractSuccessRate: ").append(toIndentedString(contractSuccessRate)).append("\n");
    sb.append("    branchPreferenceIndex: ").append(toIndentedString(branchPreferenceIndex)).append("\n");
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

