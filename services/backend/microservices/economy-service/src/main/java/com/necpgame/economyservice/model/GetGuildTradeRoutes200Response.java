package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.TradeRoute;
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
 * GetGuildTradeRoutes200Response
 */

@JsonTypeName("getGuildTradeRoutes_200_response")

public class GetGuildTradeRoutes200Response {

  @Valid
  private List<@Valid TradeRoute> activeRoutes = new ArrayList<>();

  @Valid
  private List<@Valid TradeRoute> exclusiveRoutes = new ArrayList<>();

  public GetGuildTradeRoutes200Response activeRoutes(List<@Valid TradeRoute> activeRoutes) {
    this.activeRoutes = activeRoutes;
    return this;
  }

  public GetGuildTradeRoutes200Response addActiveRoutesItem(TradeRoute activeRoutesItem) {
    if (this.activeRoutes == null) {
      this.activeRoutes = new ArrayList<>();
    }
    this.activeRoutes.add(activeRoutesItem);
    return this;
  }

  /**
   * Get activeRoutes
   * @return activeRoutes
   */
  @Valid 
  @Schema(name = "active_routes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_routes")
  public List<@Valid TradeRoute> getActiveRoutes() {
    return activeRoutes;
  }

  public void setActiveRoutes(List<@Valid TradeRoute> activeRoutes) {
    this.activeRoutes = activeRoutes;
  }

  public GetGuildTradeRoutes200Response exclusiveRoutes(List<@Valid TradeRoute> exclusiveRoutes) {
    this.exclusiveRoutes = exclusiveRoutes;
    return this;
  }

  public GetGuildTradeRoutes200Response addExclusiveRoutesItem(TradeRoute exclusiveRoutesItem) {
    if (this.exclusiveRoutes == null) {
      this.exclusiveRoutes = new ArrayList<>();
    }
    this.exclusiveRoutes.add(exclusiveRoutesItem);
    return this;
  }

  /**
   * Эксклюзивные маршруты гильдии
   * @return exclusiveRoutes
   */
  @Valid 
  @Schema(name = "exclusive_routes", description = "Эксклюзивные маршруты гильдии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("exclusive_routes")
  public List<@Valid TradeRoute> getExclusiveRoutes() {
    return exclusiveRoutes;
  }

  public void setExclusiveRoutes(List<@Valid TradeRoute> exclusiveRoutes) {
    this.exclusiveRoutes = exclusiveRoutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetGuildTradeRoutes200Response getGuildTradeRoutes200Response = (GetGuildTradeRoutes200Response) o;
    return Objects.equals(this.activeRoutes, getGuildTradeRoutes200Response.activeRoutes) &&
        Objects.equals(this.exclusiveRoutes, getGuildTradeRoutes200Response.exclusiveRoutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeRoutes, exclusiveRoutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetGuildTradeRoutes200Response {\n");
    sb.append("    activeRoutes: ").append(toIndentedString(activeRoutes)).append("\n");
    sb.append("    exclusiveRoutes: ").append(toIndentedString(exclusiveRoutes)).append("\n");
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

