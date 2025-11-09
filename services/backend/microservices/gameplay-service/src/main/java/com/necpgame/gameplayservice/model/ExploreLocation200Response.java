package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExploreLocation200Response
 */

@JsonTypeName("exploreLocation_200_response")

public class ExploreLocation200Response {

  private @Nullable String description;

  @Valid
  private List<String> pointsOfInterest = new ArrayList<>();

  @Valid
  private JsonNullable<List<String>> hiddenObjects = JsonNullable.<List<String>>undefined();

  public ExploreLocation200Response description(@Nullable String description) {
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

  public ExploreLocation200Response pointsOfInterest(List<String> pointsOfInterest) {
    this.pointsOfInterest = pointsOfInterest;
    return this;
  }

  public ExploreLocation200Response addPointsOfInterestItem(String pointsOfInterestItem) {
    if (this.pointsOfInterest == null) {
      this.pointsOfInterest = new ArrayList<>();
    }
    this.pointsOfInterest.add(pointsOfInterestItem);
    return this;
  }

  /**
   * Get pointsOfInterest
   * @return pointsOfInterest
   */
  
  @Schema(name = "pointsOfInterest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pointsOfInterest")
  public List<String> getPointsOfInterest() {
    return pointsOfInterest;
  }

  public void setPointsOfInterest(List<String> pointsOfInterest) {
    this.pointsOfInterest = pointsOfInterest;
  }

  public ExploreLocation200Response hiddenObjects(List<String> hiddenObjects) {
    this.hiddenObjects = JsonNullable.of(hiddenObjects);
    return this;
  }

  public ExploreLocation200Response addHiddenObjectsItem(String hiddenObjectsItem) {
    if (this.hiddenObjects == null || !this.hiddenObjects.isPresent()) {
      this.hiddenObjects = JsonNullable.of(new ArrayList<>());
    }
    this.hiddenObjects.get().add(hiddenObjectsItem);
    return this;
  }

  /**
   * Get hiddenObjects
   * @return hiddenObjects
   */
  
  @Schema(name = "hiddenObjects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hiddenObjects")
  public JsonNullable<List<String>> getHiddenObjects() {
    return hiddenObjects;
  }

  public void setHiddenObjects(JsonNullable<List<String>> hiddenObjects) {
    this.hiddenObjects = hiddenObjects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExploreLocation200Response exploreLocation200Response = (ExploreLocation200Response) o;
    return Objects.equals(this.description, exploreLocation200Response.description) &&
        Objects.equals(this.pointsOfInterest, exploreLocation200Response.pointsOfInterest) &&
        equalsNullable(this.hiddenObjects, exploreLocation200Response.hiddenObjects);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, pointsOfInterest, hashCodeNullable(hiddenObjects));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExploreLocation200Response {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    pointsOfInterest: ").append(toIndentedString(pointsOfInterest)).append("\n");
    sb.append("    hiddenObjects: ").append(toIndentedString(hiddenObjects)).append("\n");
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

