package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.ExtractionPoint;
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
 * GetExtractionPoints200Response
 */

@JsonTypeName("getExtractionPoints_200_response")

public class GetExtractionPoints200Response {

  @Valid
  private List<@Valid ExtractionPoint> extractionPoints = new ArrayList<>();

  public GetExtractionPoints200Response extractionPoints(List<@Valid ExtractionPoint> extractionPoints) {
    this.extractionPoints = extractionPoints;
    return this;
  }

  public GetExtractionPoints200Response addExtractionPointsItem(ExtractionPoint extractionPointsItem) {
    if (this.extractionPoints == null) {
      this.extractionPoints = new ArrayList<>();
    }
    this.extractionPoints.add(extractionPointsItem);
    return this;
  }

  /**
   * Get extractionPoints
   * @return extractionPoints
   */
  @Valid 
  @Schema(name = "extraction_points", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("extraction_points")
  public List<@Valid ExtractionPoint> getExtractionPoints() {
    return extractionPoints;
  }

  public void setExtractionPoints(List<@Valid ExtractionPoint> extractionPoints) {
    this.extractionPoints = extractionPoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetExtractionPoints200Response getExtractionPoints200Response = (GetExtractionPoints200Response) o;
    return Objects.equals(this.extractionPoints, getExtractionPoints200Response.extractionPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extractionPoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetExtractionPoints200Response {\n");
    sb.append("    extractionPoints: ").append(toIndentedString(extractionPoints)).append("\n");
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

