package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.AuctionLot;
import com.necpgame.economyservice.model.PaginationMeta;
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
 * GetAuctionLots200Response
 */

@JsonTypeName("getAuctionLots_200_response")

public class GetAuctionLots200Response {

  @Valid
  private List<@Valid AuctionLot> lots = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public GetAuctionLots200Response lots(List<@Valid AuctionLot> lots) {
    this.lots = lots;
    return this;
  }

  public GetAuctionLots200Response addLotsItem(AuctionLot lotsItem) {
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

  public GetAuctionLots200Response pagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAuctionLots200Response getAuctionLots200Response = (GetAuctionLots200Response) o;
    return Objects.equals(this.lots, getAuctionLots200Response.lots) &&
        Objects.equals(this.pagination, getAuctionLots200Response.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lots, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAuctionLots200Response {\n");
    sb.append("    lots: ").append(toIndentedString(lots)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
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

