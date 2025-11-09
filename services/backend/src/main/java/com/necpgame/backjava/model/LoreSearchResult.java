package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * LoreSearchResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LoreSearchResult {

  /**
   * Gets or Sets resultType
   */
  public enum ResultTypeEnum {
    FACTION("FACTION"),
    
    LOCATION("LOCATION"),
    
    CHARACTER("CHARACTER"),
    
    EVENT("EVENT"),
    
    TIMELINE("TIMELINE");

    private final String value;

    ResultTypeEnum(String value) {
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
    public static ResultTypeEnum fromValue(String value) {
      for (ResultTypeEnum b : ResultTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ResultTypeEnum resultType;

  private @Nullable String id;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable BigDecimal relevanceScore;

  public LoreSearchResult resultType(@Nullable ResultTypeEnum resultType) {
    this.resultType = resultType;
    return this;
  }

  /**
   * Get resultType
   * @return resultType
   */
  
  @Schema(name = "result_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("result_type")
  public @Nullable ResultTypeEnum getResultType() {
    return resultType;
  }

  public void setResultType(@Nullable ResultTypeEnum resultType) {
    this.resultType = resultType;
  }

  public LoreSearchResult id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public LoreSearchResult name(@Nullable String name) {
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

  public LoreSearchResult description(@Nullable String description) {
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

  public LoreSearchResult relevanceScore(@Nullable BigDecimal relevanceScore) {
    this.relevanceScore = relevanceScore;
    return this;
  }

  /**
   * Get relevanceScore
   * @return relevanceScore
   */
  @Valid 
  @Schema(name = "relevance_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relevance_score")
  public @Nullable BigDecimal getRelevanceScore() {
    return relevanceScore;
  }

  public void setRelevanceScore(@Nullable BigDecimal relevanceScore) {
    this.relevanceScore = relevanceScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoreSearchResult loreSearchResult = (LoreSearchResult) o;
    return Objects.equals(this.resultType, loreSearchResult.resultType) &&
        Objects.equals(this.id, loreSearchResult.id) &&
        Objects.equals(this.name, loreSearchResult.name) &&
        Objects.equals(this.description, loreSearchResult.description) &&
        Objects.equals(this.relevanceScore, loreSearchResult.relevanceScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resultType, id, name, description, relevanceScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoreSearchResult {\n");
    sb.append("    resultType: ").append(toIndentedString(resultType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    relevanceScore: ").append(toIndentedString(relevanceScore)).append("\n");
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

