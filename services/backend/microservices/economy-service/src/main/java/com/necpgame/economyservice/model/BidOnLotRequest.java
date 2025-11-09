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
 * BidOnLotRequest
 */

@JsonTypeName("bidOnLot_request")

public class BidOnLotRequest {

  private String characterId;

  private String lotId;

  private BigDecimal bidAmount;

  public BidOnLotRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BidOnLotRequest(String characterId, String lotId, BigDecimal bidAmount) {
    this.characterId = characterId;
    this.lotId = lotId;
    this.bidAmount = bidAmount;
  }

  public BidOnLotRequest characterId(String characterId) {
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

  public BidOnLotRequest lotId(String lotId) {
    this.lotId = lotId;
    return this;
  }

  /**
   * Get lotId
   * @return lotId
   */
  @NotNull 
  @Schema(name = "lot_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lot_id")
  public String getLotId() {
    return lotId;
  }

  public void setLotId(String lotId) {
    this.lotId = lotId;
  }

  public BidOnLotRequest bidAmount(BigDecimal bidAmount) {
    this.bidAmount = bidAmount;
    return this;
  }

  /**
   * Get bidAmount
   * @return bidAmount
   */
  @NotNull @Valid 
  @Schema(name = "bid_amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bid_amount")
  public BigDecimal getBidAmount() {
    return bidAmount;
  }

  public void setBidAmount(BigDecimal bidAmount) {
    this.bidAmount = bidAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BidOnLotRequest bidOnLotRequest = (BidOnLotRequest) o;
    return Objects.equals(this.characterId, bidOnLotRequest.characterId) &&
        Objects.equals(this.lotId, bidOnLotRequest.lotId) &&
        Objects.equals(this.bidAmount, bidOnLotRequest.bidAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, lotId, bidAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BidOnLotRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    lotId: ").append(toIndentedString(lotId)).append("\n");
    sb.append("    bidAmount: ").append(toIndentedString(bidAmount)).append("\n");
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

