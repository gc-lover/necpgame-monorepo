package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.PurchaseRequestExpectedPrice;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BundlePurchaseRequest
 */


public class BundlePurchaseRequest {

  private String playerId;

  private String bundleId;

  private @Nullable PurchaseRequestExpectedPrice expectedPrice;

  /**
   * Gets or Sets duplicateHandling
   */
  public enum DuplicateHandlingEnum {
    CONVERT_TO_CURRENCY("convert_to_currency"),
    
    GRANT_CHARMS("grant_charms"),
    
    SKIP_ITEM("skip_item");

    private final String value;

    DuplicateHandlingEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static DuplicateHandlingEnum fromValue(String value) {
      for (DuplicateHandlingEnum b : DuplicateHandlingEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DuplicateHandlingEnum duplicateHandling;

  public BundlePurchaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BundlePurchaseRequest(String playerId, String bundleId) {
    this.playerId = playerId;
    this.bundleId = bundleId;
  }

  public BundlePurchaseRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public BundlePurchaseRequest bundleId(String bundleId) {
    this.bundleId = bundleId;
    return this;
  }

  /**
   * Get bundleId
   * @return bundleId
   */
  @NotNull 
  @Schema(name = "bundleId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bundleId")
  public String getBundleId() {
    return bundleId;
  }

  public void setBundleId(String bundleId) {
    this.bundleId = bundleId;
  }

  public BundlePurchaseRequest expectedPrice(@Nullable PurchaseRequestExpectedPrice expectedPrice) {
    this.expectedPrice = expectedPrice;
    return this;
  }

  /**
   * Get expectedPrice
   * @return expectedPrice
   */
  @Valid 
  @Schema(name = "expectedPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedPrice")
  public @Nullable PurchaseRequestExpectedPrice getExpectedPrice() {
    return expectedPrice;
  }

  public void setExpectedPrice(@Nullable PurchaseRequestExpectedPrice expectedPrice) {
    this.expectedPrice = expectedPrice;
  }

  public BundlePurchaseRequest duplicateHandling(@Nullable DuplicateHandlingEnum duplicateHandling) {
    this.duplicateHandling = duplicateHandling;
    return this;
  }

  /**
   * Get duplicateHandling
   * @return duplicateHandling
   */
  
  @Schema(name = "duplicateHandling", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duplicateHandling")
  public @Nullable DuplicateHandlingEnum getDuplicateHandling() {
    return duplicateHandling;
  }

  public void setDuplicateHandling(@Nullable DuplicateHandlingEnum duplicateHandling) {
    this.duplicateHandling = duplicateHandling;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BundlePurchaseRequest bundlePurchaseRequest = (BundlePurchaseRequest) o;
    return Objects.equals(this.playerId, bundlePurchaseRequest.playerId) &&
        Objects.equals(this.bundleId, bundlePurchaseRequest.bundleId) &&
        Objects.equals(this.expectedPrice, bundlePurchaseRequest.expectedPrice) &&
        Objects.equals(this.duplicateHandling, bundlePurchaseRequest.duplicateHandling);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, bundleId, expectedPrice, duplicateHandling);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundlePurchaseRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    bundleId: ").append(toIndentedString(bundleId)).append("\n");
    sb.append("    expectedPrice: ").append(toIndentedString(expectedPrice)).append("\n");
    sb.append("    duplicateHandling: ").append(toIndentedString(duplicateHandling)).append("\n");
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

