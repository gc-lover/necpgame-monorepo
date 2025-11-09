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
 * BidOnLot200Response
 */

@JsonTypeName("bidOnLot_200_response")

public class BidOnLot200Response {

  private @Nullable Boolean success;

  private @Nullable String lotId;

  private @Nullable BigDecimal bidAmount;

  private @Nullable Boolean isWinning;

  public BidOnLot200Response success(@Nullable Boolean success) {
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

  public BidOnLot200Response lotId(@Nullable String lotId) {
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

  public BidOnLot200Response bidAmount(@Nullable BigDecimal bidAmount) {
    this.bidAmount = bidAmount;
    return this;
  }

  /**
   * Get bidAmount
   * @return bidAmount
   */
  @Valid 
  @Schema(name = "bid_amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bid_amount")
  public @Nullable BigDecimal getBidAmount() {
    return bidAmount;
  }

  public void setBidAmount(@Nullable BigDecimal bidAmount) {
    this.bidAmount = bidAmount;
  }

  public BidOnLot200Response isWinning(@Nullable Boolean isWinning) {
    this.isWinning = isWinning;
    return this;
  }

  /**
   * Get isWinning
   * @return isWinning
   */
  
  @Schema(name = "is_winning", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_winning")
  public @Nullable Boolean getIsWinning() {
    return isWinning;
  }

  public void setIsWinning(@Nullable Boolean isWinning) {
    this.isWinning = isWinning;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BidOnLot200Response bidOnLot200Response = (BidOnLot200Response) o;
    return Objects.equals(this.success, bidOnLot200Response.success) &&
        Objects.equals(this.lotId, bidOnLot200Response.lotId) &&
        Objects.equals(this.bidAmount, bidOnLot200Response.bidAmount) &&
        Objects.equals(this.isWinning, bidOnLot200Response.isWinning);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, lotId, bidAmount, isWinning);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BidOnLot200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    lotId: ").append(toIndentedString(lotId)).append("\n");
    sb.append("    bidAmount: ").append(toIndentedString(bidAmount)).append("\n");
    sb.append("    isWinning: ").append(toIndentedString(isWinning)).append("\n");
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

