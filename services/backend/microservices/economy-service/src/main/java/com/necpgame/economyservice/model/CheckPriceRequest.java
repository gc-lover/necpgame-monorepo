package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CheckPriceRequest
 */

@JsonTypeName("checkPrice_request")

public class CheckPriceRequest {

  private String characterId;

  private String vendorId;

  private String itemId;

  /**
   * Gets or Sets tradeType
   */
  public enum TradeTypeEnum {
    BUY("buy"),
    
    SELL("sell");

    private final String value;

    TradeTypeEnum(String value) {
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
    public static TradeTypeEnum fromValue(String value) {
      for (TradeTypeEnum b : TradeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TradeTypeEnum tradeType;

  public CheckPriceRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CheckPriceRequest(String characterId, String vendorId, String itemId, TradeTypeEnum tradeType) {
    this.characterId = characterId;
    this.vendorId = vendorId;
    this.itemId = itemId;
    this.tradeType = tradeType;
  }

  public CheckPriceRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public CheckPriceRequest vendorId(String vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  /**
   * Get vendorId
   * @return vendorId
   */
  @NotNull 
  @Schema(name = "vendor_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("vendor_id")
  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public CheckPriceRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("item_id")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public CheckPriceRequest tradeType(TradeTypeEnum tradeType) {
    this.tradeType = tradeType;
    return this;
  }

  /**
   * Get tradeType
   * @return tradeType
   */
  @NotNull 
  @Schema(name = "trade_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trade_type")
  public TradeTypeEnum getTradeType() {
    return tradeType;
  }

  public void setTradeType(TradeTypeEnum tradeType) {
    this.tradeType = tradeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckPriceRequest checkPriceRequest = (CheckPriceRequest) o;
    return Objects.equals(this.characterId, checkPriceRequest.characterId) &&
        Objects.equals(this.vendorId, checkPriceRequest.vendorId) &&
        Objects.equals(this.itemId, checkPriceRequest.itemId) &&
        Objects.equals(this.tradeType, checkPriceRequest.tradeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, vendorId, itemId, tradeType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckPriceRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    tradeType: ").append(toIndentedString(tradeType)).append("\n");
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

