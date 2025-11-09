package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ZoneBoundaries
 */

@JsonTypeName("Zone_boundaries")

public class ZoneBoundaries {

  private @Nullable BigDecimal minX;

  private @Nullable BigDecimal minY;

  private @Nullable BigDecimal maxX;

  private @Nullable BigDecimal maxY;

  public ZoneBoundaries minX(@Nullable BigDecimal minX) {
    this.minX = minX;
    return this;
  }

  /**
   * Get minX
   * @return minX
   */
  @Valid 
  @Schema(name = "minX", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minX")
  public @Nullable BigDecimal getMinX() {
    return minX;
  }

  public void setMinX(@Nullable BigDecimal minX) {
    this.minX = minX;
  }

  public ZoneBoundaries minY(@Nullable BigDecimal minY) {
    this.minY = minY;
    return this;
  }

  /**
   * Get minY
   * @return minY
   */
  @Valid 
  @Schema(name = "minY", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minY")
  public @Nullable BigDecimal getMinY() {
    return minY;
  }

  public void setMinY(@Nullable BigDecimal minY) {
    this.minY = minY;
  }

  public ZoneBoundaries maxX(@Nullable BigDecimal maxX) {
    this.maxX = maxX;
    return this;
  }

  /**
   * Get maxX
   * @return maxX
   */
  @Valid 
  @Schema(name = "maxX", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxX")
  public @Nullable BigDecimal getMaxX() {
    return maxX;
  }

  public void setMaxX(@Nullable BigDecimal maxX) {
    this.maxX = maxX;
  }

  public ZoneBoundaries maxY(@Nullable BigDecimal maxY) {
    this.maxY = maxY;
    return this;
  }

  /**
   * Get maxY
   * @return maxY
   */
  @Valid 
  @Schema(name = "maxY", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxY")
  public @Nullable BigDecimal getMaxY() {
    return maxY;
  }

  public void setMaxY(@Nullable BigDecimal maxY) {
    this.maxY = maxY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneBoundaries zoneBoundaries = (ZoneBoundaries) o;
    return Objects.equals(this.minX, zoneBoundaries.minX) &&
        Objects.equals(this.minY, zoneBoundaries.minY) &&
        Objects.equals(this.maxX, zoneBoundaries.maxX) &&
        Objects.equals(this.maxY, zoneBoundaries.maxY);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minX, minY, maxX, maxY);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneBoundaries {\n");
    sb.append("    minX: ").append(toIndentedString(minX)).append("\n");
    sb.append("    minY: ").append(toIndentedString(minY)).append("\n");
    sb.append("    maxX: ").append(toIndentedString(maxX)).append("\n");
    sb.append("    maxY: ").append(toIndentedString(maxY)).append("\n");
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

