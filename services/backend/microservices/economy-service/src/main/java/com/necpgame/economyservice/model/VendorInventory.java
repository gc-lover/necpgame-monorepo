package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.TradeItem;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * VendorInventory
 */


public class VendorInventory {

  private String vendorId;

  @Valid
  private List<@Valid TradeItem> items = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> nextRefresh = JsonNullable.<OffsetDateTime>undefined();

  public VendorInventory() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VendorInventory(String vendorId, List<@Valid TradeItem> items) {
    this.vendorId = vendorId;
    this.items = items;
  }

  public VendorInventory vendorId(String vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  /**
   * Get vendorId
   * @return vendorId
   */
  @NotNull 
  @Schema(name = "vendorId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("vendorId")
  public String getVendorId() {
    return vendorId;
  }

  public void setVendorId(String vendorId) {
    this.vendorId = vendorId;
  }

  public VendorInventory items(List<@Valid TradeItem> items) {
    this.items = items;
    return this;
  }

  public VendorInventory addItemsItem(TradeItem itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid TradeItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid TradeItem> items) {
    this.items = items;
  }

  public VendorInventory nextRefresh(OffsetDateTime nextRefresh) {
    this.nextRefresh = JsonNullable.of(nextRefresh);
    return this;
  }

  /**
   * Get nextRefresh
   * @return nextRefresh
   */
  @Valid 
  @Schema(name = "nextRefresh", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextRefresh")
  public JsonNullable<OffsetDateTime> getNextRefresh() {
    return nextRefresh;
  }

  public void setNextRefresh(JsonNullable<OffsetDateTime> nextRefresh) {
    this.nextRefresh = nextRefresh;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VendorInventory vendorInventory = (VendorInventory) o;
    return Objects.equals(this.vendorId, vendorInventory.vendorId) &&
        Objects.equals(this.items, vendorInventory.items) &&
        equalsNullable(this.nextRefresh, vendorInventory.nextRefresh);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(vendorId, items, hashCodeNullable(nextRefresh));
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
    sb.append("class VendorInventory {\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    nextRefresh: ").append(toIndentedString(nextRefresh)).append("\n");
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

