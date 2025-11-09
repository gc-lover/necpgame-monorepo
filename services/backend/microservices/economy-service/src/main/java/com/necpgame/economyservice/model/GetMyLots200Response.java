package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.AuctionLot;
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
 * GetMyLots200Response
 */

@JsonTypeName("getMyLots_200_response")

public class GetMyLots200Response {

  @Valid
  private List<@Valid AuctionLot> lots = new ArrayList<>();

  public GetMyLots200Response lots(List<@Valid AuctionLot> lots) {
    this.lots = lots;
    return this;
  }

  public GetMyLots200Response addLotsItem(AuctionLot lotsItem) {
    if (this.lots == null) {
      this.lots = new ArrayList<>();
    }
    this.lots.add(lotsItem);
    return this;
  }

  /**
   * Get lots
   * @return lots
   */
  @Valid 
  @Schema(name = "lots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lots")
  public List<@Valid AuctionLot> getLots() {
    return lots;
  }

  public void setLots(List<@Valid AuctionLot> lots) {
    this.lots = lots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMyLots200Response getMyLots200Response = (GetMyLots200Response) o;
    return Objects.equals(this.lots, getMyLots200Response.lots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMyLots200Response {\n");
    sb.append("    lots: ").append(toIndentedString(lots)).append("\n");
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

