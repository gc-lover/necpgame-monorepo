package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.ErrorGroup;
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
 * GetErrors200Response
 */

@JsonTypeName("getErrors_200_response")

public class GetErrors200Response {

  private @Nullable Integer totalErrors;

  @Valid
  private List<@Valid ErrorGroup> errorGroups = new ArrayList<>();

  public GetErrors200Response totalErrors(@Nullable Integer totalErrors) {
    this.totalErrors = totalErrors;
    return this;
  }

  /**
   * Get totalErrors
   * @return totalErrors
   */
  
  @Schema(name = "total_errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_errors")
  public @Nullable Integer getTotalErrors() {
    return totalErrors;
  }

  public void setTotalErrors(@Nullable Integer totalErrors) {
    this.totalErrors = totalErrors;
  }

  public GetErrors200Response errorGroups(List<@Valid ErrorGroup> errorGroups) {
    this.errorGroups = errorGroups;
    return this;
  }

  public GetErrors200Response addErrorGroupsItem(ErrorGroup errorGroupsItem) {
    if (this.errorGroups == null) {
      this.errorGroups = new ArrayList<>();
    }
    this.errorGroups.add(errorGroupsItem);
    return this;
  }

  /**
   * Get errorGroups
   * @return errorGroups
   */
  @Valid 
  @Schema(name = "error_groups", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error_groups")
  public List<@Valid ErrorGroup> getErrorGroups() {
    return errorGroups;
  }

  public void setErrorGroups(List<@Valid ErrorGroup> errorGroups) {
    this.errorGroups = errorGroups;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetErrors200Response getErrors200Response = (GetErrors200Response) o;
    return Objects.equals(this.totalErrors, getErrors200Response.totalErrors) &&
        Objects.equals(this.errorGroups, getErrors200Response.errorGroups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalErrors, errorGroups);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetErrors200Response {\n");
    sb.append("    totalErrors: ").append(toIndentedString(totalErrors)).append("\n");
    sb.append("    errorGroups: ").append(toIndentedString(errorGroups)).append("\n");
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

