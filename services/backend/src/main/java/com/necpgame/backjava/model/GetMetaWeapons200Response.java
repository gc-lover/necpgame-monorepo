package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.GetMetaWeapons200ResponseRecommendationsInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetMetaWeapons200Response
 */

@JsonTypeName("getMetaWeapons_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:14:20.180301500+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetMetaWeapons200Response {

  private @Nullable String contentType;

  @Valid
  private List<@Valid GetMetaWeapons200ResponseRecommendationsInner> recommendations = new ArrayList<>();

  public GetMetaWeapons200Response contentType(@Nullable String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * Get contentType
   * @return contentType
   */
  
  @Schema(name = "content_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content_type")
  public @Nullable String getContentType() {
    return contentType;
  }

  public void setContentType(@Nullable String contentType) {
    this.contentType = contentType;
  }

  public GetMetaWeapons200Response recommendations(List<@Valid GetMetaWeapons200ResponseRecommendationsInner> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public GetMetaWeapons200Response addRecommendationsItem(GetMetaWeapons200ResponseRecommendationsInner recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Get recommendations
   * @return recommendations
   */
  @Valid 
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<@Valid GetMetaWeapons200ResponseRecommendationsInner> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<@Valid GetMetaWeapons200ResponseRecommendationsInner> recommendations) {
    this.recommendations = recommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMetaWeapons200Response getMetaWeapons200Response = (GetMetaWeapons200Response) o;
    return Objects.equals(this.contentType, getMetaWeapons200Response.contentType) &&
        Objects.equals(this.recommendations, getMetaWeapons200Response.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contentType, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMetaWeapons200Response {\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
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


