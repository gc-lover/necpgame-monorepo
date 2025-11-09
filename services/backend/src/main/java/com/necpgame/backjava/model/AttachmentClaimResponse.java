package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Attachment;
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
 * AttachmentClaimResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class AttachmentClaimResponse {

  @Valid
  private List<@Valid Attachment> claimed = new ArrayList<>();

  @Valid
  private List<String> reservedItems = new ArrayList<>();

  public AttachmentClaimResponse claimed(List<@Valid Attachment> claimed) {
    this.claimed = claimed;
    return this;
  }

  public AttachmentClaimResponse addClaimedItem(Attachment claimedItem) {
    if (this.claimed == null) {
      this.claimed = new ArrayList<>();
    }
    this.claimed.add(claimedItem);
    return this;
  }

  /**
   * Get claimed
   * @return claimed
   */
  @Valid 
  @Schema(name = "claimed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("claimed")
  public List<@Valid Attachment> getClaimed() {
    return claimed;
  }

  public void setClaimed(List<@Valid Attachment> claimed) {
    this.claimed = claimed;
  }

  public AttachmentClaimResponse reservedItems(List<String> reservedItems) {
    this.reservedItems = reservedItems;
    return this;
  }

  public AttachmentClaimResponse addReservedItemsItem(String reservedItemsItem) {
    if (this.reservedItems == null) {
      this.reservedItems = new ArrayList<>();
    }
    this.reservedItems.add(reservedItemsItem);
    return this;
  }

  /**
   * Get reservedItems
   * @return reservedItems
   */
  
  @Schema(name = "reservedItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reservedItems")
  public List<String> getReservedItems() {
    return reservedItems;
  }

  public void setReservedItems(List<String> reservedItems) {
    this.reservedItems = reservedItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentClaimResponse attachmentClaimResponse = (AttachmentClaimResponse) o;
    return Objects.equals(this.claimed, attachmentClaimResponse.claimed) &&
        Objects.equals(this.reservedItems, attachmentClaimResponse.reservedItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(claimed, reservedItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentClaimResponse {\n");
    sb.append("    claimed: ").append(toIndentedString(claimed)).append("\n");
    sb.append("    reservedItems: ").append(toIndentedString(reservedItems)).append("\n");
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

