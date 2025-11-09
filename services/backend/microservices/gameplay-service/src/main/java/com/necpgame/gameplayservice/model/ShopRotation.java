package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.PurchaseRequestExpectedPrice;
import com.necpgame.gameplayservice.model.RotationItem;
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
 * ShopRotation
 */


public class ShopRotation {

  private String rotationId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    DAILY("daily"),
    
    WEEKLY("weekly"),
    
    FEATURED("featured"),
    
    BUNDLE("bundle");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endAt;

  @Valid
  private List<@Valid RotationItem> items = new ArrayList<>();

  private @Nullable PurchaseRequestExpectedPrice bundlePrice;

  private @Nullable String notes;

  public ShopRotation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ShopRotation(String rotationId, TypeEnum type, OffsetDateTime startAt, OffsetDateTime endAt) {
    this.rotationId = rotationId;
    this.type = type;
    this.startAt = startAt;
    this.endAt = endAt;
  }

  public ShopRotation rotationId(String rotationId) {
    this.rotationId = rotationId;
    return this;
  }

  /**
   * Get rotationId
   * @return rotationId
   */
  @NotNull 
  @Schema(name = "rotationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rotationId")
  public String getRotationId() {
    return rotationId;
  }

  public void setRotationId(String rotationId) {
    this.rotationId = rotationId;
  }

  public ShopRotation type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public ShopRotation startAt(OffsetDateTime startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * Get startAt
   * @return startAt
   */
  @NotNull @Valid 
  @Schema(name = "startAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  public OffsetDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(OffsetDateTime startAt) {
    this.startAt = startAt;
  }

  public ShopRotation endAt(OffsetDateTime endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * Get endAt
   * @return endAt
   */
  @NotNull @Valid 
  @Schema(name = "endAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endAt")
  public OffsetDateTime getEndAt() {
    return endAt;
  }

  public void setEndAt(OffsetDateTime endAt) {
    this.endAt = endAt;
  }

  public ShopRotation items(List<@Valid RotationItem> items) {
    this.items = items;
    return this;
  }

  public ShopRotation addItemsItem(RotationItem itemsItem) {
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
  public List<@Valid RotationItem> getItems() {
    return items;
  }

  public void setItems(List<@Valid RotationItem> items) {
    this.items = items;
  }

  public ShopRotation bundlePrice(@Nullable PurchaseRequestExpectedPrice bundlePrice) {
    this.bundlePrice = bundlePrice;
    return this;
  }

  /**
   * Get bundlePrice
   * @return bundlePrice
   */
  @Valid 
  @Schema(name = "bundlePrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bundlePrice")
  public @Nullable PurchaseRequestExpectedPrice getBundlePrice() {
    return bundlePrice;
  }

  public void setBundlePrice(@Nullable PurchaseRequestExpectedPrice bundlePrice) {
    this.bundlePrice = bundlePrice;
  }

  public ShopRotation notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShopRotation shopRotation = (ShopRotation) o;
    return Objects.equals(this.rotationId, shopRotation.rotationId) &&
        Objects.equals(this.type, shopRotation.type) &&
        Objects.equals(this.startAt, shopRotation.startAt) &&
        Objects.equals(this.endAt, shopRotation.endAt) &&
        Objects.equals(this.items, shopRotation.items) &&
        Objects.equals(this.bundlePrice, shopRotation.bundlePrice) &&
        Objects.equals(this.notes, shopRotation.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rotationId, type, startAt, endAt, items, bundlePrice, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShopRotation {\n");
    sb.append("    rotationId: ").append(toIndentedString(rotationId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    bundlePrice: ").append(toIndentedString(bundlePrice)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

