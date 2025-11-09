package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.InvestmentFund;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * GetInvestmentFunds200Response
 */

@JsonTypeName("getInvestmentFunds_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetInvestmentFunds200Response {

  @Valid
  private List<@Valid InvestmentFund> funds = new ArrayList<>();

  public GetInvestmentFunds200Response funds(List<@Valid InvestmentFund> funds) {
    this.funds = funds;
    return this;
  }

  public GetInvestmentFunds200Response addFundsItem(InvestmentFund fundsItem) {
    if (this.funds == null) {
      this.funds = new ArrayList<>();
    }
    this.funds.add(fundsItem);
    return this;
  }

  /**
   * Get funds
   * @return funds
   */
  @Valid 
  @Schema(name = "funds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("funds")
  public List<@Valid InvestmentFund> getFunds() {
    return funds;
  }

  public void setFunds(List<@Valid InvestmentFund> funds) {
    this.funds = funds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetInvestmentFunds200Response getInvestmentFunds200Response = (GetInvestmentFunds200Response) o;
    return Objects.equals(this.funds, getInvestmentFunds200Response.funds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(funds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetInvestmentFunds200Response {\n");
    sb.append("    funds: ").append(toIndentedString(funds)).append("\n");
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

