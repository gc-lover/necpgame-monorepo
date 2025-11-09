package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReputationFormula
 */


public class ReputationFormula {

  private @Nullable String formulaType;

  private @Nullable Object parameters;

  public ReputationFormula formulaType(@Nullable String formulaType) {
    this.formulaType = formulaType;
    return this;
  }

  /**
   * Get formulaType
   * @return formulaType
   */
  
  @Schema(name = "formula_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("formula_type")
  public @Nullable String getFormulaType() {
    return formulaType;
  }

  public void setFormulaType(@Nullable String formulaType) {
    this.formulaType = formulaType;
  }

  public ReputationFormula parameters(@Nullable Object parameters) {
    this.parameters = parameters;
    return this;
  }

  /**
   * Get parameters
   * @return parameters
   */
  
  @Schema(name = "parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parameters")
  public @Nullable Object getParameters() {
    return parameters;
  }

  public void setParameters(@Nullable Object parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReputationFormula reputationFormula = (ReputationFormula) o;
    return Objects.equals(this.formulaType, reputationFormula.formulaType) &&
        Objects.equals(this.parameters, reputationFormula.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(formulaType, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReputationFormula {\n");
    sb.append("    formulaType: ").append(toIndentedString(formulaType)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

