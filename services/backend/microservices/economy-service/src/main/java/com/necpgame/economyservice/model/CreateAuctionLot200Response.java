package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CreateAuctionLot200Response
 */

@JsonTypeName("createAuctionLot_200_response")

public class CreateAuctionLot200Response {

  private @Nullable Boolean success;

  private @Nullable String lotId;

  private @Nullable BigDecimal listingFee;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public CreateAuctionLot200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public CreateAuctionLot200Response lotId(@Nullable String lotId) {
    this.lotId = lotId;
    return this;
  }

  /**
   * Get lotId
   * @return lotId
   */
  
  @Schema(name = "lot_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lot_id")
  public @Nullable String getLotId() {
    return lotId;
  }

  public void setLotId(@Nullable String lotId) {
    this.lotId = lotId;
  }

  public CreateAuctionLot200Response listingFee(@Nullable BigDecimal listingFee) {
    this.listingFee = listingFee;
    return this;
  }

  /**
   * Get listingFee
   * @return listingFee
   */
  @Valid 
  @Schema(name = "listing_fee", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("listing_fee")
  public @Nullable BigDecimal getListingFee() {
    return listingFee;
  }

  public void setListingFee(@Nullable BigDecimal listingFee) {
    this.listingFee = listingFee;
  }

  public CreateAuctionLot200Response expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateAuctionLot200Response createAuctionLot200Response = (CreateAuctionLot200Response) o;
    return Objects.equals(this.success, createAuctionLot200Response.success) &&
        Objects.equals(this.lotId, createAuctionLot200Response.lotId) &&
        Objects.equals(this.listingFee, createAuctionLot200Response.listingFee) &&
        Objects.equals(this.expiresAt, createAuctionLot200Response.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, lotId, listingFee, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAuctionLot200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    lotId: ").append(toIndentedString(lotId)).append("\n");
    sb.append("    listingFee: ").append(toIndentedString(listingFee)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

