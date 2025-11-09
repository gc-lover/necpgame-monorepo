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
 * SettlementUpdateRequestUpgradePlanCommitmentsInner
 */

@JsonTypeName("SettlementUpdateRequest_upgradePlan_commitments_inner")

public class SettlementUpdateRequestUpgradePlanCommitmentsInner {

  private @Nullable String requirement;

  private @Nullable BigDecimal value;

  public SettlementUpdateRequestUpgradePlanCommitmentsInner requirement(@Nullable String requirement) {
    this.requirement = requirement;
    return this;
  }

  /**
   * Get requirement
   * @return requirement
   */
  
  @Schema(name = "requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirement")
  public @Nullable String getRequirement() {
    return requirement;
  }

  public void setRequirement(@Nullable String requirement) {
    this.requirement = requirement;
  }

  public SettlementUpdateRequestUpgradePlanCommitmentsInner value(@Nullable BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable BigDecimal getValue() {
    return value;
  }

  public void setValue(@Nullable BigDecimal value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettlementUpdateRequestUpgradePlanCommitmentsInner settlementUpdateRequestUpgradePlanCommitmentsInner = (SettlementUpdateRequestUpgradePlanCommitmentsInner) o;
    return Objects.equals(this.requirement, settlementUpdateRequestUpgradePlanCommitmentsInner.requirement) &&
        Objects.equals(this.value, settlementUpdateRequestUpgradePlanCommitmentsInner.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requirement, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettlementUpdateRequestUpgradePlanCommitmentsInner {\n");
    sb.append("    requirement: ").append(toIndentedString(requirement)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

