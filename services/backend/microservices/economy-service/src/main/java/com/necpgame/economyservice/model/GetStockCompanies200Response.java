package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.StockCompany;
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
 * GetStockCompanies200Response
 */

@JsonTypeName("getStockCompanies_200_response")

public class GetStockCompanies200Response {

  @Valid
  private List<@Valid StockCompany> companies = new ArrayList<>();

  public GetStockCompanies200Response companies(List<@Valid StockCompany> companies) {
    this.companies = companies;
    return this;
  }

  public GetStockCompanies200Response addCompaniesItem(StockCompany companiesItem) {
    if (this.companies == null) {
      this.companies = new ArrayList<>();
    }
    this.companies.add(companiesItem);
    return this;
  }

  /**
   * Get companies
   * @return companies
   */
  @Valid 
  @Schema(name = "companies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("companies")
  public List<@Valid StockCompany> getCompanies() {
    return companies;
  }

  public void setCompanies(List<@Valid StockCompany> companies) {
    this.companies = companies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStockCompanies200Response getStockCompanies200Response = (GetStockCompanies200Response) o;
    return Objects.equals(this.companies, getStockCompanies200Response.companies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(companies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStockCompanies200Response {\n");
    sb.append("    companies: ").append(toIndentedString(companies)).append("\n");
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

