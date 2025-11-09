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
 * BuyFuturesContractRequest
 */

@JsonTypeName("buyFuturesContract_request")

public class BuyFuturesContractRequest {

  private String characterId;

  private String contractId;

  private Integer quantity;

  /**
   * Long = ставка на рост, Short = ставка на падение
   */
  public enum SideEnum {
    LONG("long"),
    
    SHORT("short");

    private final String value;

    SideEnum(String value) {
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
    public static SideEnum fromValue(String value) {
      for (SideEnum b : SideEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SideEnum side;

  public BuyFuturesContractRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BuyFuturesContractRequest(String characterId, String contractId, Integer quantity, SideEnum side) {
    this.characterId = characterId;
    this.contractId = contractId;
    this.quantity = quantity;
    this.side = side;
  }

  public BuyFuturesContractRequest characterId(String characterId) {
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

  public BuyFuturesContractRequest contractId(String contractId) {
    this.contractId = contractId;
    return this;
  }

  /**
   * Get contractId
   * @return contractId
   */
  @NotNull 
  @Schema(name = "contract_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contract_id")
  public String getContractId() {
    return contractId;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public BuyFuturesContractRequest quantity(Integer quantity) {
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

  public BuyFuturesContractRequest side(SideEnum side) {
    this.side = side;
    return this;
  }

  /**
   * Long = ставка на рост, Short = ставка на падение
   * @return side
   */
  @NotNull 
  @Schema(name = "side", description = "Long = ставка на рост, Short = ставка на падение", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("side")
  public SideEnum getSide() {
    return side;
  }

  public void setSide(SideEnum side) {
    this.side = side;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyFuturesContractRequest buyFuturesContractRequest = (BuyFuturesContractRequest) o;
    return Objects.equals(this.characterId, buyFuturesContractRequest.characterId) &&
        Objects.equals(this.contractId, buyFuturesContractRequest.contractId) &&
        Objects.equals(this.quantity, buyFuturesContractRequest.quantity) &&
        Objects.equals(this.side, buyFuturesContractRequest.side);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, contractId, quantity, side);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyFuturesContractRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    side: ").append(toIndentedString(side)).append("\n");
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

