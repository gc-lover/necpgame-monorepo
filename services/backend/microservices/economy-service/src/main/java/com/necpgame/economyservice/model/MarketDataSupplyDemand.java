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
 * MarketDataSupplyDemand
 */

@JsonTypeName("MarketData_supply_demand")

public class MarketDataSupplyDemand {

  @Valid
  private List<String> highDemand = new ArrayList<>();

  @Valid
  private List<String> lowSupply = new ArrayList<>();

  public MarketDataSupplyDemand highDemand(List<String> highDemand) {
    this.highDemand = highDemand;
    return this;
  }

  public MarketDataSupplyDemand addHighDemandItem(String highDemandItem) {
    if (this.highDemand == null) {
      this.highDemand = new ArrayList<>();
    }
    this.highDemand.add(highDemandItem);
    return this;
  }

  /**
   * Get highDemand
   * @return highDemand
   */
  
  @Schema(name = "high_demand", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high_demand")
  public List<String> getHighDemand() {
    return highDemand;
  }

  public void setHighDemand(List<String> highDemand) {
    this.highDemand = highDemand;
  }

  public MarketDataSupplyDemand lowSupply(List<String> lowSupply) {
    this.lowSupply = lowSupply;
    return this;
  }

  public MarketDataSupplyDemand addLowSupplyItem(String lowSupplyItem) {
    if (this.lowSupply == null) {
      this.lowSupply = new ArrayList<>();
    }
    this.lowSupply.add(lowSupplyItem);
    return this;
  }

  /**
   * Get lowSupply
   * @return lowSupply
   */
  
  @Schema(name = "low_supply", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low_supply")
  public List<String> getLowSupply() {
    return lowSupply;
  }

  public void setLowSupply(List<String> lowSupply) {
    this.lowSupply = lowSupply;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketDataSupplyDemand marketDataSupplyDemand = (MarketDataSupplyDemand) o;
    return Objects.equals(this.highDemand, marketDataSupplyDemand.highDemand) &&
        Objects.equals(this.lowSupply, marketDataSupplyDemand.lowSupply);
  }

  @Override
  public int hashCode() {
    return Objects.hash(highDemand, lowSupply);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketDataSupplyDemand {\n");
    sb.append("    highDemand: ").append(toIndentedString(highDemand)).append("\n");
    sb.append("    lowSupply: ").append(toIndentedString(lowSupply)).append("\n");
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

