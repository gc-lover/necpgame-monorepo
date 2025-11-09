package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.StoredItem;
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
 * SellApartmentResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SellApartmentResponse {

  private @Nullable String apartmentId;

  private @Nullable Integer salePrice;

  private @Nullable Integer taxesPaid;

  @Valid
  private List<@Valid StoredItem> storageReturned = new ArrayList<>();

  public SellApartmentResponse apartmentId(@Nullable String apartmentId) {
    this.apartmentId = apartmentId;
    return this;
  }

  /**
   * Get apartmentId
   * @return apartmentId
   */
  
  @Schema(name = "apartmentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("apartmentId")
  public @Nullable String getApartmentId() {
    return apartmentId;
  }

  public void setApartmentId(@Nullable String apartmentId) {
    this.apartmentId = apartmentId;
  }

  public SellApartmentResponse salePrice(@Nullable Integer salePrice) {
    this.salePrice = salePrice;
    return this;
  }

  /**
   * Get salePrice
   * @return salePrice
   */
  
  @Schema(name = "salePrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("salePrice")
  public @Nullable Integer getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(@Nullable Integer salePrice) {
    this.salePrice = salePrice;
  }

  public SellApartmentResponse taxesPaid(@Nullable Integer taxesPaid) {
    this.taxesPaid = taxesPaid;
    return this;
  }

  /**
   * Get taxesPaid
   * @return taxesPaid
   */
  
  @Schema(name = "taxesPaid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("taxesPaid")
  public @Nullable Integer getTaxesPaid() {
    return taxesPaid;
  }

  public void setTaxesPaid(@Nullable Integer taxesPaid) {
    this.taxesPaid = taxesPaid;
  }

  public SellApartmentResponse storageReturned(List<@Valid StoredItem> storageReturned) {
    this.storageReturned = storageReturned;
    return this;
  }

  public SellApartmentResponse addStorageReturnedItem(StoredItem storageReturnedItem) {
    if (this.storageReturned == null) {
      this.storageReturned = new ArrayList<>();
    }
    this.storageReturned.add(storageReturnedItem);
    return this;
  }

  /**
   * Get storageReturned
   * @return storageReturned
   */
  @Valid 
  @Schema(name = "storageReturned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("storageReturned")
  public List<@Valid StoredItem> getStorageReturned() {
    return storageReturned;
  }

  public void setStorageReturned(List<@Valid StoredItem> storageReturned) {
    this.storageReturned = storageReturned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SellApartmentResponse sellApartmentResponse = (SellApartmentResponse) o;
    return Objects.equals(this.apartmentId, sellApartmentResponse.apartmentId) &&
        Objects.equals(this.salePrice, sellApartmentResponse.salePrice) &&
        Objects.equals(this.taxesPaid, sellApartmentResponse.taxesPaid) &&
        Objects.equals(this.storageReturned, sellApartmentResponse.storageReturned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(apartmentId, salePrice, taxesPaid, storageReturned);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SellApartmentResponse {\n");
    sb.append("    apartmentId: ").append(toIndentedString(apartmentId)).append("\n");
    sb.append("    salePrice: ").append(toIndentedString(salePrice)).append("\n");
    sb.append("    taxesPaid: ").append(toIndentedString(taxesPaid)).append("\n");
    sb.append("    storageReturned: ").append(toIndentedString(storageReturned)).append("\n");
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

