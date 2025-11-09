package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partyservice.model.LootDistributeRequestVotesInner;
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
 * LootDistributeRequest
 */


public class LootDistributeRequest {

  private String itemId;

  private @Nullable String itemRarity;

  private @Nullable String winnerMemberId;

  @Valid
  private List<@Valid LootDistributeRequestVotesInner> votes = new ArrayList<>();

  private @Nullable String idempotencyKey;

  public LootDistributeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootDistributeRequest(String itemId) {
    this.itemId = itemId;
  }

  public LootDistributeRequest itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public LootDistributeRequest itemRarity(@Nullable String itemRarity) {
    this.itemRarity = itemRarity;
    return this;
  }

  /**
   * Get itemRarity
   * @return itemRarity
   */
  
  @Schema(name = "itemRarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemRarity")
  public @Nullable String getItemRarity() {
    return itemRarity;
  }

  public void setItemRarity(@Nullable String itemRarity) {
    this.itemRarity = itemRarity;
  }

  public LootDistributeRequest winnerMemberId(@Nullable String winnerMemberId) {
    this.winnerMemberId = winnerMemberId;
    return this;
  }

  /**
   * Get winnerMemberId
   * @return winnerMemberId
   */
  
  @Schema(name = "winnerMemberId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("winnerMemberId")
  public @Nullable String getWinnerMemberId() {
    return winnerMemberId;
  }

  public void setWinnerMemberId(@Nullable String winnerMemberId) {
    this.winnerMemberId = winnerMemberId;
  }

  public LootDistributeRequest votes(List<@Valid LootDistributeRequestVotesInner> votes) {
    this.votes = votes;
    return this;
  }

  public LootDistributeRequest addVotesItem(LootDistributeRequestVotesInner votesItem) {
    if (this.votes == null) {
      this.votes = new ArrayList<>();
    }
    this.votes.add(votesItem);
    return this;
  }

  /**
   * Get votes
   * @return votes
   */
  @Valid 
  @Schema(name = "votes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("votes")
  public List<@Valid LootDistributeRequestVotesInner> getVotes() {
    return votes;
  }

  public void setVotes(List<@Valid LootDistributeRequestVotesInner> votes) {
    this.votes = votes;
  }

  public LootDistributeRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootDistributeRequest lootDistributeRequest = (LootDistributeRequest) o;
    return Objects.equals(this.itemId, lootDistributeRequest.itemId) &&
        Objects.equals(this.itemRarity, lootDistributeRequest.itemRarity) &&
        Objects.equals(this.winnerMemberId, lootDistributeRequest.winnerMemberId) &&
        Objects.equals(this.votes, lootDistributeRequest.votes) &&
        Objects.equals(this.idempotencyKey, lootDistributeRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemRarity, winnerMemberId, votes, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootDistributeRequest {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemRarity: ").append(toIndentedString(itemRarity)).append("\n");
    sb.append("    winnerMemberId: ").append(toIndentedString(winnerMemberId)).append("\n");
    sb.append("    votes: ").append(toIndentedString(votes)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

