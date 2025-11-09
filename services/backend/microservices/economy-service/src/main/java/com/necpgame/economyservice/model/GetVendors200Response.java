package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Vendor;
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
 * GetVendors200Response
 */

@JsonTypeName("getVendors_200_response")

public class GetVendors200Response {

  @Valid
  private List<@Valid Vendor> vendors = new ArrayList<>();

  public GetVendors200Response vendors(List<@Valid Vendor> vendors) {
    this.vendors = vendors;
    return this;
  }

  public GetVendors200Response addVendorsItem(Vendor vendorsItem) {
    if (this.vendors == null) {
      this.vendors = new ArrayList<>();
    }
    this.vendors.add(vendorsItem);
    return this;
  }

  /**
   * Get vendors
   * @return vendors
   */
  @Valid 
  @Schema(name = "vendors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendors")
  public List<@Valid Vendor> getVendors() {
    return vendors;
  }

  public void setVendors(List<@Valid Vendor> vendors) {
    this.vendors = vendors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetVendors200Response getVendors200Response = (GetVendors200Response) o;
    return Objects.equals(this.vendors, getVendors200Response.vendors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vendors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetVendors200Response {\n");
    sb.append("    vendors: ").append(toIndentedString(vendors)).append("\n");
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

