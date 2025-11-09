package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.LootItem;
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
 * GetZoneLoot200Response
 */

@JsonTypeName("getZoneLoot_200_response")

public class GetZoneLoot200Response {

  @Valid
  private List<@Valid LootItem> lootItems = new ArrayList<>();

  public GetZoneLoot200Response lootItems(List<@Valid LootItem> lootItems) {
    this.lootItems = lootItems;
    return this;
  }

  public GetZoneLoot200Response addLootItemsItem(LootItem lootItemsItem) {
    if (this.lootItems == null) {
      this.lootItems = new ArrayList<>();
    }
    this.lootItems.add(lootItemsItem);
    return this;
  }

  /**
   * Get lootItems
   * @return lootItems
   */
  @Valid 
  @Schema(name = "loot_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_items")
  public List<@Valid LootItem> getLootItems() {
    return lootItems;
  }

  public void setLootItems(List<@Valid LootItem> lootItems) {
    this.lootItems = lootItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetZoneLoot200Response getZoneLoot200Response = (GetZoneLoot200Response) o;
    return Objects.equals(this.lootItems, getZoneLoot200Response.lootItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lootItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetZoneLoot200Response {\n");
    sb.append("    lootItems: ").append(toIndentedString(lootItems)).append("\n");
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

