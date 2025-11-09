package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.ProductionChain;
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
 * GetProductionChains200Response
 */

@JsonTypeName("getProductionChains_200_response")

public class GetProductionChains200Response {

  @Valid
  private List<@Valid ProductionChain> chains = new ArrayList<>();

  public GetProductionChains200Response chains(List<@Valid ProductionChain> chains) {
    this.chains = chains;
    return this;
  }

  public GetProductionChains200Response addChainsItem(ProductionChain chainsItem) {
    if (this.chains == null) {
      this.chains = new ArrayList<>();
    }
    this.chains.add(chainsItem);
    return this;
  }

  /**
   * Get chains
   * @return chains
   */
  @Valid 
  @Schema(name = "chains", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chains")
  public List<@Valid ProductionChain> getChains() {
    return chains;
  }

  public void setChains(List<@Valid ProductionChain> chains) {
    this.chains = chains;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetProductionChains200Response getProductionChains200Response = (GetProductionChains200Response) o;
    return Objects.equals(this.chains, getProductionChains200Response.chains);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chains);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetProductionChains200Response {\n");
    sb.append("    chains: ").append(toIndentedString(chains)).append("\n");
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

