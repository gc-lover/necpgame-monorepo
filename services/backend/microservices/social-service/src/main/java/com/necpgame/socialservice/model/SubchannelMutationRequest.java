package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.SubchannelMutationRequestOperationsInner;
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
 * SubchannelMutationRequest
 */


public class SubchannelMutationRequest {

  @Valid
  private List<@Valid SubchannelMutationRequestOperationsInner> operations = new ArrayList<>();

  public SubchannelMutationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SubchannelMutationRequest(List<@Valid SubchannelMutationRequestOperationsInner> operations) {
    this.operations = operations;
  }

  public SubchannelMutationRequest operations(List<@Valid SubchannelMutationRequestOperationsInner> operations) {
    this.operations = operations;
    return this;
  }

  public SubchannelMutationRequest addOperationsItem(SubchannelMutationRequestOperationsInner operationsItem) {
    if (this.operations == null) {
      this.operations = new ArrayList<>();
    }
    this.operations.add(operationsItem);
    return this;
  }

  /**
   * Get operations
   * @return operations
   */
  @NotNull @Valid 
  @Schema(name = "operations", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operations")
  public List<@Valid SubchannelMutationRequestOperationsInner> getOperations() {
    return operations;
  }

  public void setOperations(List<@Valid SubchannelMutationRequestOperationsInner> operations) {
    this.operations = operations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubchannelMutationRequest subchannelMutationRequest = (SubchannelMutationRequest) o;
    return Objects.equals(this.operations, subchannelMutationRequest.operations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubchannelMutationRequest {\n");
    sb.append("    operations: ").append(toIndentedString(operations)).append("\n");
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

