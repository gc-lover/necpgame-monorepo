package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * QuestLootTableRandomLootInnerQuantityRange
 */

@JsonTypeName("QuestLootTable_random_loot_inner_quantity_range")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestLootTableRandomLootInnerQuantityRange {

  private @Nullable Integer min;

  private @Nullable Integer max;

  public QuestLootTableRandomLootInnerQuantityRange min(@Nullable Integer min) {
    this.min = min;
    return this;
  }

  /**
   * Get min
   * @return min
   */
  
  @Schema(name = "min", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min")
  public @Nullable Integer getMin() {
    return min;
  }

  public void setMin(@Nullable Integer min) {
    this.min = min;
  }

  public QuestLootTableRandomLootInnerQuantityRange max(@Nullable Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get max
   * @return max
   */
  
  @Schema(name = "max", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max")
  public @Nullable Integer getMax() {
    return max;
  }

  public void setMax(@Nullable Integer max) {
    this.max = max;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestLootTableRandomLootInnerQuantityRange questLootTableRandomLootInnerQuantityRange = (QuestLootTableRandomLootInnerQuantityRange) o;
    return Objects.equals(this.min, questLootTableRandomLootInnerQuantityRange.min) &&
        Objects.equals(this.max, questLootTableRandomLootInnerQuantityRange.max);
  }

  @Override
  public int hashCode() {
    return Objects.hash(min, max);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestLootTableRandomLootInnerQuantityRange {\n");
    sb.append("    min: ").append(toIndentedString(min)).append("\n");
    sb.append("    max: ").append(toIndentedString(max)).append("\n");
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

