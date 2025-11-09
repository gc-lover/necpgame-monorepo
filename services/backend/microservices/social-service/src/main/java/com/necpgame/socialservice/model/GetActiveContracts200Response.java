package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.NPCContract;
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
  private List<@Valid NPCContract> contracts = new ArrayList<>();

  public GetActiveContracts200Response contracts(List<@Valid NPCContract> contracts) {
    this.contracts = contracts;
    return this;
  }

  public GetActiveContracts200Response addContractsItem(NPCContract contractsItem) {
    if (this.contracts == null) {
      this.contracts = new ArrayList<>();
    }
    this.contracts.add(contractsItem);
    return this;
  }

  /**
   * Get contracts
   * @return contracts
   */
  @Valid 
  @Schema(name = "contracts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contracts")
  public List<@Valid NPCContract> getContracts() {
    return contracts;
  }

  public void setContracts(List<@Valid NPCContract> contracts) {
    this.contracts = contracts;
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
    return Objects.equals(this.contracts, getActiveContracts200Response.contracts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contracts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveContracts200Response {\n");
    sb.append("    contracts: ").append(toIndentedString(contracts)).append("\n");
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

