package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.Market;
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
 * GetMarkets200Response
 */

@JsonTypeName("getMarkets_200_response")

public class GetMarkets200Response {

  @Valid
  private List<@Valid Market> markets = new ArrayList<>();

  public GetMarkets200Response markets(List<@Valid Market> markets) {
    this.markets = markets;
    return this;
  }

  public GetMarkets200Response addMarketsItem(Market marketsItem) {
    if (this.markets == null) {
      this.markets = new ArrayList<>();
    }
    this.markets.add(marketsItem);
    return this;
  }

  /**
   * Get markets
   * @return markets
   */
  @Valid 
  @Schema(name = "markets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("markets")
  public List<@Valid Market> getMarkets() {
    return markets;
  }

  public void setMarkets(List<@Valid Market> markets) {
    this.markets = markets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMarkets200Response getMarkets200Response = (GetMarkets200Response) o;
    return Objects.equals(this.markets, getMarkets200Response.markets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(markets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMarkets200Response {\n");
    sb.append("    markets: ").append(toIndentedString(markets)).append("\n");
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

