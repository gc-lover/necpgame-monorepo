package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ShortStockRequest
 */

@JsonTypeName("shortStock_request")

public class ShortStockRequest {

  private String characterId;

  private String ticker;

  private Integer quantity;

  private @Nullable BigDecimal stopLoss;

  public ShortStockRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ShortStockRequest(String characterId, String ticker, Integer quantity) {
    this.characterId = characterId;
    this.ticker = ticker;
    this.quantity = quantity;
  }

  public ShortStockRequest characterId(String characterId) {
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

  public ShortStockRequest ticker(String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  @NotNull 
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public ShortStockRequest quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ShortStockRequest stopLoss(@Nullable BigDecimal stopLoss) {
    this.stopLoss = stopLoss;
    return this;
  }

  /**
   * Автоматическое закрытие при достижении цены
   * @return stopLoss
   */
  @Valid 
  @Schema(name = "stop_loss", description = "Автоматическое закрытие при достижении цены", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stop_loss")
  public @Nullable BigDecimal getStopLoss() {
    return stopLoss;
  }

  public void setStopLoss(@Nullable BigDecimal stopLoss) {
    this.stopLoss = stopLoss;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShortStockRequest shortStockRequest = (ShortStockRequest) o;
    return Objects.equals(this.characterId, shortStockRequest.characterId) &&
        Objects.equals(this.ticker, shortStockRequest.ticker) &&
        Objects.equals(this.quantity, shortStockRequest.quantity) &&
        Objects.equals(this.stopLoss, shortStockRequest.stopLoss);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, ticker, quantity, stopLoss);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShortStockRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    stopLoss: ").append(toIndentedString(stopLoss)).append("\n");
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

