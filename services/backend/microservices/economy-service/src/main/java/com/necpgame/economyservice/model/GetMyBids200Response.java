package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetMyBids200Response
 */

@JsonTypeName("getMyBids_200_response")

public class GetMyBids200Response {

  @Valid
  private List<Object> bids = new ArrayList<>();

  public GetMyBids200Response bids(List<Object> bids) {
    this.bids = bids;
    return this;
  }

  public GetMyBids200Response addBidsItem(Object bidsItem) {
    if (this.bids == null) {
      this.bids = new ArrayList<>();
    }
    this.bids.add(bidsItem);
    return this;
  }

  /**
   * Get bids
   * @return bids
   */
  
  @Schema(name = "bids", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bids")
  public List<Object> getBids() {
    return bids;
  }

  public void setBids(List<Object> bids) {
    this.bids = bids;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMyBids200Response getMyBids200Response = (GetMyBids200Response) o;
    return Objects.equals(this.bids, getMyBids200Response.bids);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bids);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMyBids200Response {\n");
    sb.append("    bids: ").append(toIndentedString(bids)).append("\n");
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

