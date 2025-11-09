package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AttributesMatrixClassesInner
 */

@JsonTypeName("AttributesMatrix_classes_inner")

public class AttributesMatrixClassesInner {

  private @Nullable String classId;

  private @Nullable String className;

  @Valid
  private Map<String, Integer> startingAttributes = new HashMap<>();

  @Valid
  private Map<String, BigDecimal> attributeGrowthBonuses = new HashMap<>();

  @Valid
  private List<String> recommendedAttributes = new ArrayList<>();

  public AttributesMatrixClassesInner classId(@Nullable String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_id")
  public @Nullable String getClassId() {
    return classId;
  }

  public void setClassId(@Nullable String classId) {
    this.classId = classId;
  }

  public AttributesMatrixClassesInner className(@Nullable String className) {
    this.className = className;
    return this;
  }

  /**
   * Get className
   * @return className
   */
  
  @Schema(name = "class_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_name")
  public @Nullable String getClassName() {
    return className;
  }

  public void setClassName(@Nullable String className) {
    this.className = className;
  }

  public AttributesMatrixClassesInner startingAttributes(Map<String, Integer> startingAttributes) {
    this.startingAttributes = startingAttributes;
    return this;
  }

  public AttributesMatrixClassesInner putStartingAttributesItem(String key, Integer startingAttributesItem) {
    if (this.startingAttributes == null) {
      this.startingAttributes = new HashMap<>();
    }
    this.startingAttributes.put(key, startingAttributesItem);
    return this;
  }

  /**
   * Get startingAttributes
   * @return startingAttributes
   */
  
  @Schema(name = "starting_attributes", example = "{\"STRENGTH\":12,\"DEXTERITY\":14,\"INTELLIGENCE\":8}", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_attributes")
  public Map<String, Integer> getStartingAttributes() {
    return startingAttributes;
  }

  public void setStartingAttributes(Map<String, Integer> startingAttributes) {
    this.startingAttributes = startingAttributes;
  }

  public AttributesMatrixClassesInner attributeGrowthBonuses(Map<String, BigDecimal> attributeGrowthBonuses) {
    this.attributeGrowthBonuses = attributeGrowthBonuses;
    return this;
  }

  public AttributesMatrixClassesInner putAttributeGrowthBonusesItem(String key, BigDecimal attributeGrowthBonusesItem) {
    if (this.attributeGrowthBonuses == null) {
      this.attributeGrowthBonuses = new HashMap<>();
    }
    this.attributeGrowthBonuses.put(key, attributeGrowthBonusesItem);
    return this;
  }

  /**
   * Бонусы роста при level up
   * @return attributeGrowthBonuses
   */
  @Valid 
  @Schema(name = "attribute_growth_bonuses", description = "Бонусы роста при level up", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_growth_bonuses")
  public Map<String, BigDecimal> getAttributeGrowthBonuses() {
    return attributeGrowthBonuses;
  }

  public void setAttributeGrowthBonuses(Map<String, BigDecimal> attributeGrowthBonuses) {
    this.attributeGrowthBonuses = attributeGrowthBonuses;
  }

  public AttributesMatrixClassesInner recommendedAttributes(List<String> recommendedAttributes) {
    this.recommendedAttributes = recommendedAttributes;
    return this;
  }

  public AttributesMatrixClassesInner addRecommendedAttributesItem(String recommendedAttributesItem) {
    if (this.recommendedAttributes == null) {
      this.recommendedAttributes = new ArrayList<>();
    }
    this.recommendedAttributes.add(recommendedAttributesItem);
    return this;
  }

  /**
   * Рекомендуемые для прокачки
   * @return recommendedAttributes
   */
  
  @Schema(name = "recommended_attributes", description = "Рекомендуемые для прокачки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommended_attributes")
  public List<String> getRecommendedAttributes() {
    return recommendedAttributes;
  }

  public void setRecommendedAttributes(List<String> recommendedAttributes) {
    this.recommendedAttributes = recommendedAttributes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributesMatrixClassesInner attributesMatrixClassesInner = (AttributesMatrixClassesInner) o;
    return Objects.equals(this.classId, attributesMatrixClassesInner.classId) &&
        Objects.equals(this.className, attributesMatrixClassesInner.className) &&
        Objects.equals(this.startingAttributes, attributesMatrixClassesInner.startingAttributes) &&
        Objects.equals(this.attributeGrowthBonuses, attributesMatrixClassesInner.attributeGrowthBonuses) &&
        Objects.equals(this.recommendedAttributes, attributesMatrixClassesInner.recommendedAttributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, className, startingAttributes, attributeGrowthBonuses, recommendedAttributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributesMatrixClassesInner {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    className: ").append(toIndentedString(className)).append("\n");
    sb.append("    startingAttributes: ").append(toIndentedString(startingAttributes)).append("\n");
    sb.append("    attributeGrowthBonuses: ").append(toIndentedString(attributeGrowthBonuses)).append("\n");
    sb.append("    recommendedAttributes: ").append(toIndentedString(recommendedAttributes)).append("\n");
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

