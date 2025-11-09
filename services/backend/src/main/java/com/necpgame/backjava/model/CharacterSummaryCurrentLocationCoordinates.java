package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterSummaryCurrentLocationCoordinates
 */

@JsonTypeName("CharacterSummary_currentLocation_coordinates")

public class CharacterSummaryCurrentLocationCoordinates {

  private @Nullable Float x;

  private @Nullable Float y;

  private @Nullable Float z;

  public CharacterSummaryCurrentLocationCoordinates x(@Nullable Float x) {
    this.x = x;
    return this;
  }

  /**
   * Get x
   * @return x
   */
  
  @Schema(name = "x", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("x")
  public @Nullable Float getX() {
    return x;
  }

  public void setX(@Nullable Float x) {
    this.x = x;
  }

  public CharacterSummaryCurrentLocationCoordinates y(@Nullable Float y) {
    this.y = y;
    return this;
  }

  /**
   * Get y
   * @return y
   */
  
  @Schema(name = "y", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("y")
  public @Nullable Float getY() {
    return y;
  }

  public void setY(@Nullable Float y) {
    this.y = y;
  }

  public CharacterSummaryCurrentLocationCoordinates z(@Nullable Float z) {
    this.z = z;
    return this;
  }

  /**
   * Get z
   * @return z
   */
  
  @Schema(name = "z", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("z")
  public @Nullable Float getZ() {
    return z;
  }

  public void setZ(@Nullable Float z) {
    this.z = z;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSummaryCurrentLocationCoordinates characterSummaryCurrentLocationCoordinates = (CharacterSummaryCurrentLocationCoordinates) o;
    return Objects.equals(this.x, characterSummaryCurrentLocationCoordinates.x) &&
        Objects.equals(this.y, characterSummaryCurrentLocationCoordinates.y) &&
        Objects.equals(this.z, characterSummaryCurrentLocationCoordinates.z);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSummaryCurrentLocationCoordinates {\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
    sb.append("    z: ").append(toIndentedString(z)).append("\n");
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

