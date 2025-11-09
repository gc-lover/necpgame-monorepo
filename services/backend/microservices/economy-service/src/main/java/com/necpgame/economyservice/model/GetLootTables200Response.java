package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.LootTable;
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
 * GetLootTables200Response
 */

@JsonTypeName("getLootTables_200_response")

public class GetLootTables200Response {

  @Valid
  private List<@Valid LootTable> lootTables = new ArrayList<>();

  public GetLootTables200Response lootTables(List<@Valid LootTable> lootTables) {
    this.lootTables = lootTables;
    return this;
  }

  public GetLootTables200Response addLootTablesItem(LootTable lootTablesItem) {
    if (this.lootTables == null) {
      this.lootTables = new ArrayList<>();
    }
    this.lootTables.add(lootTablesItem);
    return this;
  }

  /**
   * Get lootTables
   * @return lootTables
   */
  @Valid 
  @Schema(name = "loot_tables", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_tables")
  public List<@Valid LootTable> getLootTables() {
    return lootTables;
  }

  public void setLootTables(List<@Valid LootTable> lootTables) {
    this.lootTables = lootTables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetLootTables200Response getLootTables200Response = (GetLootTables200Response) o;
    return Objects.equals(this.lootTables, getLootTables200Response.lootTables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lootTables);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetLootTables200Response {\n");
    sb.append("    lootTables: ").append(toIndentedString(lootTables)).append("\n");
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

