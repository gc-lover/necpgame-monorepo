package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CreateAuctionLotRequest
 */

@JsonTypeName("createAuctionLot_request")

public class CreateAuctionLotRequest {

  private String characterId;

  private String itemId;

  private Integer quantity = 1;

  private BigDecimal startingPrice;

  private @Nullable BigDecimal buyoutPrice;

  /**
   * Gets or Sets duration
   */
  public enum DurationEnum {
    _12H("12h"),
    
    _24H("24h"),
    
    _48H("48h");

    private final String value;

    DurationEnum(String value) {
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
    public static DurationEnum fromValue(String value) {
      for (DurationEnum b : DurationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DurationEnum duration;

  public CreateAuctionLotRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateAuctionLotRequest(String characterId, String itemId, BigDecimal startingPrice, DurationEnum duration) {
    this.characterId = characterId;
    this.itemId = itemId;
    this.startingPrice = startingPrice;
    this.duration = duration;
  }

  public CreateAuctionLotRequest characterId(String characterId) {
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

  public CreateAuctionLotRequest itemId(String itemId) {
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

  public CreateAuctionLotRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public CreateAuctionLotRequest startingPrice(BigDecimal startingPrice) {
    this.startingPrice = startingPrice;
    return this;
  }

  /**
   * Get startingPrice
   * @return startingPrice
   */
  @NotNull @Valid 
  @Schema(name = "starting_price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("starting_price")
  public BigDecimal getStartingPrice() {
    return startingPrice;
  }

  public void setStartingPrice(BigDecimal startingPrice) {
    this.startingPrice = startingPrice;
  }

  public CreateAuctionLotRequest buyoutPrice(@Nullable BigDecimal buyoutPrice) {
    this.buyoutPrice = buyoutPrice;
    return this;
  }

  /**
   * Опциональная цена мгновенного выкупа
   * @return buyoutPrice
   */
  @Valid 
  @Schema(name = "buyout_price", description = "Опциональная цена мгновенного выкупа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("buyout_price")
  public @Nullable BigDecimal getBuyoutPrice() {
    return buyoutPrice;
  }

  public void setBuyoutPrice(@Nullable BigDecimal buyoutPrice) {
    this.buyoutPrice = buyoutPrice;
  }

  public CreateAuctionLotRequest duration(DurationEnum duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Get duration
   * @return duration
   */
  @NotNull 
  @Schema(name = "duration", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration")
  public DurationEnum getDuration() {
    return duration;
  }

  public void setDuration(DurationEnum duration) {
    this.duration = duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateAuctionLotRequest createAuctionLotRequest = (CreateAuctionLotRequest) o;
    return Objects.equals(this.characterId, createAuctionLotRequest.characterId) &&
        Objects.equals(this.itemId, createAuctionLotRequest.itemId) &&
        Objects.equals(this.quantity, createAuctionLotRequest.quantity) &&
        Objects.equals(this.startingPrice, createAuctionLotRequest.startingPrice) &&
        Objects.equals(this.buyoutPrice, createAuctionLotRequest.buyoutPrice) &&
        Objects.equals(this.duration, createAuctionLotRequest.duration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, itemId, quantity, startingPrice, buyoutPrice, duration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAuctionLotRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    startingPrice: ").append(toIndentedString(startingPrice)).append("\n");
    sb.append("    buyoutPrice: ").append(toIndentedString(buyoutPrice)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
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

