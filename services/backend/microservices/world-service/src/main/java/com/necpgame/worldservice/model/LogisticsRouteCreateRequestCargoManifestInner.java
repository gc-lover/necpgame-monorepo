package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LogisticsRouteCreateRequestCargoManifestInner
 */

@JsonTypeName("LogisticsRouteCreateRequest_cargoManifest_inner")

public class LogisticsRouteCreateRequestCargoManifestInner {

  private @Nullable String itemId;

  private @Nullable BigDecimal quantity;

  public LogisticsRouteCreateRequestCargoManifestInner itemId(@Nullable String itemId) {
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

  public LogisticsRouteCreateRequestCargoManifestInner quantity(@Nullable BigDecimal quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @Valid 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable BigDecimal quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LogisticsRouteCreateRequestCargoManifestInner logisticsRouteCreateRequestCargoManifestInner = (LogisticsRouteCreateRequestCargoManifestInner) o;
    return Objects.equals(this.itemId, logisticsRouteCreateRequestCargoManifestInner.itemId) &&
        Objects.equals(this.quantity, logisticsRouteCreateRequestCargoManifestInner.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LogisticsRouteCreateRequestCargoManifestInner {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
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

