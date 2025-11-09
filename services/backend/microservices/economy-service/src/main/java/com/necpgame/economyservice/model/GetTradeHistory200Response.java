package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetTradeHistory200ResponseStatistics;
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
 * GetTradeHistory200Response
 */

@JsonTypeName("getTradeHistory_200_response")

public class GetTradeHistory200Response {

  private @Nullable String characterId;

  private @Nullable GetTradeHistory200ResponseStatistics statistics;

  @Valid
  private List<Object> trades = new ArrayList<>();

  public GetTradeHistory200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GetTradeHistory200Response statistics(@Nullable GetTradeHistory200ResponseStatistics statistics) {
    this.statistics = statistics;
    return this;
  }

  /**
   * Get statistics
   * @return statistics
   */
  @Valid 
  @Schema(name = "statistics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("statistics")
  public @Nullable GetTradeHistory200ResponseStatistics getStatistics() {
    return statistics;
  }

  public void setStatistics(@Nullable GetTradeHistory200ResponseStatistics statistics) {
    this.statistics = statistics;
  }

  public GetTradeHistory200Response trades(List<Object> trades) {
    this.trades = trades;
    return this;
  }

  public GetTradeHistory200Response addTradesItem(Object tradesItem) {
    if (this.trades == null) {
      this.trades = new ArrayList<>();
    }
    this.trades.add(tradesItem);
    return this;
  }

  /**
   * Get trades
   * @return trades
   */
  
  @Schema(name = "trades", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trades")
  public List<Object> getTrades() {
    return trades;
  }

  public void setTrades(List<Object> trades) {
    this.trades = trades;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTradeHistory200Response getTradeHistory200Response = (GetTradeHistory200Response) o;
    return Objects.equals(this.characterId, getTradeHistory200Response.characterId) &&
        Objects.equals(this.statistics, getTradeHistory200Response.statistics) &&
        Objects.equals(this.trades, getTradeHistory200Response.trades);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, statistics, trades);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTradeHistory200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    statistics: ").append(toIndentedString(statistics)).append("\n");
    sb.append("    trades: ").append(toIndentedString(trades)).append("\n");
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

