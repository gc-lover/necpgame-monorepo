package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ItemGrant
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ItemGrant {

  private String itemId;

  private Integer quantity;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  private @Nullable String targetContainer;

  public ItemGrant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ItemGrant(String itemId, Integer quantity) {
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public ItemGrant itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public ItemGrant quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  @NotNull 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("quantity")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ItemGrant metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public ItemGrant putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  public ItemGrant targetContainer(@Nullable String targetContainer) {
    this.targetContainer = targetContainer;
    return this;
  }

  /**
   * Get targetContainer
   * @return targetContainer
   */
  
  @Schema(name = "targetContainer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetContainer")
  public @Nullable String getTargetContainer() {
    return targetContainer;
  }

  public void setTargetContainer(@Nullable String targetContainer) {
    this.targetContainer = targetContainer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemGrant itemGrant = (ItemGrant) o;
    return Objects.equals(this.itemId, itemGrant.itemId) &&
        Objects.equals(this.quantity, itemGrant.quantity) &&
        Objects.equals(this.metadata, itemGrant.metadata) &&
        Objects.equals(this.targetContainer, itemGrant.targetContainer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, quantity, metadata, targetContainer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemGrant {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    targetContainer: ").append(toIndentedString(targetContainer)).append("\n");
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

