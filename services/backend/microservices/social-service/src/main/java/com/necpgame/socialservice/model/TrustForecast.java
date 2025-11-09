package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.TrustForecastPoint;
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
 * TrustForecast
 */


public class TrustForecast {

  /**
   * Gets or Sets horizonHours
   */
  public enum HorizonHoursEnum {
    NUMBER_24(24),
    
    NUMBER_72(72);

    private final Integer value;

    HorizonHoursEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static HorizonHoursEnum fromValue(Integer value) {
      for (HorizonHoursEnum b : HorizonHoursEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private HorizonHoursEnum horizonHours;

  private Float baseline;

  @Valid
  private List<@Valid TrustForecastPoint> points = new ArrayList<>();

  public TrustForecast() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TrustForecast(HorizonHoursEnum horizonHours, Float baseline, List<@Valid TrustForecastPoint> points) {
    this.horizonHours = horizonHours;
    this.baseline = baseline;
    this.points = points;
  }

  public TrustForecast horizonHours(HorizonHoursEnum horizonHours) {
    this.horizonHours = horizonHours;
    return this;
  }

  /**
   * Get horizonHours
   * @return horizonHours
   */
  @NotNull 
  @Schema(name = "horizonHours", example = "72", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("horizonHours")
  public HorizonHoursEnum getHorizonHours() {
    return horizonHours;
  }

  public void setHorizonHours(HorizonHoursEnum horizonHours) {
    this.horizonHours = horizonHours;
  }

  public TrustForecast baseline(Float baseline) {
    this.baseline = baseline;
    return this;
  }

  /**
   * Get baseline
   * @return baseline
   */
  @NotNull 
  @Schema(name = "baseline", example = "62.5", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseline")
  public Float getBaseline() {
    return baseline;
  }

  public void setBaseline(Float baseline) {
    this.baseline = baseline;
  }

  public TrustForecast points(List<@Valid TrustForecastPoint> points) {
    this.points = points;
    return this;
  }

  public TrustForecast addPointsItem(TrustForecastPoint pointsItem) {
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
  @NotNull @Valid 
  @Schema(name = "points", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("points")
  public List<@Valid TrustForecastPoint> getPoints() {
    return points;
  }

  public void setPoints(List<@Valid TrustForecastPoint> points) {
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
    TrustForecast trustForecast = (TrustForecast) o;
    return Objects.equals(this.horizonHours, trustForecast.horizonHours) &&
        Objects.equals(this.baseline, trustForecast.baseline) &&
        Objects.equals(this.points, trustForecast.points);
  }

  @Override
  public int hashCode() {
    return Objects.hash(horizonHours, baseline, points);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TrustForecast {\n");
    sb.append("    horizonHours: ").append(toIndentedString(horizonHours)).append("\n");
    sb.append("    baseline: ").append(toIndentedString(baseline)).append("\n");
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

