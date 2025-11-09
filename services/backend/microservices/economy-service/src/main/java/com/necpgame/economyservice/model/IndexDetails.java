package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.IndexDetailsAllOfComponents;
import java.math.BigDecimal;
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
 * IndexDetails
 */


public class IndexDetails {

  private @Nullable String indexId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable BigDecimal currentValue;

  private @Nullable BigDecimal change24h;

  private @Nullable Integer componentsCount;

  @Valid
  private List<@Valid IndexDetailsAllOfComponents> components = new ArrayList<>();

  @Valid
  private List<Object> history = new ArrayList<>();

  public IndexDetails indexId(@Nullable String indexId) {
    this.indexId = indexId;
    return this;
  }

  /**
   * Get indexId
   * @return indexId
   */
  
  @Schema(name = "index_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("index_id")
  public @Nullable String getIndexId() {
    return indexId;
  }

  public void setIndexId(@Nullable String indexId) {
    this.indexId = indexId;
  }

  public IndexDetails name(@Nullable String name) {
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

  public IndexDetails description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public IndexDetails currentValue(@Nullable BigDecimal currentValue) {
    this.currentValue = currentValue;
    return this;
  }

  /**
   * Get currentValue
   * @return currentValue
   */
  @Valid 
  @Schema(name = "current_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_value")
  public @Nullable BigDecimal getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(@Nullable BigDecimal currentValue) {
    this.currentValue = currentValue;
  }

  public IndexDetails change24h(@Nullable BigDecimal change24h) {
    this.change24h = change24h;
    return this;
  }

  /**
   * Изменение за 24 часа (%)
   * @return change24h
   */
  @Valid 
  @Schema(name = "change_24h", description = "Изменение за 24 часа (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("change_24h")
  public @Nullable BigDecimal getChange24h() {
    return change24h;
  }

  public void setChange24h(@Nullable BigDecimal change24h) {
    this.change24h = change24h;
  }

  public IndexDetails componentsCount(@Nullable Integer componentsCount) {
    this.componentsCount = componentsCount;
    return this;
  }

  /**
   * Количество компаний в индексе
   * @return componentsCount
   */
  
  @Schema(name = "components_count", description = "Количество компаний в индексе", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components_count")
  public @Nullable Integer getComponentsCount() {
    return componentsCount;
  }

  public void setComponentsCount(@Nullable Integer componentsCount) {
    this.componentsCount = componentsCount;
  }

  public IndexDetails components(List<@Valid IndexDetailsAllOfComponents> components) {
    this.components = components;
    return this;
  }

  public IndexDetails addComponentsItem(IndexDetailsAllOfComponents componentsItem) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(componentsItem);
    return this;
  }

  /**
   * Компании, входящие в индекс
   * @return components
   */
  @Valid 
  @Schema(name = "components", description = "Компании, входящие в индекс", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components")
  public List<@Valid IndexDetailsAllOfComponents> getComponents() {
    return components;
  }

  public void setComponents(List<@Valid IndexDetailsAllOfComponents> components) {
    this.components = components;
  }

  public IndexDetails history(List<Object> history) {
    this.history = history;
    return this;
  }

  public IndexDetails addHistoryItem(Object historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * История значений индекса
   * @return history
   */
  
  @Schema(name = "history", description = "История значений индекса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<Object> getHistory() {
    return history;
  }

  public void setHistory(List<Object> history) {
    this.history = history;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndexDetails indexDetails = (IndexDetails) o;
    return Objects.equals(this.indexId, indexDetails.indexId) &&
        Objects.equals(this.name, indexDetails.name) &&
        Objects.equals(this.description, indexDetails.description) &&
        Objects.equals(this.currentValue, indexDetails.currentValue) &&
        Objects.equals(this.change24h, indexDetails.change24h) &&
        Objects.equals(this.componentsCount, indexDetails.componentsCount) &&
        Objects.equals(this.components, indexDetails.components) &&
        Objects.equals(this.history, indexDetails.history);
  }

  @Override
  public int hashCode() {
    return Objects.hash(indexId, name, description, currentValue, change24h, componentsCount, components, history);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IndexDetails {\n");
    sb.append("    indexId: ").append(toIndentedString(indexId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    currentValue: ").append(toIndentedString(currentValue)).append("\n");
    sb.append("    change24h: ").append(toIndentedString(change24h)).append("\n");
    sb.append("    componentsCount: ").append(toIndentedString(componentsCount)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

