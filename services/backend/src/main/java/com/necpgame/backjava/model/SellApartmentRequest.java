package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SellApartmentRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SellApartmentRequest {

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    NPC_BROKER("npc_broker"),
    
    MARKET_LISTING("market_listing");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode;

  private @Nullable Integer price;

  private @Nullable Boolean includeFurniture;

  private @Nullable String buyerPlayerId;

  public SellApartmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SellApartmentRequest(ModeEnum mode) {
    this.mode = mode;
  }

  public SellApartmentRequest mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public SellApartmentRequest price(@Nullable Integer price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
   */
  
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public @Nullable Integer getPrice() {
    return price;
  }

  public void setPrice(@Nullable Integer price) {
    this.price = price;
  }

  public SellApartmentRequest includeFurniture(@Nullable Boolean includeFurniture) {
    this.includeFurniture = includeFurniture;
    return this;
  }

  /**
   * Get includeFurniture
   * @return includeFurniture
   */
  
  @Schema(name = "includeFurniture", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeFurniture")
  public @Nullable Boolean getIncludeFurniture() {
    return includeFurniture;
  }

  public void setIncludeFurniture(@Nullable Boolean includeFurniture) {
    this.includeFurniture = includeFurniture;
  }

  public SellApartmentRequest buyerPlayerId(@Nullable String buyerPlayerId) {
    this.buyerPlayerId = buyerPlayerId;
    return this;
  }

  /**
   * Get buyerPlayerId
   * @return buyerPlayerId
   */
  
  @Schema(name = "buyerPlayerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buyerPlayerId")
  public @Nullable String getBuyerPlayerId() {
    return buyerPlayerId;
  }

  public void setBuyerPlayerId(@Nullable String buyerPlayerId) {
    this.buyerPlayerId = buyerPlayerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SellApartmentRequest sellApartmentRequest = (SellApartmentRequest) o;
    return Objects.equals(this.mode, sellApartmentRequest.mode) &&
        Objects.equals(this.price, sellApartmentRequest.price) &&
        Objects.equals(this.includeFurniture, sellApartmentRequest.includeFurniture) &&
        Objects.equals(this.buyerPlayerId, sellApartmentRequest.buyerPlayerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, price, includeFurniture, buyerPlayerId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SellApartmentRequest {\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    includeFurniture: ").append(toIndentedString(includeFurniture)).append("\n");
    sb.append("    buyerPlayerId: ").append(toIndentedString(buyerPlayerId)).append("\n");
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

