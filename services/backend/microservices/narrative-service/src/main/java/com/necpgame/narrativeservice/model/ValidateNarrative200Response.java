package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ValidateNarrative200Response
 */

@JsonTypeName("validateNarrative_200_response")

public class ValidateNarrative200Response {

  private @Nullable Boolean valid;

  @Valid
  private List<String> errors = new ArrayList<>();

  @Valid
  private List<String> warnings = new ArrayList<>();

  public ValidateNarrative200Response valid(@Nullable Boolean valid) {
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

  public ValidateNarrative200Response errors(List<String> errors) {
    this.errors = errors;
    return this;
  }

  public ValidateNarrative200Response addErrorsItem(String errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Get errors
   * @return errors
   */
  
  @Schema(name = "errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  public ValidateNarrative200Response warnings(List<String> warnings) {
    this.warnings = warnings;
    return this;
  }

  public ValidateNarrative200Response addWarningsItem(String warningsItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidateNarrative200Response validateNarrative200Response = (ValidateNarrative200Response) o;
    return Objects.equals(this.valid, validateNarrative200Response.valid) &&
        Objects.equals(this.errors, validateNarrative200Response.errors) &&
        Objects.equals(this.warnings, validateNarrative200Response.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valid, errors, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidateNarrative200Response {\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

