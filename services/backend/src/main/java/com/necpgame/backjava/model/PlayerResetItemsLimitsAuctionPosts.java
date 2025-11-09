package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerResetItemsLimitsAuctionPosts
 */

@JsonTypeName("PlayerResetItems_limits_auction_posts")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerResetItemsLimitsAuctionPosts {

  private @Nullable Integer used;

  private @Nullable Integer max;

  public PlayerResetItemsLimitsAuctionPosts used(@Nullable Integer used) {
    this.used = used;
    return this;
  }

  /**
   * Get used
   * @return used
   */
  
  @Schema(name = "used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("used")
  public @Nullable Integer getUsed() {
    return used;
  }

  public void setUsed(@Nullable Integer used) {
    this.used = used;
  }

  public PlayerResetItemsLimitsAuctionPosts max(@Nullable Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable Integer getMax() {
    return max;
  }

  public void setMax(@Nullable Integer max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetItemsLimitsAuctionPosts playerResetItemsLimitsAuctionPosts = (PlayerResetItemsLimitsAuctionPosts) o;
    return Objects.equals(this.used, playerResetItemsLimitsAuctionPosts.used) &&
        Objects.equals(this.max, playerResetItemsLimitsAuctionPosts.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(used, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetItemsLimitsAuctionPosts {\n");
    sb.append("    used: ").append(toIndentedString(used)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

