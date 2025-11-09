package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SmurfFlag;
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
 * SmurfFlagList
 */


public class SmurfFlagList {

  @Valid
  private List<@Valid SmurfFlag> items = new ArrayList<>();

  private @Nullable Float threshold;

  public SmurfFlagList() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SmurfFlagList(List<@Valid SmurfFlag> items) {
    this.items = items;
  }

  public SmurfFlagList items(List<@Valid SmurfFlag> items) {
    this.items = items;
    return this;
  }

  public SmurfFlagList addItemsItem(SmurfFlag itemsItem) {
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
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid SmurfFlag> getItems() {
    return items;
  }

  public void setItems(List<@Valid SmurfFlag> items) {
    this.items = items;
  }

  public SmurfFlagList threshold(@Nullable Float threshold) {
    this.threshold = threshold;
    return this;
  }

  /**
   * Get threshold
   * @return threshold
   */
  
  @Schema(name = "threshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threshold")
  public @Nullable Float getThreshold() {
    return threshold;
  }

  public void setThreshold(@Nullable Float threshold) {
    this.threshold = threshold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmurfFlagList smurfFlagList = (SmurfFlagList) o;
    return Objects.equals(this.items, smurfFlagList.items) &&
        Objects.equals(this.threshold, smurfFlagList.threshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, threshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmurfFlagList {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    threshold: ").append(toIndentedString(threshold)).append("\n");
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

