package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TradingGuildDetailedAllOfUpgrades
 */

@JsonTypeName("TradingGuildDetailed_allOf_upgrades")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TradingGuildDetailedAllOfUpgrades {

  private @Nullable Integer guildHallLevel;

  private @Nullable Integer warehouseCapacity;

  private @Nullable Integer tradeOfficeCount;

  public TradingGuildDetailedAllOfUpgrades guildHallLevel(@Nullable Integer guildHallLevel) {
    this.guildHallLevel = guildHallLevel;
    return this;
  }

  /**
   * Get guildHallLevel
   * @return guildHallLevel
   */
  
  @Schema(name = "guild_hall_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guild_hall_level")
  public @Nullable Integer getGuildHallLevel() {
    return guildHallLevel;
  }

  public void setGuildHallLevel(@Nullable Integer guildHallLevel) {
    this.guildHallLevel = guildHallLevel;
  }

  public TradingGuildDetailedAllOfUpgrades warehouseCapacity(@Nullable Integer warehouseCapacity) {
    this.warehouseCapacity = warehouseCapacity;
    return this;
  }

  /**
   * Get warehouseCapacity
   * @return warehouseCapacity
   */
  
  @Schema(name = "warehouse_capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warehouse_capacity")
  public @Nullable Integer getWarehouseCapacity() {
    return warehouseCapacity;
  }

  public void setWarehouseCapacity(@Nullable Integer warehouseCapacity) {
    this.warehouseCapacity = warehouseCapacity;
  }

  public TradingGuildDetailedAllOfUpgrades tradeOfficeCount(@Nullable Integer tradeOfficeCount) {
    this.tradeOfficeCount = tradeOfficeCount;
    return this;
  }

  /**
   * Get tradeOfficeCount
   * @return tradeOfficeCount
   */
  
  @Schema(name = "trade_office_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trade_office_count")
  public @Nullable Integer getTradeOfficeCount() {
    return tradeOfficeCount;
  }

  public void setTradeOfficeCount(@Nullable Integer tradeOfficeCount) {
    this.tradeOfficeCount = tradeOfficeCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingGuildDetailedAllOfUpgrades tradingGuildDetailedAllOfUpgrades = (TradingGuildDetailedAllOfUpgrades) o;
    return Objects.equals(this.guildHallLevel, tradingGuildDetailedAllOfUpgrades.guildHallLevel) &&
        Objects.equals(this.warehouseCapacity, tradingGuildDetailedAllOfUpgrades.warehouseCapacity) &&
        Objects.equals(this.tradeOfficeCount, tradingGuildDetailedAllOfUpgrades.tradeOfficeCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildHallLevel, warehouseCapacity, tradeOfficeCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingGuildDetailedAllOfUpgrades {\n");
    sb.append("    guildHallLevel: ").append(toIndentedString(guildHallLevel)).append("\n");
    sb.append("    warehouseCapacity: ").append(toIndentedString(warehouseCapacity)).append("\n");
    sb.append("    tradeOfficeCount: ").append(toIndentedString(tradeOfficeCount)).append("\n");
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

