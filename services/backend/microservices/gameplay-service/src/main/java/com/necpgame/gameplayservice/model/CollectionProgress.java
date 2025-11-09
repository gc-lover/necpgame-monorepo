package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CollectionProgressCompletionReward;
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
 * CollectionProgress
 */


public class CollectionProgress {

  private String collectionId;

  private @Nullable String name;

  private Integer totalItems;

  private Integer ownedItems;

  private @Nullable String rewardPreview;

  private @Nullable CollectionProgressCompletionReward completionReward;

  @Valid
  private List<String> items = new ArrayList<>();

  public CollectionProgress() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CollectionProgress(String collectionId, Integer totalItems, Integer ownedItems) {
    this.collectionId = collectionId;
    this.totalItems = totalItems;
    this.ownedItems = ownedItems;
  }

  public CollectionProgress collectionId(String collectionId) {
    this.collectionId = collectionId;
    return this;
  }

  /**
   * Get collectionId
   * @return collectionId
   */
  @NotNull 
  @Schema(name = "collectionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("collectionId")
  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public CollectionProgress name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public CollectionProgress totalItems(Integer totalItems) {
    this.totalItems = totalItems;
    return this;
  }

  /**
   * Get totalItems
   * @return totalItems
   */
  @NotNull 
  @Schema(name = "totalItems", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalItems")
  public Integer getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(Integer totalItems) {
    this.totalItems = totalItems;
  }

  public CollectionProgress ownedItems(Integer ownedItems) {
    this.ownedItems = ownedItems;
    return this;
  }

  /**
   * Get ownedItems
   * @return ownedItems
   */
  @NotNull 
  @Schema(name = "ownedItems", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownedItems")
  public Integer getOwnedItems() {
    return ownedItems;
  }

  public void setOwnedItems(Integer ownedItems) {
    this.ownedItems = ownedItems;
  }

  public CollectionProgress rewardPreview(@Nullable String rewardPreview) {
    this.rewardPreview = rewardPreview;
    return this;
  }

  /**
   * Get rewardPreview
   * @return rewardPreview
   */
  
  @Schema(name = "rewardPreview", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardPreview")
  public @Nullable String getRewardPreview() {
    return rewardPreview;
  }

  public void setRewardPreview(@Nullable String rewardPreview) {
    this.rewardPreview = rewardPreview;
  }

  public CollectionProgress completionReward(@Nullable CollectionProgressCompletionReward completionReward) {
    this.completionReward = completionReward;
    return this;
  }

  /**
   * Get completionReward
   * @return completionReward
   */
  @Valid 
  @Schema(name = "completionReward", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completionReward")
  public @Nullable CollectionProgressCompletionReward getCompletionReward() {
    return completionReward;
  }

  public void setCompletionReward(@Nullable CollectionProgressCompletionReward completionReward) {
    this.completionReward = completionReward;
  }

  public CollectionProgress items(List<String> items) {
    this.items = items;
    return this;
  }

  public CollectionProgress addItemsItem(String itemsItem) {
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
  
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
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
    CollectionProgress collectionProgress = (CollectionProgress) o;
    return Objects.equals(this.collectionId, collectionProgress.collectionId) &&
        Objects.equals(this.name, collectionProgress.name) &&
        Objects.equals(this.totalItems, collectionProgress.totalItems) &&
        Objects.equals(this.ownedItems, collectionProgress.ownedItems) &&
        Objects.equals(this.rewardPreview, collectionProgress.rewardPreview) &&
        Objects.equals(this.completionReward, collectionProgress.completionReward) &&
        Objects.equals(this.items, collectionProgress.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(collectionId, name, totalItems, ownedItems, rewardPreview, completionReward, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionProgress {\n");
    sb.append("    collectionId: ").append(toIndentedString(collectionId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
    sb.append("    ownedItems: ").append(toIndentedString(ownedItems)).append("\n");
    sb.append("    rewardPreview: ").append(toIndentedString(rewardPreview)).append("\n");
    sb.append("    completionReward: ").append(toIndentedString(completionReward)).append("\n");
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

