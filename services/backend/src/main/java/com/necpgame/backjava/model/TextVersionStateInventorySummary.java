package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TextVersionStateInventorySummary
 */

@JsonTypeName("TextVersionState_inventory_summary")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TextVersionStateInventorySummary {

  private @Nullable Integer itemsCount;

  private @Nullable BigDecimal weight;

  public TextVersionStateInventorySummary itemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
    return this;
  }

  /**
   * Get itemsCount
   * @return itemsCount
   */
  
  @Schema(name = "items_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_count")
  public @Nullable Integer getItemsCount() {
    return itemsCount;
  }

  public void setItemsCount(@Nullable Integer itemsCount) {
    this.itemsCount = itemsCount;
  }

  public TextVersionStateInventorySummary weight(@Nullable BigDecimal weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Get weight
   * @return weight
   */
  @Valid 
  @Schema(name = "weight", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weight")
  public @Nullable BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(@Nullable BigDecimal weight) {
    this.weight = weight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TextVersionStateInventorySummary textVersionStateInventorySummary = (TextVersionStateInventorySummary) o;
    return Objects.equals(this.itemsCount, textVersionStateInventorySummary.itemsCount) &&
        Objects.equals(this.weight, textVersionStateInventorySummary.weight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemsCount, weight);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TextVersionStateInventorySummary {\n");
    sb.append("    itemsCount: ").append(toIndentedString(itemsCount)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
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

