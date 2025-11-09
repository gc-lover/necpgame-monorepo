package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.ArbitrageOpportunity;
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
 * FindArbitrageOpportunities200Response
 */

@JsonTypeName("findArbitrageOpportunities_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class FindArbitrageOpportunities200Response {

  @Valid
  private List<@Valid ArbitrageOpportunity> opportunities = new ArrayList<>();

  public FindArbitrageOpportunities200Response opportunities(List<@Valid ArbitrageOpportunity> opportunities) {
    this.opportunities = opportunities;
    return this;
  }

  public FindArbitrageOpportunities200Response addOpportunitiesItem(ArbitrageOpportunity opportunitiesItem) {
    if (this.opportunities == null) {
      this.opportunities = new ArrayList<>();
    }
    this.opportunities.add(opportunitiesItem);
    return this;
  }

  /**
   * Get opportunities
   * @return opportunities
   */
  @Valid 
  @Schema(name = "opportunities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opportunities")
  public List<@Valid ArbitrageOpportunity> getOpportunities() {
    return opportunities;
  }

  public void setOpportunities(List<@Valid ArbitrageOpportunity> opportunities) {
    this.opportunities = opportunities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FindArbitrageOpportunities200Response findArbitrageOpportunities200Response = (FindArbitrageOpportunities200Response) o;
    return Objects.equals(this.opportunities, findArbitrageOpportunities200Response.opportunities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opportunities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindArbitrageOpportunities200Response {\n");
    sb.append("    opportunities: ").append(toIndentedString(opportunities)).append("\n");
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

