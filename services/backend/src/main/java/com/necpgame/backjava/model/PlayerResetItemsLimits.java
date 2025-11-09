package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.PlayerResetItemsLimitsAuctionPosts;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerResetItemsLimits
 */

@JsonTypeName("PlayerResetItems_limits")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerResetItemsLimits {

  private @Nullable PlayerResetItemsLimitsAuctionPosts auctionPosts;

  private @Nullable PlayerResetItemsLimitsAuctionPosts craftingSlots;

  public PlayerResetItemsLimits auctionPosts(@Nullable PlayerResetItemsLimitsAuctionPosts auctionPosts) {
    this.auctionPosts = auctionPosts;
    return this;
  }

  /**
   * Get auctionPosts
   * @return auctionPosts
   */
  @Valid 
  @Schema(name = "auction_posts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auction_posts")
  public @Nullable PlayerResetItemsLimitsAuctionPosts getAuctionPosts() {
    return auctionPosts;
  }

  public void setAuctionPosts(@Nullable PlayerResetItemsLimitsAuctionPosts auctionPosts) {
    this.auctionPosts = auctionPosts;
  }

  public PlayerResetItemsLimits craftingSlots(@Nullable PlayerResetItemsLimitsAuctionPosts craftingSlots) {
    this.craftingSlots = craftingSlots;
    return this;
  }

  /**
   * Get craftingSlots
   * @return craftingSlots
   */
  @Valid 
  @Schema(name = "crafting_slots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crafting_slots")
  public @Nullable PlayerResetItemsLimitsAuctionPosts getCraftingSlots() {
    return craftingSlots;
  }

  public void setCraftingSlots(@Nullable PlayerResetItemsLimitsAuctionPosts craftingSlots) {
    this.craftingSlots = craftingSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetItemsLimits playerResetItemsLimits = (PlayerResetItemsLimits) o;
    return Objects.equals(this.auctionPosts, playerResetItemsLimits.auctionPosts) &&
        Objects.equals(this.craftingSlots, playerResetItemsLimits.craftingSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auctionPosts, craftingSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetItemsLimits {\n");
    sb.append("    auctionPosts: ").append(toIndentedString(auctionPosts)).append("\n");
    sb.append("    craftingSlots: ").append(toIndentedString(craftingSlots)).append("\n");
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

