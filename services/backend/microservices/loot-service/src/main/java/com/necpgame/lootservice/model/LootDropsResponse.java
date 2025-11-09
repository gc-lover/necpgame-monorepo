package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.LootDrop;
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
 * LootDropsResponse
 */


public class LootDropsResponse {

  @Valid
  private List<@Valid LootDrop> drops = new ArrayList<>();

  public LootDropsResponse drops(List<@Valid LootDrop> drops) {
    this.drops = drops;
    return this;
  }

  public LootDropsResponse addDropsItem(LootDrop dropsItem) {
    if (this.drops == null) {
      this.drops = new ArrayList<>();
    }
    this.drops.add(dropsItem);
    return this;
  }

  /**
   * Get drops
   * @return drops
   */
  @Valid 
  @Schema(name = "drops", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drops")
  public List<@Valid LootDrop> getDrops() {
    return drops;
  }

  public void setDrops(List<@Valid LootDrop> drops) {
    this.drops = drops;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootDropsResponse lootDropsResponse = (LootDropsResponse) o;
    return Objects.equals(this.drops, lootDropsResponse.drops);
  }

  @Override
  public int hashCode() {
    return Objects.hash(drops);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootDropsResponse {\n");
    sb.append("    drops: ").append(toIndentedString(drops)).append("\n");
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

