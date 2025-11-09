package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.GetQuestRecommendations200ResponseRecommendationsInner;
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
 * GetQuestRecommendations200Response
 */

@JsonTypeName("getQuestRecommendations_200_response")

public class GetQuestRecommendations200Response {

  @Valid
  private List<@Valid GetQuestRecommendations200ResponseRecommendationsInner> recommendations = new ArrayList<>();

  public GetQuestRecommendations200Response recommendations(List<@Valid GetQuestRecommendations200ResponseRecommendationsInner> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public GetQuestRecommendations200Response addRecommendationsItem(GetQuestRecommendations200ResponseRecommendationsInner recommendationsItem) {
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
  public List<@Valid GetQuestRecommendations200ResponseRecommendationsInner> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<@Valid GetQuestRecommendations200ResponseRecommendationsInner> recommendations) {
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
    GetQuestRecommendations200Response getQuestRecommendations200Response = (GetQuestRecommendations200Response) o;
    return Objects.equals(this.recommendations, getQuestRecommendations200Response.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetQuestRecommendations200Response {\n");
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

