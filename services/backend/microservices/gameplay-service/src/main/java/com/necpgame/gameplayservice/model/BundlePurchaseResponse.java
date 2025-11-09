package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.BundlePurchaseResponseCompensation;
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
 * BundlePurchaseResponse
 */


public class BundlePurchaseResponse {

  private @Nullable String bundleId;

  @Valid
  private List<String> grantedItems = new ArrayList<>();

  private @Nullable BundlePurchaseResponseCompensation compensation;

  public BundlePurchaseResponse bundleId(@Nullable String bundleId) {
    this.bundleId = bundleId;
    return this;
  }

  /**
   * Get bundleId
   * @return bundleId
   */
  
  @Schema(name = "bundleId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bundleId")
  public @Nullable String getBundleId() {
    return bundleId;
  }

  public void setBundleId(@Nullable String bundleId) {
    this.bundleId = bundleId;
  }

  public BundlePurchaseResponse grantedItems(List<String> grantedItems) {
    this.grantedItems = grantedItems;
    return this;
  }

  public BundlePurchaseResponse addGrantedItemsItem(String grantedItemsItem) {
    if (this.grantedItems == null) {
      this.grantedItems = new ArrayList<>();
    }
    this.grantedItems.add(grantedItemsItem);
    return this;
  }

  /**
   * Get grantedItems
   * @return grantedItems
   */
  
  @Schema(name = "grantedItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grantedItems")
  public List<String> getGrantedItems() {
    return grantedItems;
  }

  public void setGrantedItems(List<String> grantedItems) {
    this.grantedItems = grantedItems;
  }

  public BundlePurchaseResponse compensation(@Nullable BundlePurchaseResponseCompensation compensation) {
    this.compensation = compensation;
    return this;
  }

  /**
   * Get compensation
   * @return compensation
   */
  @Valid 
  @Schema(name = "compensation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compensation")
  public @Nullable BundlePurchaseResponseCompensation getCompensation() {
    return compensation;
  }

  public void setCompensation(@Nullable BundlePurchaseResponseCompensation compensation) {
    this.compensation = compensation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BundlePurchaseResponse bundlePurchaseResponse = (BundlePurchaseResponse) o;
    return Objects.equals(this.bundleId, bundlePurchaseResponse.bundleId) &&
        Objects.equals(this.grantedItems, bundlePurchaseResponse.grantedItems) &&
        Objects.equals(this.compensation, bundlePurchaseResponse.compensation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bundleId, grantedItems, compensation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundlePurchaseResponse {\n");
    sb.append("    bundleId: ").append(toIndentedString(bundleId)).append("\n");
    sb.append("    grantedItems: ").append(toIndentedString(grantedItems)).append("\n");
    sb.append("    compensation: ").append(toIndentedString(compensation)).append("\n");
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

