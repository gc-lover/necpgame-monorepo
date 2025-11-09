package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * AttachmentItem
 */

@JsonTypeName("Attachment_item")

public class AttachmentItem {

  private @Nullable String itemId;

  private @Nullable String itemInstanceId;

  private @Nullable Integer quantity;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public AttachmentItem itemId(@Nullable String itemId) {
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

  public AttachmentItem itemInstanceId(@Nullable String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
    return this;
  }

  /**
   * Get itemInstanceId
   * @return itemInstanceId
   */
  
  @Schema(name = "itemInstanceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemInstanceId")
  public @Nullable String getItemInstanceId() {
    return itemInstanceId;
  }

  public void setItemInstanceId(@Nullable String itemInstanceId) {
    this.itemInstanceId = itemInstanceId;
  }

  public AttachmentItem quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * @return quantity
   */
  
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public AttachmentItem metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public AttachmentItem putMetadataItem(String key, Object metadataItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttachmentItem attachmentItem = (AttachmentItem) o;
    return Objects.equals(this.itemId, attachmentItem.itemId) &&
        Objects.equals(this.itemInstanceId, attachmentItem.itemInstanceId) &&
        Objects.equals(this.quantity, attachmentItem.quantity) &&
        Objects.equals(this.metadata, attachmentItem.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, itemInstanceId, quantity, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttachmentItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    itemInstanceId: ").append(toIndentedString(itemInstanceId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

