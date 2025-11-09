package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ValidationCategoryResult;
import com.necpgame.worldservice.model.ValidationSummary;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ValidationChecklist
 */


public class ValidationChecklist {

  private ValidationCategoryResult territory;

  private ValidationCategoryResult sanctions;

  private ValidationCategoryResult legal;

  private ValidationCategoryResult toxicity;

  private ValidationCategoryResult budgetBounds;

  private ValidationCategoryResult duplicates;

  private ValidationSummary summary;

  public ValidationChecklist() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidationChecklist(ValidationCategoryResult territory, ValidationCategoryResult sanctions, ValidationCategoryResult legal, ValidationCategoryResult toxicity, ValidationCategoryResult budgetBounds, ValidationCategoryResult duplicates, ValidationSummary summary) {
    this.territory = territory;
    this.sanctions = sanctions;
    this.legal = legal;
    this.toxicity = toxicity;
    this.budgetBounds = budgetBounds;
    this.duplicates = duplicates;
    this.summary = summary;
  }

  public ValidationChecklist territory(ValidationCategoryResult territory) {
    this.territory = territory;
    return this;
  }

  /**
   * Get territory
   * @return territory
   */
  @NotNull @Valid 
  @Schema(name = "territory", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("territory")
  public ValidationCategoryResult getTerritory() {
    return territory;
  }

  public void setTerritory(ValidationCategoryResult territory) {
    this.territory = territory;
  }

  public ValidationChecklist sanctions(ValidationCategoryResult sanctions) {
    this.sanctions = sanctions;
    return this;
  }

  /**
   * Get sanctions
   * @return sanctions
   */
  @NotNull @Valid 
  @Schema(name = "sanctions", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sanctions")
  public ValidationCategoryResult getSanctions() {
    return sanctions;
  }

  public void setSanctions(ValidationCategoryResult sanctions) {
    this.sanctions = sanctions;
  }

  public ValidationChecklist legal(ValidationCategoryResult legal) {
    this.legal = legal;
    return this;
  }

  /**
   * Get legal
   * @return legal
   */
  @NotNull @Valid 
  @Schema(name = "legal", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("legal")
  public ValidationCategoryResult getLegal() {
    return legal;
  }

  public void setLegal(ValidationCategoryResult legal) {
    this.legal = legal;
  }

  public ValidationChecklist toxicity(ValidationCategoryResult toxicity) {
    this.toxicity = toxicity;
    return this;
  }

  /**
   * Get toxicity
   * @return toxicity
   */
  @NotNull @Valid 
  @Schema(name = "toxicity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("toxicity")
  public ValidationCategoryResult getToxicity() {
    return toxicity;
  }

  public void setToxicity(ValidationCategoryResult toxicity) {
    this.toxicity = toxicity;
  }

  public ValidationChecklist budgetBounds(ValidationCategoryResult budgetBounds) {
    this.budgetBounds = budgetBounds;
    return this;
  }

  /**
   * Get budgetBounds
   * @return budgetBounds
   */
  @NotNull @Valid 
  @Schema(name = "budgetBounds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("budgetBounds")
  public ValidationCategoryResult getBudgetBounds() {
    return budgetBounds;
  }

  public void setBudgetBounds(ValidationCategoryResult budgetBounds) {
    this.budgetBounds = budgetBounds;
  }

  public ValidationChecklist duplicates(ValidationCategoryResult duplicates) {
    this.duplicates = duplicates;
    return this;
  }

  /**
   * Get duplicates
   * @return duplicates
   */
  @NotNull @Valid 
  @Schema(name = "duplicates", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duplicates")
  public ValidationCategoryResult getDuplicates() {
    return duplicates;
  }

  public void setDuplicates(ValidationCategoryResult duplicates) {
    this.duplicates = duplicates;
  }

  public ValidationChecklist summary(ValidationSummary summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @NotNull @Valid 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("summary")
  public ValidationSummary getSummary() {
    return summary;
  }

  public void setSummary(ValidationSummary summary) {
    this.summary = summary;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationChecklist validationChecklist = (ValidationChecklist) o;
    return Objects.equals(this.territory, validationChecklist.territory) &&
        Objects.equals(this.sanctions, validationChecklist.sanctions) &&
        Objects.equals(this.legal, validationChecklist.legal) &&
        Objects.equals(this.toxicity, validationChecklist.toxicity) &&
        Objects.equals(this.budgetBounds, validationChecklist.budgetBounds) &&
        Objects.equals(this.duplicates, validationChecklist.duplicates) &&
        Objects.equals(this.summary, validationChecklist.summary);
  }

  @Override
  public int hashCode() {
    return Objects.hash(territory, sanctions, legal, toxicity, budgetBounds, duplicates, summary);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationChecklist {\n");
    sb.append("    territory: ").append(toIndentedString(territory)).append("\n");
    sb.append("    sanctions: ").append(toIndentedString(sanctions)).append("\n");
    sb.append("    legal: ").append(toIndentedString(legal)).append("\n");
    sb.append("    toxicity: ").append(toIndentedString(toxicity)).append("\n");
    sb.append("    budgetBounds: ").append(toIndentedString(budgetBounds)).append("\n");
    sb.append("    duplicates: ").append(toIndentedString(duplicates)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
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

