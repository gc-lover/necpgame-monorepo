package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NewsLink
 */


public class NewsLink {

  private String title;

  private URI url;

  /**
   * Gets or Sets relation
   */
  public enum RelationEnum {
    DETAILS("details"),
    
    MAP("map"),
    
    BRIEFING("briefing"),
    
    STATISTICS("statistics");

    private final String value;

    RelationEnum(String value) {
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
    public static RelationEnum fromValue(String value) {
      for (RelationEnum b : RelationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RelationEnum relation;

  public NewsLink() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NewsLink(String title, URI url) {
    this.title = title;
    this.url = url;
  }

  public NewsLink title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public NewsLink url(URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @NotNull @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  public void setUrl(URI url) {
    this.url = url;
  }

  public NewsLink relation(@Nullable RelationEnum relation) {
    this.relation = relation;
    return this;
  }

  /**
   * Get relation
   * @return relation
   */
  
  @Schema(name = "relation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relation")
  public @Nullable RelationEnum getRelation() {
    return relation;
  }

  public void setRelation(@Nullable RelationEnum relation) {
    this.relation = relation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NewsLink newsLink = (NewsLink) o;
    return Objects.equals(this.title, newsLink.title) &&
        Objects.equals(this.url, newsLink.url) &&
        Objects.equals(this.relation, newsLink.relation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, url, relation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NewsLink {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    relation: ").append(toIndentedString(relation)).append("\n");
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

