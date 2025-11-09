package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RatingTrendPoint;
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
 * RatingTrend
 */


public class RatingTrend {

  /**
   * Gets or Sets window
   */
  public enum WindowEnum {
    _7D("7d"),
    
    _14D("14d"),
    
    _30D("30d"),
    
    _90D("90d"),
    
    SEASON("season");

    private final String value;

    WindowEnum(String value) {
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
    public static WindowEnum fromValue(String value) {
      for (WindowEnum b : WindowEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable WindowEnum window;

  @Valid
  private List<@Valid RatingTrendPoint> points = new ArrayList<>();

  public RatingTrend window(@Nullable WindowEnum window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  
  @Schema(name = "window", example = "30d", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("window")
  public @Nullable WindowEnum getWindow() {
    return window;
  }

  public void setWindow(@Nullable WindowEnum window) {
    this.window = window;
  }

  public RatingTrend points(List<@Valid RatingTrendPoint> points) {
    this.points = points;
    return this;
  }

  public RatingTrend addPointsItem(RatingTrendPoint pointsItem) {
    if (this.points == null) {
      this.points = new ArrayList<>();
    }
    this.points.add(pointsItem);
    return this;
  }

  /**
   * Get points
   * @return points
   */
  @Valid 
  @Schema(name = "points", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("points")
  public List<@Valid RatingTrendPoint> getPoints() {
    return points;
  }

  public void setPoints(List<@Valid RatingTrendPoint> points) {
    this.points = points;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingTrend ratingTrend = (RatingTrend) o;
    return Objects.equals(this.window, ratingTrend.window) &&
        Objects.equals(this.points, ratingTrend.points);
  }

  @Override
  public int hashCode() {
    return Objects.hash(window, points);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingTrend {\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
    sb.append("    points: ").append(toIndentedString(points)).append("\n");
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

