package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FriendRequestDecline
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class FriendRequestDecline {

  private @Nullable String reason;

  private @Nullable Boolean block;

  public FriendRequestDecline reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public FriendRequestDecline block(@Nullable Boolean block) {
    this.block = block;
    return this;
  }

  /**
   * Get block
   * @return block
   */
  
  @Schema(name = "block", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("block")
  public @Nullable Boolean getBlock() {
    return block;
  }

  public void setBlock(@Nullable Boolean block) {
    this.block = block;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FriendRequestDecline friendRequestDecline = (FriendRequestDecline) o;
    return Objects.equals(this.reason, friendRequestDecline.reason) &&
        Objects.equals(this.block, friendRequestDecline.block);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, block);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FriendRequestDecline {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    block: ").append(toIndentedString(block)).append("\n");
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

