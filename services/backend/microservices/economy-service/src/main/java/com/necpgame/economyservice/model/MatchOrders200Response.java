package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.MatchOrders200ResponseMatchedTradesInner;
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
 * MatchOrders200Response
 */

@JsonTypeName("matchOrders_200_response")

public class MatchOrders200Response {

  @Valid
  private List<@Valid MatchOrders200ResponseMatchedTradesInner> matchedTrades = new ArrayList<>();

  public MatchOrders200Response matchedTrades(List<@Valid MatchOrders200ResponseMatchedTradesInner> matchedTrades) {
    this.matchedTrades = matchedTrades;
    return this;
  }

  public MatchOrders200Response addMatchedTradesItem(MatchOrders200ResponseMatchedTradesInner matchedTradesItem) {
    if (this.matchedTrades == null) {
      this.matchedTrades = new ArrayList<>();
    }
    this.matchedTrades.add(matchedTradesItem);
    return this;
  }

  /**
   * Get matchedTrades
   * @return matchedTrades
   */
  @Valid 
  @Schema(name = "matched_trades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matched_trades")
  public List<@Valid MatchOrders200ResponseMatchedTradesInner> getMatchedTrades() {
    return matchedTrades;
  }

  public void setMatchedTrades(List<@Valid MatchOrders200ResponseMatchedTradesInner> matchedTrades) {
    this.matchedTrades = matchedTrades;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchOrders200Response matchOrders200Response = (MatchOrders200Response) o;
    return Objects.equals(this.matchedTrades, matchOrders200Response.matchedTrades);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matchedTrades);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchOrders200Response {\n");
    sb.append("    matchedTrades: ").append(toIndentedString(matchedTrades)).append("\n");
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

