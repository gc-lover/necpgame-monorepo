package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CosmeticItemMetadata
 */

@JsonTypeName("CosmeticItem_metadata")

public class CosmeticItemMetadata {

  private @Nullable String collectionId;

  @Valid
  private List<String> bundleIds = new ArrayList<>();

  @Valid
  private List<String> tags = new ArrayList<>();

  public CosmeticItemMetadata collectionId(@Nullable String collectionId) {
    this.collectionId = collectionId;
    return this;
  }

  /**
   * Get collectionId
   * @return collectionId
   */
  
  @Schema(name = "collectionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collectionId")
  public @Nullable String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(@Nullable String collectionId) {
    this.collectionId = collectionId;
  }

  public CosmeticItemMetadata bundleIds(List<String> bundleIds) {
    this.bundleIds = bundleIds;
    return this;
  }

  public CosmeticItemMetadata addBundleIdsItem(String bundleIdsItem) {
    if (this.bundleIds == null) {
      this.bundleIds = new ArrayList<>();
    }
    this.bundleIds.add(bundleIdsItem);
    return this;
  }

  /**
   * Get bundleIds
   * @return bundleIds
   */
  
  @Schema(name = "bundleIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bundleIds")
  public List<String> getBundleIds() {
    return bundleIds;
  }

  public void setBundleIds(List<String> bundleIds) {
    this.bundleIds = bundleIds;
  }

  public CosmeticItemMetadata tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public CosmeticItemMetadata addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticItemMetadata cosmeticItemMetadata = (CosmeticItemMetadata) o;
    return Objects.equals(this.collectionId, cosmeticItemMetadata.collectionId) &&
        Objects.equals(this.bundleIds, cosmeticItemMetadata.bundleIds) &&
        Objects.equals(this.tags, cosmeticItemMetadata.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(collectionId, bundleIds, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticItemMetadata {\n");
    sb.append("    collectionId: ").append(toIndentedString(collectionId)).append("\n");
    sb.append("    bundleIds: ").append(toIndentedString(bundleIds)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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

