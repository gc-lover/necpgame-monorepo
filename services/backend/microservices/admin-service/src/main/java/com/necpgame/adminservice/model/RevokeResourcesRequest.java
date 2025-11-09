package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.GrantResourcesRequestCurrency;
import com.necpgame.adminservice.model.InventoryAdjustmentItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RevokeResourcesRequest
 */


public class RevokeResourcesRequest {

  private UUID playerId;

  private JsonNullable<GrantResourcesRequestCurrency> currency = JsonNullable.<GrantResourcesRequestCurrency>undefined();

  @Valid
  private JsonNullable<List<@Valid InventoryAdjustmentItem>> items = JsonNullable.<List<@Valid InventoryAdjustmentItem>>undefined();

  private String reason;

  public RevokeResourcesRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RevokeResourcesRequest(UUID playerId, String reason) {
    this.playerId = playerId;
    this.reason = reason;
  }

  public RevokeResourcesRequest playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player_id")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public RevokeResourcesRequest currency(GrantResourcesRequestCurrency currency) {
    this.currency = JsonNullable.of(currency);
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Valid 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public JsonNullable<GrantResourcesRequestCurrency> getCurrency() {
    return currency;
  }

  public void setCurrency(JsonNullable<GrantResourcesRequestCurrency> currency) {
    this.currency = currency;
  }

  public RevokeResourcesRequest items(List<@Valid InventoryAdjustmentItem> items) {
    this.items = JsonNullable.of(items);
    return this;
  }

  public RevokeResourcesRequest addItemsItem(InventoryAdjustmentItem itemsItem) {
    if (this.items == null || !this.items.isPresent()) {
      this.items = JsonNullable.of(new ArrayList<>());
    }
    this.items.get().add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public JsonNullable<List<@Valid InventoryAdjustmentItem>> getItems() {
    return items;
  }

  public void setItems(JsonNullable<List<@Valid InventoryAdjustmentItem>> items) {
    this.items = items;
  }

  public RevokeResourcesRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RevokeResourcesRequest revokeResourcesRequest = (RevokeResourcesRequest) o;
    return Objects.equals(this.playerId, revokeResourcesRequest.playerId) &&
        equalsNullable(this.currency, revokeResourcesRequest.currency) &&
        equalsNullable(this.items, revokeResourcesRequest.items) &&
        Objects.equals(this.reason, revokeResourcesRequest.reason);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, hashCodeNullable(currency), hashCodeNullable(items), reason);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RevokeResourcesRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

