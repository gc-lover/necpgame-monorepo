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
 * Market
 */


public class Market {

  private @Nullable String marketId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    NPC_VENDOR("npc_vendor"),
    
    AUCTION_HOUSE("auction_house"),
    
    PLAYER_MARKET("player_market"),
    
    BLACK_MARKET("black_market");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String region;

  private @Nullable BigDecimal reputationRequired;

  private @Nullable Boolean available;

  public Market marketId(@Nullable String marketId) {
    this.marketId = marketId;
    return this;
  }

  /**
   * Get marketId
   * @return marketId
   */
  
  @Schema(name = "market_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_id")
  public @Nullable String getMarketId() {
    return marketId;
  }

  public void setMarketId(@Nullable String marketId) {
    this.marketId = marketId;
  }

  public Market name(@Nullable String name) {
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

  public Market type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public Market region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public Market reputationRequired(@Nullable BigDecimal reputationRequired) {
    this.reputationRequired = reputationRequired;
    return this;
  }

  /**
   * Get reputationRequired
   * @return reputationRequired
   */
  @Valid 
  @Schema(name = "reputation_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_required")
  public @Nullable BigDecimal getReputationRequired() {
    return reputationRequired;
  }

  public void setReputationRequired(@Nullable BigDecimal reputationRequired) {
    this.reputationRequired = reputationRequired;
  }

  public Market available(@Nullable Boolean available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public @Nullable Boolean getAvailable() {
    return available;
  }

  public void setAvailable(@Nullable Boolean available) {
    this.available = available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Market market = (Market) o;
    return Objects.equals(this.marketId, market.marketId) &&
        Objects.equals(this.name, market.name) &&
        Objects.equals(this.type, market.type) &&
        Objects.equals(this.region, market.region) &&
        Objects.equals(this.reputationRequired, market.reputationRequired) &&
        Objects.equals(this.available, market.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(marketId, name, type, region, reputationRequired, available);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Market {\n");
    sb.append("    marketId: ").append(toIndentedString(marketId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    reputationRequired: ").append(toIndentedString(reputationRequired)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
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

