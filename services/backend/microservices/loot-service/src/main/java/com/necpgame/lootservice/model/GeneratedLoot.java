package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.GeneratedLootItemsInner;
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
 * GeneratedLoot
 */


public class GeneratedLoot {

  private @Nullable String dropId;

  private @Nullable String sourceType;

  @Valid
  private List<@Valid GeneratedLootItemsInner> items = new ArrayList<>();

  public GeneratedLoot dropId(@Nullable String dropId) {
    this.dropId = dropId;
    return this;
  }

  /**
   * Get dropId
   * @return dropId
   */
  
  @Schema(name = "drop_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drop_id")
  public @Nullable String getDropId() {
    return dropId;
  }

  public void setDropId(@Nullable String dropId) {
    this.dropId = dropId;
  }

  public GeneratedLoot sourceType(@Nullable String sourceType) {
    this.sourceType = sourceType;
    return this;
  }

  /**
   * Get sourceType
   * @return sourceType
   */
  
  @Schema(name = "source_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source_type")
  public @Nullable String getSourceType() {
    return sourceType;
  }

  public void setSourceType(@Nullable String sourceType) {
    this.sourceType = sourceType;
  }

  public GeneratedLoot items(List<@Valid GeneratedLootItemsInner> items) {
    this.items = items;
    return this;
  }

  public GeneratedLoot addItemsItem(GeneratedLootItemsInner itemsItem) {
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
  public List<@Valid GeneratedLootItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid GeneratedLootItemsInner> items) {
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
    GeneratedLoot generatedLoot = (GeneratedLoot) o;
    return Objects.equals(this.dropId, generatedLoot.dropId) &&
        Objects.equals(this.sourceType, generatedLoot.sourceType) &&
        Objects.equals(this.items, generatedLoot.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dropId, sourceType, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeneratedLoot {\n");
    sb.append("    dropId: ").append(toIndentedString(dropId)).append("\n");
    sb.append("    sourceType: ").append(toIndentedString(sourceType)).append("\n");
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

