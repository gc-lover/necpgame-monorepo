package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FuturesContract
 */


public class FuturesContract {

  private @Nullable String contractId;

  private @Nullable String underlyingAsset;

  /**
   * Gets or Sets contractType
   */
  public enum ContractTypeEnum {
    RESOURCE("resource"),
    
    COMMODITY("commodity"),
    
    INDEX("index");

    private final String value;

    ContractTypeEnum(String value) {
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
    public static ContractTypeEnum fromValue(String value) {
      for (ContractTypeEnum b : ContractTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ContractTypeEnum contractType;

  private @Nullable BigDecimal currentPrice;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expirationDate;

  private @Nullable Integer contractSize;

  private @Nullable BigDecimal marginRequirement;

  public FuturesContract contractId(@Nullable String contractId) {
    this.contractId = contractId;
    return this;
  }

  /**
   * Get contractId
   * @return contractId
   */
  
  @Schema(name = "contract_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_id")
  public @Nullable String getContractId() {
    return contractId;
  }

  public void setContractId(@Nullable String contractId) {
    this.contractId = contractId;
  }

  public FuturesContract underlyingAsset(@Nullable String underlyingAsset) {
    this.underlyingAsset = underlyingAsset;
    return this;
  }

  /**
   * Базовый актив
   * @return underlyingAsset
   */
  
  @Schema(name = "underlying_asset", description = "Базовый актив", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("underlying_asset")
  public @Nullable String getUnderlyingAsset() {
    return underlyingAsset;
  }

  public void setUnderlyingAsset(@Nullable String underlyingAsset) {
    this.underlyingAsset = underlyingAsset;
  }

  public FuturesContract contractType(@Nullable ContractTypeEnum contractType) {
    this.contractType = contractType;
    return this;
  }

  /**
   * Get contractType
   * @return contractType
   */
  
  @Schema(name = "contract_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_type")
  public @Nullable ContractTypeEnum getContractType() {
    return contractType;
  }

  public void setContractType(@Nullable ContractTypeEnum contractType) {
    this.contractType = contractType;
  }

  public FuturesContract currentPrice(@Nullable BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice
   * @return currentPrice
   */
  @Valid 
  @Schema(name = "current_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_price")
  public @Nullable BigDecimal getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(@Nullable BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
  }

  public FuturesContract expirationDate(@Nullable OffsetDateTime expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  /**
   * Get expirationDate
   * @return expirationDate
   */
  @Valid 
  @Schema(name = "expiration_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiration_date")
  public @Nullable OffsetDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(@Nullable OffsetDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public FuturesContract contractSize(@Nullable Integer contractSize) {
    this.contractSize = contractSize;
    return this;
  }

  /**
   * Размер контракта
   * @return contractSize
   */
  
  @Schema(name = "contract_size", description = "Размер контракта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_size")
  public @Nullable Integer getContractSize() {
    return contractSize;
  }

  public void setContractSize(@Nullable Integer contractSize) {
    this.contractSize = contractSize;
  }

  public FuturesContract marginRequirement(@Nullable BigDecimal marginRequirement) {
    this.marginRequirement = marginRequirement;
    return this;
  }

  /**
   * Требуемая маржа (%)
   * @return marginRequirement
   */
  @Valid 
  @Schema(name = "margin_requirement", description = "Требуемая маржа (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("margin_requirement")
  public @Nullable BigDecimal getMarginRequirement() {
    return marginRequirement;
  }

  public void setMarginRequirement(@Nullable BigDecimal marginRequirement) {
    this.marginRequirement = marginRequirement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FuturesContract futuresContract = (FuturesContract) o;
    return Objects.equals(this.contractId, futuresContract.contractId) &&
        Objects.equals(this.underlyingAsset, futuresContract.underlyingAsset) &&
        Objects.equals(this.contractType, futuresContract.contractType) &&
        Objects.equals(this.currentPrice, futuresContract.currentPrice) &&
        Objects.equals(this.expirationDate, futuresContract.expirationDate) &&
        Objects.equals(this.contractSize, futuresContract.contractSize) &&
        Objects.equals(this.marginRequirement, futuresContract.marginRequirement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contractId, underlyingAsset, contractType, currentPrice, expirationDate, contractSize, marginRequirement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FuturesContract {\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    underlyingAsset: ").append(toIndentedString(underlyingAsset)).append("\n");
    sb.append("    contractType: ").append(toIndentedString(contractType)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    expirationDate: ").append(toIndentedString(expirationDate)).append("\n");
    sb.append("    contractSize: ").append(toIndentedString(contractSize)).append("\n");
    sb.append("    marginRequirement: ").append(toIndentedString(marginRequirement)).append("\n");
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

