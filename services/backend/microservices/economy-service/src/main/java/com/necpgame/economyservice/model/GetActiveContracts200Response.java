package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Contract;
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
 * GetActiveContracts200Response
 */

@JsonTypeName("getActiveContracts_200_response")

public class GetActiveContracts200Response {

  @Valid
  private List<@Valid Contract> asCreator = new ArrayList<>();

  @Valid
  private List<@Valid Contract> asExecutor = new ArrayList<>();

  public GetActiveContracts200Response asCreator(List<@Valid Contract> asCreator) {
    this.asCreator = asCreator;
    return this;
  }

  public GetActiveContracts200Response addAsCreatorItem(Contract asCreatorItem) {
    if (this.asCreator == null) {
      this.asCreator = new ArrayList<>();
    }
    this.asCreator.add(asCreatorItem);
    return this;
  }

  /**
   * Get asCreator
   * @return asCreator
   */
  @Valid 
  @Schema(name = "as_creator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("as_creator")
  public List<@Valid Contract> getAsCreator() {
    return asCreator;
  }

  public void setAsCreator(List<@Valid Contract> asCreator) {
    this.asCreator = asCreator;
  }

  public GetActiveContracts200Response asExecutor(List<@Valid Contract> asExecutor) {
    this.asExecutor = asExecutor;
    return this;
  }

  public GetActiveContracts200Response addAsExecutorItem(Contract asExecutorItem) {
    if (this.asExecutor == null) {
      this.asExecutor = new ArrayList<>();
    }
    this.asExecutor.add(asExecutorItem);
    return this;
  }

  /**
   * Get asExecutor
   * @return asExecutor
   */
  @Valid 
  @Schema(name = "as_executor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("as_executor")
  public List<@Valid Contract> getAsExecutor() {
    return asExecutor;
  }

  public void setAsExecutor(List<@Valid Contract> asExecutor) {
    this.asExecutor = asExecutor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveContracts200Response getActiveContracts200Response = (GetActiveContracts200Response) o;
    return Objects.equals(this.asCreator, getActiveContracts200Response.asCreator) &&
        Objects.equals(this.asExecutor, getActiveContracts200Response.asExecutor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(asCreator, asExecutor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveContracts200Response {\n");
    sb.append("    asCreator: ").append(toIndentedString(asCreator)).append("\n");
    sb.append("    asExecutor: ").append(toIndentedString(asExecutor)).append("\n");
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

