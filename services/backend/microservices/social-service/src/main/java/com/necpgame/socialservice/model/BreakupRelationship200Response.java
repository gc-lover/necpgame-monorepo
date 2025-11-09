package com.necpgame.socialservice.model;

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
 * BreakupRelationship200Response
 */

@JsonTypeName("breakupRelationship_200_response")

public class BreakupRelationship200Response {

  private @Nullable Integer reputationImpact;

  private @Nullable Boolean possibleReconciliation;

  public BreakupRelationship200Response reputationImpact(@Nullable Integer reputationImpact) {
    this.reputationImpact = reputationImpact;
    return this;
  }

  /**
   * Get reputationImpact
   * @return reputationImpact
   */
  
  @Schema(name = "reputation_impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_impact")
  public @Nullable Integer getReputationImpact() {
    return reputationImpact;
  }

  public void setReputationImpact(@Nullable Integer reputationImpact) {
    this.reputationImpact = reputationImpact;
  }

  public BreakupRelationship200Response possibleReconciliation(@Nullable Boolean possibleReconciliation) {
    this.possibleReconciliation = possibleReconciliation;
    return this;
  }

  /**
   * Get possibleReconciliation
   * @return possibleReconciliation
   */
  
  @Schema(name = "possible_reconciliation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("possible_reconciliation")
  public @Nullable Boolean getPossibleReconciliation() {
    return possibleReconciliation;
  }

  public void setPossibleReconciliation(@Nullable Boolean possibleReconciliation) {
    this.possibleReconciliation = possibleReconciliation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BreakupRelationship200Response breakupRelationship200Response = (BreakupRelationship200Response) o;
    return Objects.equals(this.reputationImpact, breakupRelationship200Response.reputationImpact) &&
        Objects.equals(this.possibleReconciliation, breakupRelationship200Response.possibleReconciliation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationImpact, possibleReconciliation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BreakupRelationship200Response {\n");
    sb.append("    reputationImpact: ").append(toIndentedString(reputationImpact)).append("\n");
    sb.append("    possibleReconciliation: ").append(toIndentedString(possibleReconciliation)).append("\n");
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

