package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.Bottleneck;
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
 * DetectBottlenecks200Response
 */

@JsonTypeName("detectBottlenecks_200_response")

public class DetectBottlenecks200Response {

  @Valid
  private List<@Valid Bottleneck> bottlenecks = new ArrayList<>();

  @Valid
  private List<String> recommendations = new ArrayList<>();

  public DetectBottlenecks200Response bottlenecks(List<@Valid Bottleneck> bottlenecks) {
    this.bottlenecks = bottlenecks;
    return this;
  }

  public DetectBottlenecks200Response addBottlenecksItem(Bottleneck bottlenecksItem) {
    if (this.bottlenecks == null) {
      this.bottlenecks = new ArrayList<>();
    }
    this.bottlenecks.add(bottlenecksItem);
    return this;
  }

  /**
   * Get bottlenecks
   * @return bottlenecks
   */
  @Valid 
  @Schema(name = "bottlenecks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bottlenecks")
  public List<@Valid Bottleneck> getBottlenecks() {
    return bottlenecks;
  }

  public void setBottlenecks(List<@Valid Bottleneck> bottlenecks) {
    this.bottlenecks = bottlenecks;
  }

  public DetectBottlenecks200Response recommendations(List<String> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public DetectBottlenecks200Response addRecommendationsItem(String recommendationsItem) {
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
  
  @Schema(name = "recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<String> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<String> recommendations) {
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
    DetectBottlenecks200Response detectBottlenecks200Response = (DetectBottlenecks200Response) o;
    return Objects.equals(this.bottlenecks, detectBottlenecks200Response.bottlenecks) &&
        Objects.equals(this.recommendations, detectBottlenecks200Response.recommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bottlenecks, recommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetectBottlenecks200Response {\n");
    sb.append("    bottlenecks: ").append(toIndentedString(bottlenecks)).append("\n");
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

