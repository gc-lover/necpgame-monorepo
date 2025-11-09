package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AnalyticsErrorAllOfError;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AnalyticsError
 */


public class AnalyticsError {

  private AnalyticsErrorAllOfError error;

  public AnalyticsError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AnalyticsError(AnalyticsErrorAllOfError error) {
    this.error = error;
  }

  public AnalyticsError error(AnalyticsErrorAllOfError error) {
    this.error = error;
    return this;
  }

  /**
   * Get error
   * @return error
   */
  @NotNull @Valid 
  @Schema(name = "error", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("error")
  public AnalyticsErrorAllOfError getError() {
    return error;
  }

  public void setError(AnalyticsErrorAllOfError error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsError analyticsError = (AnalyticsError) o;
    return Objects.equals(this.error, analyticsError.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsError {\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

