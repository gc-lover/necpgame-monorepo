package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderNewsTag
 */


public class PlayerOrderNewsTag {

  private String tagId;

  private String label;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    ORDER("order"),
    
    CRISIS("crisis"),
    
    ECONOMY("economy"),
    
    REPUTATION("reputation"),
    
    COMMUNITY("community"),
    
    TOURNAMENT("tournament"),
    
    OTHER("other");

    private final String value;

    CategoryEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  public PlayerOrderNewsTag() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderNewsTag(String tagId, String label) {
    this.tagId = tagId;
    this.label = label;
  }

  public PlayerOrderNewsTag tagId(String tagId) {
    this.tagId = tagId;
    return this;
  }

  /**
   * Get tagId
   * @return tagId
   */
  @NotNull 
  @Schema(name = "tagId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tagId")
  public String getTagId() {
    return tagId;
  }

  public void setTagId(String tagId) {
    this.tagId = tagId;
  }

  public PlayerOrderNewsTag label(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get label
   * @return label
   */
  @NotNull 
  @Schema(name = "label", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("label")
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public PlayerOrderNewsTag category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderNewsTag playerOrderNewsTag = (PlayerOrderNewsTag) o;
    return Objects.equals(this.tagId, playerOrderNewsTag.tagId) &&
        Objects.equals(this.label, playerOrderNewsTag.label) &&
        Objects.equals(this.category, playerOrderNewsTag.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagId, label, category);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderNewsTag {\n");
    sb.append("    tagId: ").append(toIndentedString(tagId)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
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

