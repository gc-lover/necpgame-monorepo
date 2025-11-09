package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CoherenceValidationViolationsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CoherenceValidation
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CoherenceValidation {

  private @Nullable Boolean valid;

  @Valid
  private List<@Valid CoherenceValidationViolationsInner> violations = new ArrayList<>();

  @Valid
  private List<String> warnings = new ArrayList<>();

  private @Nullable Boolean canProceed;

  public CoherenceValidation valid(@Nullable Boolean valid) {
    this.valid = valid;
    return this;
  }

  /**
   * Get valid
   * @return valid
   */
  
  @Schema(name = "valid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valid")
  public @Nullable Boolean getValid() {
    return valid;
  }

  public void setValid(@Nullable Boolean valid) {
    this.valid = valid;
  }

  public CoherenceValidation violations(List<@Valid CoherenceValidationViolationsInner> violations) {
    this.violations = violations;
    return this;
  }

  public CoherenceValidation addViolationsItem(CoherenceValidationViolationsInner violationsItem) {
    if (this.violations == null) {
      this.violations = new ArrayList<>();
    }
    this.violations.add(violationsItem);
    return this;
  }

  /**
   * Get violations
   * @return violations
   */
  @Valid 
  @Schema(name = "violations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violations")
  public List<@Valid CoherenceValidationViolationsInner> getViolations() {
    return violations;
  }

  public void setViolations(List<@Valid CoherenceValidationViolationsInner> violations) {
    this.violations = violations;
  }

  public CoherenceValidation warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public CoherenceValidation addWarningsItem(String warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

  public CoherenceValidation canProceed(@Nullable Boolean canProceed) {
    this.canProceed = canProceed;
    return this;
  }

  /**
   * Get canProceed
   * @return canProceed
   */
  
  @Schema(name = "can_proceed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("can_proceed")
  public @Nullable Boolean getCanProceed() {
    return canProceed;
  }

  public void setCanProceed(@Nullable Boolean canProceed) {
    this.canProceed = canProceed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CoherenceValidation coherenceValidation = (CoherenceValidation) o;
    return Objects.equals(this.valid, coherenceValidation.valid) &&
        Objects.equals(this.violations, coherenceValidation.violations) &&
        Objects.equals(this.warnings, coherenceValidation.warnings) &&
        Objects.equals(this.canProceed, coherenceValidation.canProceed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valid, violations, warnings, canProceed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CoherenceValidation {\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    violations: ").append(toIndentedString(violations)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    canProceed: ").append(toIndentedString(canProceed)).append("\n");
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

