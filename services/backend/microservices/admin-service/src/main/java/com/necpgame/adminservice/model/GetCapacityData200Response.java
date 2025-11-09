package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.GetCapacityData200ResponseCurrentCapacity;
import com.necpgame.adminservice.model.GetCapacityData200ResponseProjectedCapacity;
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
 * GetCapacityData200Response
 */

@JsonTypeName("getCapacityData_200_response")

public class GetCapacityData200Response {

  private @Nullable GetCapacityData200ResponseCurrentCapacity currentCapacity;

  private @Nullable GetCapacityData200ResponseProjectedCapacity projectedCapacity;

  @Valid
  private List<String> scalingRecommendations = new ArrayList<>();

  public GetCapacityData200Response currentCapacity(@Nullable GetCapacityData200ResponseCurrentCapacity currentCapacity) {
    this.currentCapacity = currentCapacity;
    return this;
  }

  /**
   * Get currentCapacity
   * @return currentCapacity
   */
  @Valid 
  @Schema(name = "current_capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_capacity")
  public @Nullable GetCapacityData200ResponseCurrentCapacity getCurrentCapacity() {
    return currentCapacity;
  }

  public void setCurrentCapacity(@Nullable GetCapacityData200ResponseCurrentCapacity currentCapacity) {
    this.currentCapacity = currentCapacity;
  }

  public GetCapacityData200Response projectedCapacity(@Nullable GetCapacityData200ResponseProjectedCapacity projectedCapacity) {
    this.projectedCapacity = projectedCapacity;
    return this;
  }

  /**
   * Get projectedCapacity
   * @return projectedCapacity
   */
  @Valid 
  @Schema(name = "projected_capacity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("projected_capacity")
  public @Nullable GetCapacityData200ResponseProjectedCapacity getProjectedCapacity() {
    return projectedCapacity;
  }

  public void setProjectedCapacity(@Nullable GetCapacityData200ResponseProjectedCapacity projectedCapacity) {
    this.projectedCapacity = projectedCapacity;
  }

  public GetCapacityData200Response scalingRecommendations(List<String> scalingRecommendations) {
    this.scalingRecommendations = scalingRecommendations;
    return this;
  }

  public GetCapacityData200Response addScalingRecommendationsItem(String scalingRecommendationsItem) {
    if (this.scalingRecommendations == null) {
      this.scalingRecommendations = new ArrayList<>();
    }
    this.scalingRecommendations.add(scalingRecommendationsItem);
    return this;
  }

  /**
   * Get scalingRecommendations
   * @return scalingRecommendations
   */
  
  @Schema(name = "scaling_recommendations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scaling_recommendations")
  public List<String> getScalingRecommendations() {
    return scalingRecommendations;
  }

  public void setScalingRecommendations(List<String> scalingRecommendations) {
    this.scalingRecommendations = scalingRecommendations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCapacityData200Response getCapacityData200Response = (GetCapacityData200Response) o;
    return Objects.equals(this.currentCapacity, getCapacityData200Response.currentCapacity) &&
        Objects.equals(this.projectedCapacity, getCapacityData200Response.projectedCapacity) &&
        Objects.equals(this.scalingRecommendations, getCapacityData200Response.scalingRecommendations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentCapacity, projectedCapacity, scalingRecommendations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCapacityData200Response {\n");
    sb.append("    currentCapacity: ").append(toIndentedString(currentCapacity)).append("\n");
    sb.append("    projectedCapacity: ").append(toIndentedString(projectedCapacity)).append("\n");
    sb.append("    scalingRecommendations: ").append(toIndentedString(scalingRecommendations)).append("\n");
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

