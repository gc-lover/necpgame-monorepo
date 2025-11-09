package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * TradeRoute
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TradeRoute {

  private @Nullable String routeId;

  private @Nullable String name;

  private @Nullable String origin;

  private @Nullable String destination;

  @Valid
  private List<String> goods = new ArrayList<>();

  private @Nullable Float profitMargin;

  private @Nullable Boolean isExclusive;

  /**
   * Gets or Sets dangerLevel
   */
  public enum DangerLevelEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    EXTREME("EXTREME");

    private final String value;

    DangerLevelEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DangerLevelEnum fromValue(String value) {
      for (DangerLevelEnum b : DangerLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DangerLevelEnum dangerLevel;

  public TradeRoute routeId(@Nullable String routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  
  @Schema(name = "route_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("route_id")
  public @Nullable String getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable String routeId) {
    this.routeId = routeId;
  }

  public TradeRoute name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public TradeRoute origin(@Nullable String origin) {
    this.origin = origin;
    return this;
  }

  /**
   * Get origin
   * @return origin
   */
  
  @Schema(name = "origin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("origin")
  public @Nullable String getOrigin() {
    return origin;
  }

  public void setOrigin(@Nullable String origin) {
    this.origin = origin;
  }

  public TradeRoute destination(@Nullable String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get destination
   * @return destination
   */
  
  @Schema(name = "destination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("destination")
  public @Nullable String getDestination() {
    return destination;
  }

  public void setDestination(@Nullable String destination) {
    this.destination = destination;
  }

  public TradeRoute goods(List<String> goods) {
    this.goods = goods;
    return this;
  }

  public TradeRoute addGoodsItem(String goodsItem) {
    if (this.goods == null) {
      this.goods = new ArrayList<>();
    }
    this.goods.add(goodsItem);
    return this;
  }

  /**
   * Get goods
   * @return goods
   */
  
  @Schema(name = "goods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("goods")
  public List<String> getGoods() {
    return goods;
  }

  public void setGoods(List<String> goods) {
    this.goods = goods;
  }

  public TradeRoute profitMargin(@Nullable Float profitMargin) {
    this.profitMargin = profitMargin;
    return this;
  }

  /**
   * Get profitMargin
   * @return profitMargin
   */
  
  @Schema(name = "profit_margin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit_margin")
  public @Nullable Float getProfitMargin() {
    return profitMargin;
  }

  public void setProfitMargin(@Nullable Float profitMargin) {
    this.profitMargin = profitMargin;
  }

  public TradeRoute isExclusive(@Nullable Boolean isExclusive) {
    this.isExclusive = isExclusive;
    return this;
  }

  /**
   * Get isExclusive
   * @return isExclusive
   */
  
  @Schema(name = "is_exclusive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_exclusive")
  public @Nullable Boolean getIsExclusive() {
    return isExclusive;
  }

  public void setIsExclusive(@Nullable Boolean isExclusive) {
    this.isExclusive = isExclusive;
  }

  public TradeRoute dangerLevel(@Nullable DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
    return this;
  }

  /**
   * Get dangerLevel
   * @return dangerLevel
   */
  
  @Schema(name = "danger_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("danger_level")
  public @Nullable DangerLevelEnum getDangerLevel() {
    return dangerLevel;
  }

  public void setDangerLevel(@Nullable DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradeRoute tradeRoute = (TradeRoute) o;
    return Objects.equals(this.routeId, tradeRoute.routeId) &&
        Objects.equals(this.name, tradeRoute.name) &&
        Objects.equals(this.origin, tradeRoute.origin) &&
        Objects.equals(this.destination, tradeRoute.destination) &&
        Objects.equals(this.goods, tradeRoute.goods) &&
        Objects.equals(this.profitMargin, tradeRoute.profitMargin) &&
        Objects.equals(this.isExclusive, tradeRoute.isExclusive) &&
        Objects.equals(this.dangerLevel, tradeRoute.dangerLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, name, origin, destination, goods, profitMargin, isExclusive, dangerLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradeRoute {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    goods: ").append(toIndentedString(goods)).append("\n");
    sb.append("    profitMargin: ").append(toIndentedString(profitMargin)).append("\n");
    sb.append("    isExclusive: ").append(toIndentedString(isExclusive)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
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

