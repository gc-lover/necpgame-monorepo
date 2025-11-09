package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetVendorPrices200ResponseItemsInner;
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
 * GetVendorPrices200Response
 */

@JsonTypeName("getVendorPrices_200_response")

public class GetVendorPrices200Response {

  private @Nullable String vendorId;

  private @Nullable String vendorName;

  private @Nullable String faction;

  private @Nullable String location;

  @Valid
  private List<@Valid GetVendorPrices200ResponseItemsInner> items = new ArrayList<>();

  public GetVendorPrices200Response vendorId(@Nullable String vendorId) {
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

  public GetVendorPrices200Response vendorName(@Nullable String vendorName) {
    this.vendorName = vendorName;
    return this;
  }

  /**
   * Get vendorName
   * @return vendorName
   */
  
  @Schema(name = "vendor_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vendor_name")
  public @Nullable String getVendorName() {
    return vendorName;
  }

  public void setVendorName(@Nullable String vendorName) {
    this.vendorName = vendorName;
  }

  public GetVendorPrices200Response faction(@Nullable String faction) {
    this.faction = faction;
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public @Nullable String getFaction() {
    return faction;
  }

  public void setFaction(@Nullable String faction) {
    this.faction = faction;
  }

  public GetVendorPrices200Response location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public GetVendorPrices200Response items(List<@Valid GetVendorPrices200ResponseItemsInner> items) {
    this.items = items;
    return this;
  }

  public GetVendorPrices200Response addItemsItem(GetVendorPrices200ResponseItemsInner itemsItem) {
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
  public List<@Valid GetVendorPrices200ResponseItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid GetVendorPrices200ResponseItemsInner> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetVendorPrices200Response getVendorPrices200Response = (GetVendorPrices200Response) o;
    return Objects.equals(this.vendorId, getVendorPrices200Response.vendorId) &&
        Objects.equals(this.vendorName, getVendorPrices200Response.vendorName) &&
        Objects.equals(this.faction, getVendorPrices200Response.faction) &&
        Objects.equals(this.location, getVendorPrices200Response.location) &&
        Objects.equals(this.items, getVendorPrices200Response.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vendorId, vendorName, faction, location, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetVendorPrices200Response {\n");
    sb.append("    vendorId: ").append(toIndentedString(vendorId)).append("\n");
    sb.append("    vendorName: ").append(toIndentedString(vendorName)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

