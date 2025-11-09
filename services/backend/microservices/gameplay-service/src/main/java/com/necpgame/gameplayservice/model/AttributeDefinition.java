package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * AttributeDefinition
 */


public class AttributeDefinition {

  private @Nullable String attributeId;

  private @Nullable String name;

  private @Nullable String shortName;

  private @Nullable String description;

  @Valid
  private List<String> affects = new ArrayList<>();

  private Integer baseValue = 10;

  private Integer minValue = 3;

  private Integer maxValue = 20;

  public AttributeDefinition attributeId(@Nullable String attributeId) {
    this.attributeId = attributeId;
    return this;
  }

  /**
   * Get attributeId
   * @return attributeId
   */
  
  @Schema(name = "attribute_id", example = "STRENGTH", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_id")
  public @Nullable String getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(@Nullable String attributeId) {
    this.attributeId = attributeId;
  }

  public AttributeDefinition name(@Nullable String name) {
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

  public AttributeDefinition shortName(@Nullable String shortName) {
    this.shortName = shortName;
    return this;
  }

  /**
   * Get shortName
   * @return shortName
   */
  
  @Schema(name = "short_name", example = "STR", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("short_name")
  public @Nullable String getShortName() {
    return shortName;
  }

  public void setShortName(@Nullable String shortName) {
    this.shortName = shortName;
  }

  public AttributeDefinition description(@Nullable String description) {
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

  public AttributeDefinition affects(List<String> affects) {
    this.affects = affects;
    return this;
  }

  public AttributeDefinition addAffectsItem(String affectsItem) {
    if (this.affects == null) {
      this.affects = new ArrayList<>();
    }
    this.affects.add(affectsItem);
    return this;
  }

  /**
   * На что влияет атрибут
   * @return affects
   */
  
  @Schema(name = "affects", example = "[\"Melee damage\",\"Carry weight\",\"Health\"]", description = "На что влияет атрибут", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affects")
  public List<String> getAffects() {
    return affects;
  }

  public void setAffects(List<String> affects) {
    this.affects = affects;
  }

  public AttributeDefinition baseValue(Integer baseValue) {
    this.baseValue = baseValue;
    return this;
  }

  /**
   * Get baseValue
   * @return baseValue
   */
  
  @Schema(name = "base_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_value")
  public Integer getBaseValue() {
    return baseValue;
  }

  public void setBaseValue(Integer baseValue) {
    this.baseValue = baseValue;
  }

  public AttributeDefinition minValue(Integer minValue) {
    this.minValue = minValue;
    return this;
  }

  /**
   * Get minValue
   * @return minValue
   */
  
  @Schema(name = "min_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_value")
  public Integer getMinValue() {
    return minValue;
  }

  public void setMinValue(Integer minValue) {
    this.minValue = minValue;
  }

  public AttributeDefinition maxValue(Integer maxValue) {
    this.maxValue = maxValue;
    return this;
  }

  /**
   * Get maxValue
   * @return maxValue
   */
  
  @Schema(name = "max_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_value")
  public Integer getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(Integer maxValue) {
    this.maxValue = maxValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeDefinition attributeDefinition = (AttributeDefinition) o;
    return Objects.equals(this.attributeId, attributeDefinition.attributeId) &&
        Objects.equals(this.name, attributeDefinition.name) &&
        Objects.equals(this.shortName, attributeDefinition.shortName) &&
        Objects.equals(this.description, attributeDefinition.description) &&
        Objects.equals(this.affects, attributeDefinition.affects) &&
        Objects.equals(this.baseValue, attributeDefinition.baseValue) &&
        Objects.equals(this.minValue, attributeDefinition.minValue) &&
        Objects.equals(this.maxValue, attributeDefinition.maxValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeId, name, shortName, description, affects, baseValue, minValue, maxValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeDefinition {\n");
    sb.append("    attributeId: ").append(toIndentedString(attributeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    shortName: ").append(toIndentedString(shortName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    affects: ").append(toIndentedString(affects)).append("\n");
    sb.append("    baseValue: ").append(toIndentedString(baseValue)).append("\n");
    sb.append("    minValue: ").append(toIndentedString(minValue)).append("\n");
    sb.append("    maxValue: ").append(toIndentedString(maxValue)).append("\n");
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

