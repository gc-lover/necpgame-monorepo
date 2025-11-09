package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.ProductionFacility;
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
 * GetProductionFacilities200Response
 */

@JsonTypeName("getProductionFacilities_200_response")

public class GetProductionFacilities200Response {

  @Valid
  private List<@Valid ProductionFacility> facilities = new ArrayList<>();

  public GetProductionFacilities200Response facilities(List<@Valid ProductionFacility> facilities) {
    this.facilities = facilities;
    return this;
  }

  public GetProductionFacilities200Response addFacilitiesItem(ProductionFacility facilitiesItem) {
    if (this.facilities == null) {
      this.facilities = new ArrayList<>();
    }
    this.facilities.add(facilitiesItem);
    return this;
  }

  /**
   * Get facilities
   * @return facilities
   */
  @Valid 
  @Schema(name = "facilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("facilities")
  public List<@Valid ProductionFacility> getFacilities() {
    return facilities;
  }

  public void setFacilities(List<@Valid ProductionFacility> facilities) {
    this.facilities = facilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetProductionFacilities200Response getProductionFacilities200Response = (GetProductionFacilities200Response) o;
    return Objects.equals(this.facilities, getProductionFacilities200Response.facilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(facilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetProductionFacilities200Response {\n");
    sb.append("    facilities: ").append(toIndentedString(facilities)).append("\n");
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

