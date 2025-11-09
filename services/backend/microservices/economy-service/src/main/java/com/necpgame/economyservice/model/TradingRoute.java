package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TradingRoute
 */


public class TradingRoute {

  private @Nullable String routeId;

  private @Nullable String name;

  private @Nullable String fromHub;

  private @Nullable String toHub;

  private @Nullable BigDecimal distance;

  private @Nullable BigDecimal deliveryTime;

  private @Nullable BigDecimal baseProfitMargin;

  /**
   * Gets or Sets riskLevel
   */
  public enum RiskLevelEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    EXTREME("extreme");

    private final String value;

    RiskLevelEnum(String value) {
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
    public static RiskLevelEnum fromValue(String value) {
      for (RiskLevelEnum b : RiskLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskLevelEnum riskLevel;

  private @Nullable Object requirements;

  public TradingRoute routeId(@Nullable String routeId) {
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

  public TradingRoute name(@Nullable String name) {
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

  public TradingRoute fromHub(@Nullable String fromHub) {
    this.fromHub = fromHub;
    return this;
  }

  /**
   * Get fromHub
   * @return fromHub
   */
  
  @Schema(name = "from_hub", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("from_hub")
  public @Nullable String getFromHub() {
    return fromHub;
  }

  public void setFromHub(@Nullable String fromHub) {
    this.fromHub = fromHub;
  }

  public TradingRoute toHub(@Nullable String toHub) {
    this.toHub = toHub;
    return this;
  }

  /**
   * Get toHub
   * @return toHub
   */
  
  @Schema(name = "to_hub", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("to_hub")
  public @Nullable String getToHub() {
    return toHub;
  }

  public void setToHub(@Nullable String toHub) {
    this.toHub = toHub;
  }

  public TradingRoute distance(@Nullable BigDecimal distance) {
    this.distance = distance;
    return this;
  }

  /**
   * Get distance
   * @return distance
   */
  @Valid 
  @Schema(name = "distance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distance")
  public @Nullable BigDecimal getDistance() {
    return distance;
  }

  public void setDistance(@Nullable BigDecimal distance) {
    this.distance = distance;
  }

  public TradingRoute deliveryTime(@Nullable BigDecimal deliveryTime) {
    this.deliveryTime = deliveryTime;
    return this;
  }

  /**
   * Время доставки (часы)
   * @return deliveryTime
   */
  @Valid 
  @Schema(name = "delivery_time", description = "Время доставки (часы)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delivery_time")
  public @Nullable BigDecimal getDeliveryTime() {
    return deliveryTime;
  }

  public void setDeliveryTime(@Nullable BigDecimal deliveryTime) {
    this.deliveryTime = deliveryTime;
  }

  public TradingRoute baseProfitMargin(@Nullable BigDecimal baseProfitMargin) {
    this.baseProfitMargin = baseProfitMargin;
    return this;
  }

  /**
   * Get baseProfitMargin
   * @return baseProfitMargin
   */
  @Valid 
  @Schema(name = "base_profit_margin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_profit_margin")
  public @Nullable BigDecimal getBaseProfitMargin() {
    return baseProfitMargin;
  }

  public void setBaseProfitMargin(@Nullable BigDecimal baseProfitMargin) {
    this.baseProfitMargin = baseProfitMargin;
  }

  public TradingRoute riskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
    return this;
  }

  /**
   * Get riskLevel
   * @return riskLevel
   */
  
  @Schema(name = "risk_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_level")
  public @Nullable RiskLevelEnum getRiskLevel() {
    return riskLevel;
  }

  public void setRiskLevel(@Nullable RiskLevelEnum riskLevel) {
    this.riskLevel = riskLevel;
  }

  public TradingRoute requirements(@Nullable Object requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable Object getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable Object requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingRoute tradingRoute = (TradingRoute) o;
    return Objects.equals(this.routeId, tradingRoute.routeId) &&
        Objects.equals(this.name, tradingRoute.name) &&
        Objects.equals(this.fromHub, tradingRoute.fromHub) &&
        Objects.equals(this.toHub, tradingRoute.toHub) &&
        Objects.equals(this.distance, tradingRoute.distance) &&
        Objects.equals(this.deliveryTime, tradingRoute.deliveryTime) &&
        Objects.equals(this.baseProfitMargin, tradingRoute.baseProfitMargin) &&
        Objects.equals(this.riskLevel, tradingRoute.riskLevel) &&
        Objects.equals(this.requirements, tradingRoute.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeId, name, fromHub, toHub, distance, deliveryTime, baseProfitMargin, riskLevel, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingRoute {\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    fromHub: ").append(toIndentedString(fromHub)).append("\n");
    sb.append("    toHub: ").append(toIndentedString(toHub)).append("\n");
    sb.append("    distance: ").append(toIndentedString(distance)).append("\n");
    sb.append("    deliveryTime: ").append(toIndentedString(deliveryTime)).append("\n");
    sb.append("    baseProfitMargin: ").append(toIndentedString(baseProfitMargin)).append("\n");
    sb.append("    riskLevel: ").append(toIndentedString(riskLevel)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

