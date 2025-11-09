package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.PurchaseResponseUpdatedBalance;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PurchaseResponse
 */


public class PurchaseResponse {

  private @Nullable String itemId;

  private @Nullable String playerId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime grantedAt;

  private @Nullable PurchaseResponseUpdatedBalance updatedBalance;

  @Valid
  private List<String> rewardTokens = new ArrayList<>();

  public PurchaseResponse itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public PurchaseResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public PurchaseResponse grantedAt(@Nullable OffsetDateTime grantedAt) {
    this.grantedAt = grantedAt;
    return this;
  }

  /**
   * Get grantedAt
   * @return grantedAt
   */
  @Valid 
  @Schema(name = "grantedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grantedAt")
  public @Nullable OffsetDateTime getGrantedAt() {
    return grantedAt;
  }

  public void setGrantedAt(@Nullable OffsetDateTime grantedAt) {
    this.grantedAt = grantedAt;
  }

  public PurchaseResponse updatedBalance(@Nullable PurchaseResponseUpdatedBalance updatedBalance) {
    this.updatedBalance = updatedBalance;
    return this;
  }

  /**
   * Get updatedBalance
   * @return updatedBalance
   */
  @Valid 
  @Schema(name = "updatedBalance", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedBalance")
  public @Nullable PurchaseResponseUpdatedBalance getUpdatedBalance() {
    return updatedBalance;
  }

  public void setUpdatedBalance(@Nullable PurchaseResponseUpdatedBalance updatedBalance) {
    this.updatedBalance = updatedBalance;
  }

  public PurchaseResponse rewardTokens(List<String> rewardTokens) {
    this.rewardTokens = rewardTokens;
    return this;
  }

  public PurchaseResponse addRewardTokensItem(String rewardTokensItem) {
    if (this.rewardTokens == null) {
      this.rewardTokens = new ArrayList<>();
    }
    this.rewardTokens.add(rewardTokensItem);
    return this;
  }

  /**
   * Get rewardTokens
   * @return rewardTokens
   */
  
  @Schema(name = "rewardTokens", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardTokens")
  public List<String> getRewardTokens() {
    return rewardTokens;
  }

  public void setRewardTokens(List<String> rewardTokens) {
    this.rewardTokens = rewardTokens;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseResponse purchaseResponse = (PurchaseResponse) o;
    return Objects.equals(this.itemId, purchaseResponse.itemId) &&
        Objects.equals(this.playerId, purchaseResponse.playerId) &&
        Objects.equals(this.grantedAt, purchaseResponse.grantedAt) &&
        Objects.equals(this.updatedBalance, purchaseResponse.updatedBalance) &&
        Objects.equals(this.rewardTokens, purchaseResponse.rewardTokens);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, playerId, grantedAt, updatedBalance, rewardTokens);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseResponse {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    grantedAt: ").append(toIndentedString(grantedAt)).append("\n");
    sb.append("    updatedBalance: ").append(toIndentedString(updatedBalance)).append("\n");
    sb.append("    rewardTokens: ").append(toIndentedString(rewardTokens)).append("\n");
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

