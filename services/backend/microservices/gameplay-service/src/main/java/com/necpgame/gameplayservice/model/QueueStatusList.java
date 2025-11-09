package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.QueueStatus;
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
 * QueueStatusList
 */


public class QueueStatusList {

  @Valid
  private List<@Valid QueueStatus> items = new ArrayList<>();

  private @Nullable Integer totalActive;

  public QueueStatusList() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QueueStatusList(List<@Valid QueueStatus> items) {
    this.items = items;
  }

  public QueueStatusList items(List<@Valid QueueStatus> items) {
    this.items = items;
    return this;
  }

  public QueueStatusList addItemsItem(QueueStatus itemsItem) {
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
  public List<@Valid QueueStatus> getItems() {
    return items;
  }

  public void setItems(List<@Valid QueueStatus> items) {
    this.items = items;
  }

  public QueueStatusList totalActive(@Nullable Integer totalActive) {
    this.totalActive = totalActive;
    return this;
  }

  /**
   * Get totalActive
   * minimum: 0
   * @return totalActive
   */
  @Min(value = 0) 
  @Schema(name = "totalActive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalActive")
  public @Nullable Integer getTotalActive() {
    return totalActive;
  }

  public void setTotalActive(@Nullable Integer totalActive) {
    this.totalActive = totalActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueueStatusList queueStatusList = (QueueStatusList) o;
    return Objects.equals(this.items, queueStatusList.items) &&
        Objects.equals(this.totalActive, queueStatusList.totalActive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, totalActive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueueStatusList {\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    totalActive: ").append(toIndentedString(totalActive)).append("\n");
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

