package com.necpgame.worldservice.model;

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
 * SettlementUpgradeRequirementsInner
 */

@JsonTypeName("Settlement_upgradeRequirements_inner")

public class SettlementUpgradeRequirementsInner {

  private @Nullable String requirement;

  private @Nullable Boolean fulfilled;

  public SettlementUpgradeRequirementsInner requirement(@Nullable String requirement) {
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

  public SettlementUpgradeRequirementsInner fulfilled(@Nullable Boolean fulfilled) {
    this.fulfilled = fulfilled;
    return this;
  }

  /**
   * Get fulfilled
   * @return fulfilled
   */
  
  @Schema(name = "fulfilled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fulfilled")
  public @Nullable Boolean getFulfilled() {
    return fulfilled;
  }

  public void setFulfilled(@Nullable Boolean fulfilled) {
    this.fulfilled = fulfilled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettlementUpgradeRequirementsInner settlementUpgradeRequirementsInner = (SettlementUpgradeRequirementsInner) o;
    return Objects.equals(this.requirement, settlementUpgradeRequirementsInner.requirement) &&
        Objects.equals(this.fulfilled, settlementUpgradeRequirementsInner.fulfilled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requirement, fulfilled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SettlementUpgradeRequirementsInner {\n");
    sb.append("    requirement: ").append(toIndentedString(requirement)).append("\n");
    sb.append("    fulfilled: ").append(toIndentedString(fulfilled)).append("\n");
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

