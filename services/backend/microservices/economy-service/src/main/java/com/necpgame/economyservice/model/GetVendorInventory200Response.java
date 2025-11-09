package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.VendorItem;
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
 * GetVendorInventory200Response
 */

@JsonTypeName("getVendorInventory_200_response")

public class GetVendorInventory200Response {

  private @Nullable String vendorId;

  @Valid
  private List<@Valid VendorItem> items = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime refreshTime;

  public GetVendorInventory200Response vendorId(@Nullable String vendorId) {
    this.vendorId = vendorId;
    return this;
  }

  /**
   * Get vendorId
   * @return vendorId
   */
  
  @Schema(name = "vendor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendor_id")
  public @Nullable String getVendorId() {
    return vendorId;
  }

  public void setVendorId(@Nullable String vendorId) {
    this.vendorId = vendorId;
  }

  public GetVendorInventory200Response items(List<@Valid VendorItem> items) {
    this.items = items;
    return this;
  }

  public GetVendorInventory200Response addItemsItem(VendorItem itemsItem) {
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
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid VendorItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid VendorItem> items) {
    this.items = items;
  }

  public GetVendorInventory200Response refreshTime(@Nullable OffsetDateTime refreshTime) {
    this.refreshTime = refreshTime;
    return this;
  }

  /**
   * Время обновления ассортимента
   * @return refreshTime
   */
  @Valid 
  @Schema(name = "refresh_time", description = "Время обновления ассортимента", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refresh_time")
  public @Nullable OffsetDateTime getRefreshTime() {
    return refreshTime;
  }

  public void setRefreshTime(@Nullable OffsetDateTime refreshTime) {
    this.refreshTime = refreshTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetVendorInventory200Response getVendorInventory200Response = (GetVendorInventory200Response) o;
    return Objects.equals(this.vendorId, getVendorInventory200Response.vendorId) &&
        Objects.equals(this.items, getVendorInventory200Response.items) &&
        Objects.equals(this.refreshTime, getVendorInventory200Response.refreshTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vendorId, items, refreshTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetVendorInventory200Response {\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    refreshTime: ").append(toIndentedString(refreshTime)).append("\n");
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

