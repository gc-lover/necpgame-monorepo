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
 * SearchAuctionLots200Response
 */

@JsonTypeName("searchAuctionLots_200_response")

public class SearchAuctionLots200Response {

  @Valid
  private List<@Valid AuctionLot> lots = new ArrayList<>();

  private @Nullable Integer total;

  private @Nullable PaginationMeta pagination;

  public SearchAuctionLots200Response lots(List<@Valid AuctionLot> lots) {
    this.lots = lots;
    return this;
  }

  public SearchAuctionLots200Response addLotsItem(AuctionLot lotsItem) {
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

  public SearchAuctionLots200Response total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public SearchAuctionLots200Response pagination(@Nullable PaginationMeta pagination) {
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
    SearchAuctionLots200Response searchAuctionLots200Response = (SearchAuctionLots200Response) o;
    return Objects.equals(this.lots, searchAuctionLots200Response.lots) &&
        Objects.equals(this.total, searchAuctionLots200Response.total) &&
        Objects.equals(this.pagination, searchAuctionLots200Response.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lots, total, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchAuctionLots200Response {\n");
    sb.append("    lots: ").append(toIndentedString(lots)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
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

